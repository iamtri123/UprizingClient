package uprizing.setting.defaults.togglesprint;

import net.minecraft.client.Minecraft;
import uprizing.ToggleSprint;
import uprizing.Uprizing;
import uprizing.draw.Drawer;
import uprizing.draw.defaults.BooleanSettingDrawer;
import uprizing.setting.Setting;

public class ToggleSprintAlwaysSprintingSetting extends Setting {

	private final ToggleSprint toggleSprint = Uprizing.getInstance().getToggleSprint();

	public ToggleSprintAlwaysSprintingSetting() {
		super("Always Sprinting", "ToggleSprint - Always Sprinting");
	}

	@Override
	public final void deserialize(String configValue) {
		toggleSprint.alwaysSprinting = configValue.equals("1");
	}

	@Override
	public final String serialize() {
		return toggleSprint.alwaysSprinting ? "1" : "0";
	}

	@Override
	public Drawer createDrawer(final int x, final int y, final int width) {
		return new BooleanSettingDrawer(this, x, y, width);
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) {
		super.handleMouseClick(minecraft, mouseButton);

		toggleSprint.alwaysSprinting = !toggleSprint.alwaysSprinting;

		if (!toggleSprint.alwaysSprinting) {
			toggleSprint.wasSprintDisabled = true;
		}
	}

	@Override
	public boolean getAsBoolean() {
		return toggleSprint.alwaysSprinting;
	}

	@Override
	public final String getAsString() {
		return toggleSprint.alwaysSprinting ? "ON" : "OFF";
	}
}