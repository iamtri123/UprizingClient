package uprizing.setting.defaults.togglesprint;

import net.minecraft.client.Minecraft;
import uprizing.ToggleSprint;
import uprizing.Uprizing;
import uprizing.draw.Drawer;
import uprizing.draw.defaults.BooleanSettingDrawer;
import uprizing.setting.Setting;

public class ToggleSprintDoubleTappingSetting extends Setting {

	private final ToggleSprint toggleSprint = Uprizing.getInstance().getToggleSprint();

	public ToggleSprintDoubleTappingSetting() {
		super("Double Tapping", "ToggleSprint - Double Tapping");
	}

	@Override
	public final void deserialize(String configValue) {
		toggleSprint.doubleTapping = configValue.equals("1");
	}

	@Override
	public final String serialize() {
		return toggleSprint.doubleTapping ? "1" : "0";
	}

	@Override
	public Drawer createDrawer(final int x, final int y, final int width) {
		return new BooleanSettingDrawer(this, x, y, width);
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) {
		super.handleMouseClick(minecraft, mouseButton);

		toggleSprint.doubleTapping = !toggleSprint.doubleTapping;
	}

	@Override
	public boolean getAsBoolean() {
		return toggleSprint.doubleTapping;
	}

	@Override
	public final String getAsString() {
		return toggleSprint.doubleTapping ? "ON" : "OFF";
	}
}