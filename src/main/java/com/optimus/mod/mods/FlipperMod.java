package com.optimus.mod.mods;

import com.optimus.mod.Module;
import com.optimus.mod.ModuleGui;
import com.optimus.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class FlipperMod extends Module {

    public static HashMap<String, String> names = new HashMap<>();

    private String mode = "Auction";
    private FlipperGui gui;
    public List<Flip> flips = new ArrayList<>();

    @Override
    public void init() {
        this.gui = new FlipperGui(this);
    }

    @Override
    public String getName() {
        return "CraftFlipper";
    }

    @Override
    public ModuleGui getGUI() {
        return gui;
    }
    public void incrementMode() {
        switch (mode) {
            case "Auction":
                mode = "Bazaar";
                break;
            case "Bazaar":
                mode = "NPC";
                break;
            case "NPC":
                mode = "Auction";
                break;
        }
    }

    private static final List<String> RECIPE_SLOTS = Arrays.asList("A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3");

    private Double getBuyPrice(String itemId) {
        if (buyInstantly.containsKey(itemId)) return buyInstantly.get(itemId);
        if (lowestBins.containsKey(itemId)) return Double.valueOf(lowestBins.get(itemId));
        return 0.0;
    }

    private Double getSellPrice(String itemId) {
        if (sellInstantly.containsKey(itemId)) return buyInstantly.get(itemId);
        if (lowestBins.containsKey(itemId)) return Double.valueOf(lowestBins.get(itemId));
        return 0.0;
    }

    public HashMap<String, List<RecipeItem>> recipes;
    public HashMap<String, Double> sellInstantly;
    public HashMap<String, Double> buyInstantly;
    public HashMap<String, Integer> lowestBins;
    private List<Flip> calculateFlips() {
        File folder = new File(((FlipperGui) getGUI()).textField.getText());

        if (!folder.exists()) {
            Minecraft.getMinecraft().addScheduledTask(() -> getGUI().setStatusText("Items folder not found", Color.RED.getRGB()));
            return new ArrayList<>();
        }

        recipes = new HashMap<>();

        Minecraft.getMinecraft().addScheduledTask(() -> getGUI().setStatusText("Parsing Items...", Color.GREEN.getRGB()));

        for (File file : folder.listFiles()) {
            try {
                JSONObject item = new JSONObject(new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8));

                boolean hasRecipe = item.has("recipe");

                if (!hasRecipe) continue;

                String internalName = item.getString("internalname");
                JSONObject recipe = item.getJSONObject("recipe");

                List<RecipeItem> items = new ArrayList<>();

                for (String slot : RECIPE_SLOTS) {
                    String ingredient = recipe.getString(slot);

                    if (ingredient.equals("")) continue;

                    String id = ingredient.split(":")[0];
                    int amount = Integer.parseInt(ingredient.split(":")[1]);

                    RecipeItem craft = new RecipeItem(id);
                    craft.addAmount(amount);

                    for (RecipeItem i : items) {
                        if (i.equals(craft)) {
                            i.addAmount(craft.amount);
                            craft.amount = -1;
                        }
                    }

                    if (craft.getAmount() != -1) items.add(craft);
                }

                recipes.put(internalName, items);
            } catch (Exception ignored) { }
        }

        Minecraft.getMinecraft().addScheduledTask(() -> getGUI().setStatusText("Calculating Lowest Bins...", Color.GREEN.getRGB()));

        System.out.printf("Finished Parsing %s items%n", recipes.size());

        int amount = Util.getFromURL("https://api.hypixel.net/skyblock/auctions").getInt("totalPages");

        HashMap<String, String> itemIds = new HashMap<>();
        sellInstantly = new HashMap<>();

        JSONObject items = Util.getFromURL("https://api.hypixel.net/resources/skyblock/items");

        for (Object o : items.getJSONArray("items")) {
            JSONObject item = (JSONObject) o;

            itemIds.put(item.getString("name"), item.getString("id"));
            names.put(item.getString("id"), item.getString("name"));

            if (item.has("npc_sell_price") && mode.equals("NPC")) sellInstantly.put(item.getString("id"), (double) item.getInt("npc_sell_price"));
        }

        HashMap<String, List<Integer>> prices = new HashMap<>();

        for (int page = 0; page < amount; page++) {
            int finalPage = page;
            Minecraft.getMinecraft().addScheduledTask(() -> getGUI().setStatusText("Calculating Lowest Bins (" + finalPage + "/" + amount + ")...", Color.GREEN.getRGB()));

            JSONObject json = Util.getFromURL("https://api.hypixel.net/skyblock/auctions?page=" + page);

            for (Object o : json.getJSONArray("auctions")) {
                JSONObject auction = (JSONObject) o;
                String itemId = itemIds.get(auction.getString("item_name"));

                if (!auction.getBoolean("bin")) continue;
                if (!prices.containsKey(itemId)) prices.put(itemId, new ArrayList<Integer>());

                prices.get(itemId).add(auction.getInt("starting_bid"));
            }
        }

        lowestBins = new HashMap<>();

        for (String id : prices.keySet()) {
            List<Integer> auctionPrices = prices.get(id);
            int min = auctionPrices.get(0);

            for (int i : auctionPrices) min = Math.min(min, i);

            lowestBins.put(id, min);
        }

        Minecraft.getMinecraft().addScheduledTask(() -> getGUI().setStatusText("Calculating Bazaar Buys...", Color.GREEN.getRGB()));

        JSONObject bazaar = Util.getFromURL("https://api.hypixel.net/skyblock/bazaar");
        buyInstantly = new HashMap<>();

        for (String s : bazaar.getJSONObject("products").keySet()) {
            JSONObject product = bazaar.getJSONObject("products").getJSONObject(s);
            JSONObject quickStatus = product.getJSONObject("quick_status");

            double buyPrice = quickStatus.getDouble("buyPrice");

            buyInstantly.put(product.getString("product_id"), buyPrice);
            if (mode.equals("Bazaar")) sellInstantly.put(product.getString("product_id"), quickStatus.getDouble("sellPrice"));
        }

        HashMap<String, Integer> diffs = new HashMap<>();


        Minecraft.getMinecraft().addScheduledTask(() -> getGUI().setStatusText("Calculating Profit...", Color.GREEN.getRGB()));
        for (String id : recipes.keySet()) {
            if (!mode.equals("Auction") && !sellInstantly.containsKey(id)) continue;
            if (mode.equals("Auction") && !lowestBins.containsKey(id)) continue;

            List<RecipeItem> recipe = recipes.get(id);

            int total = 0;
            boolean invalidIngredient = false;

            for (RecipeItem ingredient : recipe) {
                if (buyInstantly.containsKey(ingredient.getId())) {
                    total += ingredient.getAmount() * buyInstantly.get(ingredient.getId());
                } else if (lowestBins.containsKey(ingredient.getId())) {
                    total += ingredient.getAmount() * lowestBins.get(ingredient.getId());
                } else {
                    invalidIngredient = true;
                    break;
                }
            }

            if (invalidIngredient) continue;

            Double lowestBin = sellInstantly.get(id);
            if (mode.equals("Auction")) lowestBin = Double.valueOf(lowestBins.get(id));
            int diff = (int) (lowestBin - total);

            diffs.put(id, diff);
        }

        List<Flip> flips = new ArrayList<>();

        for (String id : Util.sort(diffs).keySet()) {
            if (diffs.get(id) < 0) continue;
            flips.add(new Flip(id, diffs.get(id)));
        }

        Minecraft.getMinecraft().addScheduledTask(() -> getGUI().setStatusText("Done!", Color.GREEN.getRGB()));

        return flips;
    }

    static class RecipeItem {
        private final String id;
        private int amount;

        public RecipeItem(String id) {
            this.id = id;
        }

        public String getId() { return id; }
        public int getAmount() { return amount; }
        public void addAmount(int i) { amount += i; }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof RecipeItem) {
                return Objects.equals(((RecipeItem) obj).id, getId());
            }

            return false;
        }

        @Override
        public String toString() {
            return String.format("x%s %s", amount, names.get(id));
        }
    }

    static class Flip {
        public String itemId;
        public int profit;

        public Flip(String id, int p) {
            itemId = id;
            profit = p;
        }
    }

    static class FlipList extends GuiScrollingList {
        public List<Flip> elements;
        private int selected = -1;

        public FlipList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, List<Flip> elements) {
            super(client, width, height, top, bottom, left, entryHeight);

            this.elements = elements;
        }

        @Override
        protected int getSize() {
            return elements.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {
            selected = index;
        }

        @Override
        protected boolean isSelected(int index) {
            return index == selected;
        }

        @Override
        protected void drawBackground() {
        }

        @Override
        protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2) {
        }

        @Override
        protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
            DecimalFormat format = new DecimalFormat("#,###");

            Flip flip = elements.get(slotIdx);
            FontRenderer font = Minecraft.getMinecraft().fontRendererObj;

            String text = names.get(flip.itemId) + " - " + format.format(flip.profit);
            font.drawString(text, entryRight - 200, slotTop + 2, Color.WHITE.getRGB());
        }
    }

    static class FlipperGui extends ModuleGui {
        private GuiTextField textField;
        private FlipList list;
        private final FlipperMod flipper;
        public FlipperGui(Module mod) {
            super(mod);

            this.flipper = (FlipperMod) mod;
        }

        @Override
        public void initGui() {
            super.initGui();

            this.buttonList.add(new GuiButton(2, width / 2, height / 2 - 60, "Mode: " + flipper.mode));
            this.buttonList.add(new GuiButton(0, width / 2, height / 2 - 30, "Calculate!"));

            this.textField = new GuiTextField(1, mc.fontRendererObj, width / 2, height / 2, 200, 20);
            this.textField.setMaxStringLength(Integer.MAX_VALUE);
            this.textField.setText(Util.getMinecraftDir().getPath() + "/config/notenoughupdates/repo/items");

            this.list = new FlipList(mc, 215, height - 10, 0, height, 0, 20, flipper.flips);
        }

        @Override
        protected void actionPerformed(GuiButton button) throws IOException {
            super.actionPerformed(button);

            list.actionPerformed(button);

            if (button.id == 0) {
                flipper.flips = new ArrayList<>();
                list.selected = -1;

                Thread thread = new Thread(() -> {
                    List<Flip> flips = flipper.calculateFlips();

                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        flipper.flips = flips;

                        list.elements = flips;
                    });
                });

                thread.start();
            } else if (button.id == 2) {
                flipper.incrementMode();
                button.displayString = "Mode: " + flipper.mode;
            }
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            drawDefaultBackground();

            textField.drawTextBox();
            list.drawScreen(mouseX, mouseY, partialTicks);

            DecimalFormat format = new DecimalFormat("#,###");

            if (list.selected != -1) {
                Flip flip = list.elements.get(list.selected);

                drawString(fontRendererObj, names.get(flip.itemId) + " - " + format.format(Math.round(flipper.getSellPrice(flip.itemId))), width / 2, height / 2 + 30, Color.WHITE.getRGB());

                int yOffset = 20;
                for (RecipeItem item : flipper.recipes.get(flip.itemId)) {
                    drawString(fontRendererObj, item.toString() + " - " + format.format(Math.round(item.getAmount() * flipper.getBuyPrice(item.id))), width / 2, height / 2 + 30 + yOffset, Color.WHITE.getRGB());
                    yOffset += 20;
                }
            }

            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        @Override
        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            textField.mouseClicked(mouseX, mouseY, mouseButton);
        }

        @Override
        protected void keyTyped(char typedChar, int keyCode) throws IOException {
            super.keyTyped(typedChar, keyCode);
            textField.textboxKeyTyped(typedChar, keyCode);
        }

        @Override
        public void updateScreen() {
            super.updateScreen();
            textField.updateCursorCounter();
        }
    }
}
