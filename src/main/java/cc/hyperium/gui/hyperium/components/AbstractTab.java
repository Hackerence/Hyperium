package cc.hyperium.gui.hyperium.components;

import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.SimpleAnimValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by Cubxity on 27/08/2018
 */
public abstract class AbstractTab {
    private SimpleAnimValue scrollAnim = new SimpleAnimValue(0L, 0f, 0f);
    protected List<AbstractTabComponent> components = new ArrayList<>();
    protected Map<AbstractTabComponent, Boolean> clickStates = new HashMap<>();
    protected HyperiumMainGui gui;
    protected String title;
    private int scroll = 0;

    public AbstractTab(HyperiumMainGui gui, String title) {
        this.gui = gui;
        this.title = title;
    }

    public void render(int x, int y, int width, int height) {
        ScaledResolution sr = ResolutionUtil.current();
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();
        int yg = height / 10;  // Y grid
        int xg = width / 11;   // X grid

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        int sf = sr.getScaleFactor();
        GL11.glScissor(x * sf, (y - yg) * sf, width * sf, height * sf - (yg + 8) * sf);
        final int mx = Mouse.getX() * sw / Minecraft.getMinecraft().displayWidth;           // Mouse X
        final int my = sh - Mouse.getY() * sh / Minecraft.getMinecraft().displayHeight - 1; // Mouse Y

        if (scrollAnim.getValue() != scroll * 18 && scrollAnim.isFinished())
            scrollAnim = new SimpleAnimValue(1000L, scrollAnim.getValue(), scroll * 18);
        y += scrollAnim.getValue();
        for (AbstractTabComponent comp : components) {
            comp.render(x, y, width, mx, my);

            if (mx >= x && mx <= x + width && my > y && my <= y + comp.getHeight()) {
                comp.hover = true;
                if (Mouse.isButtonDown(0)) {
                    if (!clickStates.computeIfAbsent(comp, ignored -> false)) {
                        comp.onClick(mx, my - y /* Make the Y relevant to the component */);
                        clickStates.put(comp, true);
                    }
                } else if (clickStates.computeIfAbsent(comp, ignored -> false))
                    clickStates.put(comp, false);
            } else
                comp.hover = false;
            y += comp.getHeight();
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public String getTitle() {
        return title;
    }

    public void handleMouseInput() {
        if (Mouse.getEventDWheel() > 0)
            scroll++;
        else if (Mouse.getEventDWheel() < 0)
            scroll--;
    }
}