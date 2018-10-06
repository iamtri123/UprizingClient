package uprizing.settings;

import lombok.Getter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import uprizing.Constants;

@Getter
public class SettingButton extends Gui { // TODO: Temporary extending Gui

	private static final int DEFAULT_BUTTON_HEIGHT = 10;

	private final Setting setting;
	private int textX;
	private int textY;
	private int x;
	private int y;
	private int width;
	private int height;

	SettingButton(final Setting setting, final int x, final int y, final int width) {
		this.setting = setting;
		this.textX = x + 4 + 2 + 1;
		this.textY = y + 1;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = DEFAULT_BUTTON_HEIGHT;
	}

	final boolean isHovered(int mouseX, int mouseY) { // TODO: enabled
		return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
	}

	public final void draw(FontRenderer fontRenderer, int mouseX, int mouseY) {
		final boolean hovered = isHovered(mouseX, mouseY);

		if (hovered) {
			drawSexyRect(x, y, width, height, Constants.C);
		}

		drawString(fontRenderer, setting.name() + ": " + setting.getAsString(), textX, textY, Constants.D);
	}
}