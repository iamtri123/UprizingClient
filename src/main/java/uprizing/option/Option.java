package uprizing.option;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public interface Option {

	String getName();

	String getConfigKeyAndValue();

	String getConfigValue();

	String getConfigKey();

	void parseValue(String configValue); // TODO: rename

	boolean getAsBoolean();

	String getAsString();

	long getAsLong();

	int getAsInt();

	boolean isHovered(int mouseX, int mouseY);

	void drawButton(FontRenderer fontRenderer, int mouseX, int mouseY);

	void pressButton(Minecraft minecraft, int mouseButton);

	void updateButton(int buttonX, int buttonY, int buttonWidth);

	void resetButton();
}