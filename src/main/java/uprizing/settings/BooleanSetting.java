package uprizing.settings;

import net.minecraft.client.Minecraft;

public class BooleanSetting extends Setting {

	private boolean value = false;

	public BooleanSetting(final String name) {
		super(name);
	}

	public BooleanSetting(final String name, final boolean defaultValue) {
		super(name);
		this.value = defaultValue;
	}

	@Override
	public final void foo(String configValue) {
		value = configValue.equals("1");
	}

	@Override
	public final String bar() {
		return value ? "1" : "0";
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		value = !value;
	}

	@Override
	public final boolean getAsBoolean() {
		return value;
	}

	@Override
	public final String getAsString() {
		return value ? "§aYes" : "§cNo";
	}
}