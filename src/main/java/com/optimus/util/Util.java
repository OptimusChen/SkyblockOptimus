package com.optimus.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.json.JSONObject;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Util {

    // ChatGPT
    public static HashMap<String, Integer> sort(HashMap<String, Integer> map) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());

        // Sort the list based on the values using a Comparator
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Create a new LinkedHashMap to store the sorted entries
        Map<String, Integer> sortedDictionary = new LinkedHashMap<>();

        // Add the sorted entries to the new LinkedHashMap
        for (Map.Entry<String, Integer> entry : entries) {
            sortedDictionary.put(entry.getKey(), entry.getValue());
        }

        return (HashMap<String, Integer>) sortedDictionary;
    }

    // ChatGPT
    public static JSONObject getFromURL(String url) {
        try {
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String json = response.toString();

                return new JSONObject(json);
            } else {
                System.out.println("Failed to retrieve JSON data from URL. HTTP response code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving JSON data from URL: " + e.getMessage());
        }

        return null;
    }

    public static File getMinecraftDir() {
        String os = System.getProperty("os.name").toLowerCase();
        String path;

        if (os.contains("win")) {
            // Windows OS
            path = System.getenv("APPDATA") + "/.minecraft";
        } else if (os.contains("mac")) {
            // Mac OS
            path = System.getProperty("user.home") + "/Library/Application Support/minecraft";
        } else {
            // Linux or other OS
            path = System.getProperty("user.home") + "/.minecraft";
        }

        File minecraftDir = new File(path);

        return minecraftDir;
    }

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void delayTask(int delaySeconds, Runnable run) {
        scheduler.schedule(run, delaySeconds, TimeUnit.MILLISECONDS);
    }

    public static void shutdownScheduler() {
        scheduler.shutdown();
    }

    public static boolean isGuiInstance(GuiScreen gui, Class<? extends GuiScreen> clazz) {
        return clazz.isInstance(gui);
    }

    public static BlockPos getLookingAtBlock() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        double maxDistance = 3.5;

        Vec3 playerPos = new Vec3(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 lookVec = player.getLookVec();
        Vec3 rayTraceEnd = playerPos.addVector(lookVec.xCoord * maxDistance, lookVec.yCoord * maxDistance, lookVec.zCoord * maxDistance);
        MovingObjectPosition objectMouseOver = player.worldObj.rayTraceBlocks(playerPos, rayTraceEnd, true);
        if (objectMouseOver != null && objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            return objectMouseOver.getBlockPos();
        }
        return null;
    }

    public static boolean isPlayerLookingAtChest(EntityPlayerSP player, BlockPos chestPos, World world) {
        Block chestBlock = world.getBlockState(chestPos).getBlock();
        if (chestBlock instanceof BlockChest) {
            EnumFacing playerFacing = player.getHorizontalFacing();
            EnumFacing chestFacing = world.getBlockState(chestPos).getValue(BlockChest.FACING);
            if (playerFacing == chestFacing.getOpposite()) {
                return true;
            }
        }
        return false;
    }

    public static Entity getTargetedEntity(EntityPlayerSP player) {
        double distance = 50;

        World world = player.worldObj;
        Vec3 vec3 = new Vec3(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 vec31 = player.getLook(1.0F).normalize();
        Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
        Entity pointedEntity = null;
        float f1 = 1.0F;
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().addCoord(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance).expand(f1, f1, f1));
        double d2 = distance;

        for (Entity entity : list) {
            if (entity.canBeCollidedWith()) {
                float f2 = entity.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(f2, f2, f2);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (0.0D < d2 || d2 == 0.0D) {
                        pointedEntity = entity;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        pointedEntity = entity;
                        d2 = d3;
                    }
                }
            }
        }
        return pointedEntity;
    }

    public static int getPrivateIntFieldFromObject(Object object, String forgeFieldName, String vanillaFieldName) throws NoSuchFieldException, SecurityException, NumberFormatException, IllegalArgumentException, IllegalAccessException {
        Field targetField = null;
        try {
            targetField = object.getClass().getDeclaredField(forgeFieldName);
        } catch (NoSuchFieldException e) {
            targetField = object.getClass().getDeclaredField(vanillaFieldName);
        }
        if (targetField != null) {
            targetField.setAccessible(true);
            return Integer.valueOf(targetField.get(object).toString()).intValue();
        } else {
            System.out.println("not found");
            return -1;
        }
    }

    public static void rightClick() {
        KeyBinding rc = Minecraft.getMinecraft().gameSettings.keyBindUseItem;
        int code = rc.getKeyCode();

        KeyBinding.setKeyBindState(code, !rc.isKeyDown());

        Util.delayTask(500, () -> KeyBinding.setKeyBindState(code, !rc.isKeyDown()));
    }

    public static void drawCenteredString(String text, int color, float y, float scale) {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);

        float width = fontRenderer.getStringWidth(text) * scale;

        fontRenderer.drawStringWithShadow(text, ((res.getScaledWidth() - width) / 2f) / scale, (res.getScaledHeight() / y) / scale, color);

        GL11.glPopMatrix();
    }
}
