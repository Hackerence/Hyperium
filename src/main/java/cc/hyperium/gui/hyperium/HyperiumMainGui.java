package cc.hyperium.gui.hyperium;

import cc.hyperium.Metadata;
import cc.hyperium.config.Settings;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.gui.hyperium.components.AbstractTab;
import cc.hyperium.gui.hyperium.tabs.SettingsTab;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ResourceLocation;

import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/*
 * Created by Cubxity on 27/08/2018
 */
public class HyperiumMainGui extends HyperiumGui {
    private static int tabIndex = 0; // save tab position

    private HyperiumFontRenderer smol = new HyperiumFontRenderer(Settings.GUI_FONT, Font.PLAIN, 14);
    private HyperiumFontRenderer font = new HyperiumFontRenderer(Settings.GUI_FONT, Font.PLAIN, 20);
    private HyperiumFontRenderer title = new HyperiumFontRenderer(Settings.GUI_FONT, Font.PLAIN, 30);
    private List<AbstractTab> tabs = Arrays.asList(
            new SettingsTab(this)
    );
    private AbstractTab currentTab;

    public HyperiumMainGui() {
        setTab(tabIndex);
    }

    @Override
    protected void pack() {
        Method loadShaderMethod = null;
        try {
            loadShaderMethod = EntityRenderer.class.getDeclaredMethod("loadShader", ResourceLocation.class);
        } catch (NoSuchMethodException e) {
            try {
                loadShaderMethod = EntityRenderer.class.getDeclaredMethod("a", ResourceLocation.class);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        }

        if (loadShaderMethod != null) {
            loadShaderMethod.setAccessible(true);
            try {
                loadShaderMethod.invoke(Minecraft.getMinecraft().entityRenderer, new ResourceLocation("shaders/hyperium_blur.json"));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int yg = height / 10;  // Y grid
        int xg = width / 10;   // X grid

        if (Minecraft.getMinecraft().theWorld == null)
            drawDefaultBackground(); //TODO: Make it draw custom background

        // Header
        drawRect(xg, yg, xg * 9, yg * 2, 0x64000000);
        drawRect(xg, yg * 2, xg * 9, yg * 9, 0x28000000);
        title.drawCenteredString(currentTab.getTitle().toUpperCase(), width / 2, yg + (yg / 2 - 8), 0xffffff);

        // Body
        currentTab.render(xg, yg * 2, xg * 8, yg * 8);

        // Footer
        smol.drawString(Metadata.getVersion(), width - smol.getWidth(Metadata.getVersion()) - 1, height - 10, 0xffffffff);
    }

    public HyperiumFontRenderer getFont() {
        return font;
    }

    public HyperiumFontRenderer getTitle() {
        return title;
    }

    public void setTab(int i) {
        tabIndex = i;
        currentTab = tabs.get(i);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Minecraft.getMinecraft().entityRenderer.stopUseShader();
    }
}