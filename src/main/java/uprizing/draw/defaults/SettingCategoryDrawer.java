package uprizing.draw.defaults;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import uprizing.draw.Drawer;
import uprizing.gui.GuiUprizingMenu;
import uprizing.setting.SettingCategory;
import uprizing.util.Constants;

public class SettingCategoryDrawer implements Drawer {

    private final SettingCategory category;
    private final int x;
    @Getter private int y;
    private final int textX;
    private int textY;
    private final int width;

    public SettingCategoryDrawer(SettingCategory category, int x, int y, int width) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.textX = x + 1;
        this.textY = y + 2;
        this.width = width;
    }

    @Override
    public int getDrawingHeight() {
        return category.getHeight();
    }

    @Override
    public final boolean isHovered(int mouseX, int mouseY, GuiUprizingMenu.Scrollbar scrollbar) {
        return mouseX >= x && mouseY >= y - scrollbar.posY && mouseX < x + width && mouseY < y + category.getHeight() - scrollbar.posY;
    }

    @Override
    public final void draw(GuiUprizingMenu gui, FontRenderer fontRenderer, GuiUprizingMenu.Scrollbar scrollbar, int mouseX, int mouseY) {
        //if (isHovered(mouseX, mouseY)) {
            //gui.drawSexyRect(x, y, width, category.getDrawingHeight(), Constants2.C);
        //}

        fontRenderer.drawString(category.getName(), textX, textY - scrollbar.posY, Constants.categoryTextColor);

        gui.drawHorizontalLine(x, x + width - 1, y + 12 + 1 - 1 - scrollbar.posY, GuiUprizingMenu.sexyColor);
    }

    @Override
    public void handleMouseClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        System.out.println("click " + category.getName() + " category");
    }
}