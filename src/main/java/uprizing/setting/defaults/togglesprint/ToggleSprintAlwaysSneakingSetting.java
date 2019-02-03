package uprizing.setting.defaults.togglesprint;

import net.minecraft.client.Minecraft;
import uprizing.ToggleSprint;
import uprizing.Uprizing;
import uprizing.draw.Drawer;
import uprizing.draw.defaults.BooleanSettingDrawer;
import uprizing.setting.Setting;

public class ToggleSprintAlwaysSneakingSetting extends Setting {

	private final ToggleSprint toggleSprint = Uprizing.getInstance().getToggleSprint();

	public ToggleSprintAlwaysSneakingSetting() {
		super("Always Sneaking", "ToggleSprint - Always Sneaking");
	}

	@Override
	public final void deserialize(String configValue) {
		toggleSprint.alwaysSneaking = configValue.equals("1");
	}

	@Override
	public final String serialize() {
		return toggleSprint.alwaysSneaking ? "1" : "0";
	}

	@Override
	public Drawer createDrawer(final int x, final int y, final int width) {
		return new BooleanSettingDrawer(this, x, y, width);
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) {
		super.handleMouseClick(minecraft, mouseButton);

		toggleSprint.alwaysSneaking = !toggleSprint.alwaysSneaking;
	}

	@Override
	public boolean getAsBoolean() {
		return toggleSprint.alwaysSneaking;
	}

	@Override
	public final String getAsString() {
		return toggleSprint.alwaysSneaking ? "ON" : "OFF";
	}
}