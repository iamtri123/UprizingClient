package uprizing.draw.defaults;

import java.awt.Color;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import uprizing.gui.GuiUprizingMenu;
import uprizing.setting.Setting;
import uprizing.util.Constants;

public class BooleanSettingDrawer extends SettingDrawer {

    private static final int test = new Color(180, 14, 12, 255).getRGB();
    private static final int test2 = new Color(29, 180, 20, 255).getRGB();

    private final Box[] boxes = new Box[2];

    public BooleanSettingDrawer(final Setting setting, final int x, final int y, final int width) {
        super(setting, x, y, width);

        boxes[0] = new Box(x + width - 70, 10);
        boxes[1] = new Box(x + width - 20, 10);
    }

    @Override
    public final void draw(GuiUprizingMenu gui, FontRenderer fontRenderer, GuiUprizingMenu.Scrollbar scrollbar, int mouseX, int mouseY) {
        int textColor = Constants.defaultTextColor;

        if (isHovered(mouseX, mouseY, scrollbar)) {
            //gui.drawSexyRect(x, y, width, setting.getHeight(), Constants2.C);

            if (mouseX < boxes[0].getX()) {
                textColor = Constants.hoveredTextColor;
            }
        }

        fontRenderer.drawString(setting.getName(), textX, textY - scrollbar.posY, textColor, false);

        final String text = setting.getAsString();

        fontRenderer.drawString(text, (x + width - 40) - fontRenderer.getStringWidth(text) / 2, textY - scrollbar.posY, Constants.defaultTextColor, false);

        for (Box box : boxes) {
            gui.drawSexyRect(box.getX(), y - scrollbar.posY, box.getWidth(), setting.getHeight(), setting.getAsBoolean() ? test2 : test);
        }
    }

    @Override
    public void handleMouseClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        for (Box box : boxes) {
            if (mouseX >= box.getX() && mouseX < box.getX() + box.getWidth()) {
                setting.handleMouseClick(minecraft, mouseButton);
            }
        }
    }

    @Getter
    private class Box {

        private final int x;
        private final int width;

        Box(final int x, final int width) {
            this.x = x;
            this.width = width;
        }
    }
}