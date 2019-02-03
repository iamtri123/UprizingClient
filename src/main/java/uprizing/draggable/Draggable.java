package uprizing.draggable;

import net.minecraft.client.gui.FontRenderer;
import uprizing.ISelectable;

public interface Draggable extends ISelectable {

	String getName();

	int getPosX();

	void setPosX(int posX);

	int getPosY();

	void setPosY(int posY);

	boolean isHovered(int mouseX, int mouseY);

	void draw(FontRenderer fontRenderer);

	void forceDraw(FontRenderer fontRenderer);
}