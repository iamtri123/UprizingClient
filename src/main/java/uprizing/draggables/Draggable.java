package uprizing.draggables;

import net.minecraft.client.gui.FontRenderer;

public interface Draggable {

	String getName();

	int getPosX();

	void setPosX(int posX);

	int getPosY();

	void setPosY(int posY);

	boolean isHovered(int mouseX, int mouseY);

	void move(int mouseX, int mouseY);

	void draw(FontRenderer fontRenderer);

	void drawSlut(FontRenderer fontRenderer);
}