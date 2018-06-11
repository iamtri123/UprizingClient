package uprizing.draggable;

import net.minecraft.client.gui.FontRenderer;

public interface Draggable {

	String getName();

	int getPosX();

	void setPosX(int posX);

	int getPosY();

	void setPosY(int posY);

	double getScale();

	void setScale(double scale);

	boolean isShowBackground();

	void setShowBackground(boolean showBackground);

	int getTextColor();

	void setTextColor(int textColor);

	int getBackgroundColor();

	void setBackgroundColor(int backgroundColor);

	boolean isHovered(int mouseX, int mouseY);

	void move(int mouseX, int mouseY);

	void draw(FontRenderer fontRenderer);
}