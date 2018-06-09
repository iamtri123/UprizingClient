package uprizing.setting;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import uprizing.Merguez;
import uprizing.Stawlker;

@Getter
abstract class AbstractSetting extends Gui implements Setting { // TODO: Temporary extending Gui

	private final String name, configKey;
	private int textX, textY, buttonX, buttonY, buttonWidth, buttonHeight;
	private static final int DEFAULT_BUTTON_HEIGHT = 10;

	AbstractSetting(final String name) {
		this.name = name;
		this.configKey = Stawlker.configKey(name);
	}

	@Override
	public final String getConfigKeyAndValue() {
		return configKey + ":" + getConfigValue();
	}

	@Override
	public boolean getAsBoolean() {
		return false;
	}

	@Override
	public String getAsString() {
		return null;
	}

	@Override
	public long getAsLong() {
		return 0;
	}

	@Override
	public int getAsInt() {
		return 0;
	}

	@Override
	public final boolean isHovered(int mouseX, int mouseY) { // TODO: enabled
		return mouseX >= buttonX && mouseY >= buttonY && mouseX < buttonX + buttonWidth && mouseY < buttonY + buttonHeight;
	}

	public void drawButton(FontRenderer fontRenderer, int mouseX, int mouseY) {
		final boolean hovered = isHovered(mouseX, mouseY);

		if (hovered) {
			drawSexyRect(buttonX, buttonY, buttonWidth, buttonHeight, Merguez.C);
		}

		drawString(fontRenderer, name + ": " + getAsString(), textX, textY, Merguez.D);
	}

	@Override
	public void pressButton(Minecraft minecraft) {
		// TODO: LOGGER - System.out.println("clicked button: " + name);
		//minecraft.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
	}

	@Override
	public final void updateButton(int buttonX, int buttonY, int buttonWidth) {
		this.textX = buttonX + 4 + 4 + 1;
		this.textY = buttonY + 1;
		this.buttonX = buttonX;
		this.buttonY = buttonY;
		this.buttonWidth = buttonWidth;
		this.buttonHeight = DEFAULT_BUTTON_HEIGHT;
	}

	@Override
	public final void resetButton() {
		textX = textY = buttonX = buttonY = buttonWidth = buttonHeight = 0;
	}
}