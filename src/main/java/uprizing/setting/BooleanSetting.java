package uprizing.setting;

import net.minecraft.client.Minecraft;
import uprizing.draw.Drawer;
import uprizing.draw.defaults.BooleanSettingDrawer;

public class  BooleanSetting extends Setting {

	private boolean value;

	public BooleanSetting(final String name, final boolean defaultValue) {
		super(name, name);
		this.value = defaultValue;
	}

	public BooleanSetting(final String name, final String key, final boolean defaultValue) {
		super(name, key);
		this.value = defaultValue;
	}

	@Override
	public final String serialize() {
		return value ? "1" : "0";
	}

	@Override
	public final void deserialize(String configValue) {
		value = configValue.equals("1");
	}

	@Override
	public Drawer createDrawer(int x, int y, int width) {
		return new BooleanSettingDrawer(this, x, y, width);
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) {
		super.handleMouseClick(minecraft, mouseButton);

		value = !value;
	}

	@Override
	public final boolean getAsBoolean() {
		return value;
	}

	@Override
	public final String getAsString() { // TODO: remove
		return value ? "ON" : "OFF";
	}
}