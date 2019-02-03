package uprizing.draw;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import uprizing.gui.GuiUprizingMenu;

public interface Drawer {

    int getDrawingHeight();

    int getY();

    boolean isHovered(int mouseX, int mouseY, GuiUprizingMenu.Scrollbar scrollbar);

    void draw(GuiUprizingMenu gui, FontRenderer fontRenderer, GuiUprizingMenu.Scrollbar scrollbar, int mouseX, int mouseY);

    void handleMouseClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton);
}