package cc.hyperium.mods.glintcolorizer.gui;

import cc.hyperium.gui.main.HyperiumOverlay;
import cc.hyperium.gui.main.components.OverlaySlider;
import cc.hyperium.mods.glintcolorizer.Colors;
import cc.hyperium.mods.glintcolorizer.GlintColorizer;

import java.lang.reflect.Field;

public class GlintColorizerSettings extends HyperiumOverlay {
    public GlintColorizerSettings() {
        super("Glint Colorizer");
        try {
            this.addToggle("Enabled", GlintColorizer.class.getField("enabled"), (b) -> {
                if ((boolean) b) {
                    Colors.setonepoint8color(Colors.glintR, Colors.glintG, Colors.glintB);
                } else {
                    Colors.onepoint8glintcolorI = -8372020;
                }
            }, true, this);
            this.addToggle("Chroma", Colors.class.getField("chroma"), null, true, this);
            addSlider("Red", Colors.class.getField("glintR"), 255, 0);
            addSlider("Green", Colors.class.getField("glintG"), 255, 0);
            addSlider("Blue", Colors.class.getField("glintB"), 255, 0);
            addSlider("Cycle speed", Colors.class.getField("chromaSpeed"), 10, 1);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void addSlider(String label, Field f, int max, int min) {
        f.setAccessible(true);
        try {
            this.getComponents().add(new OverlaySlider(label, min, max, ((Integer) f.get(null)).floatValue(), (i) -> {
                try {
                    f.set(null, i.intValue());
                    Colors.setonepoint8color(Colors.glintR, Colors.glintG, Colors.glintB);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }, true));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
