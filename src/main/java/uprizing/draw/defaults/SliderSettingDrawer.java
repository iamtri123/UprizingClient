package uprizing.draw.defaults;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import uprizing.gui.GuiUprizingMenu;
import uprizing.setting.Setting;
import uprizing.util.Constants;

public class SliderSettingDrawer extends SettingDrawer {

    public SliderSettingDrawer(final Setting setting, final int x, final int y, final int width) {
        super(setting, x, y, width);
    }

    @Override
    public final void draw(GuiUprizingMenu gui, FontRenderer fontRenderer, GuiUprizingMenu.Scrollbar scrollbar, int mouseX, int mouseY) {
        int textColor = Constants.defaultTextColor;

        if (isHovered(mouseX, mouseY, scrollbar)) {
            textColor = Constants.hoveredTextColor;
            //gui.drawSexyRect(x, y, width, setting.getHeight(), Constants2.F);
        }



        gui.drawSexyRect(x + (width / 2) - 75, y - scrollbar.posY + 4, width / 2, 2, Constants.defaultTextColor);
        // slider -> ISelectable



        fontRenderer.drawString(setting.getName(), textX, textY - scrollbar.posY, textColor, false);

        final String text = setting.getAsString();

        fontRenderer.drawString(text, (x + width - 40) - fontRenderer.getStringWidth(text) / 2, textY - scrollbar.posY, textColor, false);
    }

    @Override
    public void handleMouseClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        setting.handleMouseClick(minecraft, mouseButton);
    }
}