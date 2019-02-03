package uprizing.draw.defaults;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import uprizing.draw.Drawer;
import uprizing.gui.GuiUprizingMenu;
import uprizing.setting.Setting;
import uprizing.util.Constants;

public class SettingDrawer implements Drawer {

    protected final Setting setting;
    protected final int x;
    @Getter protected int y;
    protected final int width;
    protected final int textX;
    protected int textY;

    public SettingDrawer(final Setting setting, final int x, final int y, final int width) {
        this.setting = setting;
        this.x = x;
        this.y = y;
        this.width = width;
        this.textX = x + 4 + 2 + 1;
        this.textY = y + 1;
    }

    @Override
    public int getDrawingHeight() {
        return setting.getHeight();
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY, GuiUprizingMenu.Scrollbar scrollbar) {
        return mouseX >= x && mouseY >= y - scrollbar.posY && mouseX < x + width && mouseY < y + setting.getHeight() - scrollbar.posY;
    }

    @Override
    public void draw(GuiUprizingMenu gui, FontRenderer fontRenderer, GuiUprizingMenu.Scrollbar scrollbar, int mouseX, int mouseY) {
        int textColor = Constants.defaultTextColor;

        if (isHovered(mouseX, mouseY, scrollbar)) {
            //gui.drawSexyRect(x, y, width, setting.getDrawingHeight(), Constants2.C);
            textColor = Constants.hoveredTextColor;
        }

        fontRenderer.drawString(setting.getName() + ": " + setting.getAsString(), textX, textY - scrollbar.posY, textColor, false);
    }

    @Override
    public void handleMouseClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        setting.handleMouseClick(minecraft, mouseButton);
    }
}