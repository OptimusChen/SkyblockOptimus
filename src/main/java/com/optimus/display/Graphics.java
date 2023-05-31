package com.optimus.display;

import com.optimus.util.Util;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class Graphics {

    private String title = null;
    private String subTitle = null;
    private int titleColor = Color.WHITE.getRGB();

    public Graphics() { }

    @SubscribeEvent
    public void onRenderTick(RenderGameOverlayEvent.Text event) {
        if (title != null) {
            Util.drawCenteredString(title, titleColor, 3f, 4f);
        }

        if (subTitle != null) {
            Util.drawCenteredString(subTitle, titleColor, 1.75f, 2.25f);
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
}
