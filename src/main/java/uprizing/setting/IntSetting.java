package uprizing.setting;

import net.minecraft.client.Minecraft;

public class IntSetting extends AbstractSetting {

	protected int value = 0;

	private final int maximum;

	public IntSetting(final String name, final int maximum) {
		super(name);
		this.maximum = maximum;
	}

	@Override
	public final String getConfigValue() {
		return "" + value;
	}

	@Override
	public final void parseValue(String configValue) {
		value = Integer.parseInt(configValue);
	}

	@Override
	public final int getAsInt() {
		return value;
	}

	@Override
	public void pressButton(Minecraft minecraft) {
		super.pressButton(minecraft);

		value = value == maximum ? 0 : value + 1;
	}
}