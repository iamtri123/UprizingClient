package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.TextAndColor;
import uprizing.settings.Setting;

public class TextAndColorColorSetting extends Setting {

	private final TextAndColor textAndColor;

	public TextAndColorColorSetting(final TextAndColor textAndColor) {
		super("Color", "ToggleSprint - " + textAndColor.getName() + " - ");
		this.textAndColor = textAndColor;
	}

	@Override
	public final void foo(String configValue) {
		textAndColor.setValue(Integer.parseInt(configValue));
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
		return "" + textAndColor.getValue();
	}
}