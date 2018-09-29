package uprizing.settings;

import net.minecraft.client.Minecraft;

public class NumberSetting extends Setting {

	protected int value = 0;
	private final int maximum;

	public NumberSetting(final String name, final int maximum) {
		super(name);
		this.maximum = maximum;
	}

	@Override
	public final void foo(String configValue) {
		value = Integer.parseInt(configValue);
	}

	@Override
	public final String bar() {
		return "" + value;
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		if (mouseButton == 1) {
			value = value == 0 ? maximum : value - 1;
		} else {
			value = value == maximum ? 0 : value + 1;
		}
	}

	@Override
	public final int getAsInt() {
		return value;
	}
}