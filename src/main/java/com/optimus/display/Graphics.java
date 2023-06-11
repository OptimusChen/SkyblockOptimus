package com.optimus.display;

import com.optimus.util.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Graphics {

    private static final FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
    private String title = null;
    private String subTitle = null;
    private int titleColor = Color.WHITE.getRGB();
    private final List<Text> texts = new ArrayList<>();

    public Graphics() { }

    @SubscribeEvent
    public void onRenderTick(RenderGameOverlayEvent.Text event) {
        if (title != null) {
            Util.drawCenteredString(title, titleColor, 3f, 4f);
        }

        if (subTitle != null) {
            Util.drawCenteredString(subTitle, titleColor, 1.75f, 2.25f);
        }

        for (Text t : texts) {
            if (t == null) continue;
            String text = t.getText();

            GL11.glPushMatrix();

            font.drawStringWithShadow(text, t.getX(), t.getY(), t.getColor());

            GL11.glPopMatrix();
        }
    }

    public void sendTitle(String title, String subTitle, int color) {
        this.title = title;
        this.subTitle = subTitle;
        this.titleColor = color;

        Util.delayTask(2000, () -> {
            this.title = null;
            this.subTitle = null;
        });
    }

    public void drawTemporaryText(String text, int x, int y, int color, int time) {
        Text t = new Text(text, color, x, y);

        texts.add(t);

        Util.delayTask(time, () -> {
            texts.remove(t);
        });
    }

    @Getter
    @AllArgsConstructor
    static class Text {
        private String text;
        private int color, x, y;
    }
}
