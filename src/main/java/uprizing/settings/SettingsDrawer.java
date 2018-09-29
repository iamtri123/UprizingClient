package uprizing.settings;

import net.minecraft.client.gui.FontRenderer;
import uprizing.Constants;
import uprizing.UprizingSettings;

public class SettingsDrawer {

	private final UprizingSettings settings;
	private transient SettingButton[] elements = {};
	private int size;

	public SettingsDrawer(final UprizingSettings settings) {
		this.settings = settings;
	}

	public final void createButtons(int width, int height) {
		int count = 0;
		SettingType type = null;

		for (int index = 0; index < settings.count(); index++) {
			final Setting setting = settings.get(index);
			final SettingType other = setting.getType();

			if (other == null) continue;

			if (type != other) {
				type = other;
				type.textX = (width / Constants.F) + 8 + 1;
				type.textY = (height / Constants.F) + Constants.V + (count * 10) + 1 + Constants.G;

				count++;
			}

			final int buttonX = (width / Constants.F) + 8;
			final int buttonY = (height / Constants.F) + Constants.V + (count * 10) + Constants.G;
			final int buttonWidth = width - (buttonX * 2);

			count++;

			final SettingButton[] result = new SettingButton[elements.length + 1];
			System.arraycopy(elements, 0, result, 0, elements.length);
			elements = result;

			elements[size++] = new SettingButton(setting, buttonX, buttonY, buttonWidth);
		}
	}

	public final void draw(FontRenderer fontRenderer, int mouseX, int mouseY) {
		SettingType type = null;

		for (int index = 0; index < size; index++) {
			final SettingButton button = elements[index];
			final SettingType other = button.getSetting().getType();

			if (type != other) {
				type = other;
				fontRenderer.drawStringWithShadow(type.name(), type.textX, type.textY, Constants.E);
			}

			button.draw(fontRenderer, mouseX, mouseY);
		}
	}

	public final Setting getByMouse(int mouseX, int mouseY) {
		for (SettingButton button : elements) {
			if (button.isHovered(mouseX, mouseY)) {
				return button.getSetting();
			}
		}

		return null;
	}

	public final void destroyButtons() {
		elements = new SettingButton[size = 0];
	}
}