package uprizing.option;

import net.minecraft.client.Minecraft;

public class BooleanOption extends AbstractOption {

	protected boolean value = false;

	public BooleanOption(final String name) {
		super(name);
	}

	@Override
	public final String getConfigValue() {
		return value ? "1" : "0";
	}

	@Override
	public final void parseValue(String configValue) {
		value = configValue.equals("1");
	}

	@Override
	public final boolean getAsBoolean() {
		return value;
	}

	@Override
	public final String getAsString() {
		return value ? "ON" : "OFF";
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		value = !value;
	}
}