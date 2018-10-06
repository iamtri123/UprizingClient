package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.TextAndColor;
import uprizing.settings.Setting;

public class TextAndColorTextSetting extends Setting {

	private final TextAndColor textAndColor;

	public TextAndColorTextSetting(final TextAndColor textAndColor) {
		super("Text", "ToggleSprint - " + textAndColor.getName() + " - ");
		this.textAndColor = textAndColor;
	}

	@Override
	public final void foo(String configValue) {
		textAndColor.setKey(configValue);
	}

	@Override
	public final String bar() {
		return getAsString();
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);
	}

	@Override
	public final String getAsString() {
		return textAndColor.getKey();
	}
}