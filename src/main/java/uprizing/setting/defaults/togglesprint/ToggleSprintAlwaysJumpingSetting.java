package uprizing.setting.defaults.togglesprint;

import net.minecraft.client.Minecraft;
import uprizing.ToggleSprint;
import uprizing.Uprizing;
import uprizing.draw.Drawer;
import uprizing.draw.defaults.BooleanSettingDrawer;
import uprizing.setting.Setting;

public class ToggleSprintAlwaysJumpingSetting extends Setting {

	private final ToggleSprint toggleSprint = Uprizing.getInstance().getToggleSprint();

	public ToggleSprintAlwaysJumpingSetting() {
		super("Always Jumping", "ToggleSprint - Always Jumping");
	}

	@Override
	public final void deserialize(String configValue) {
		toggleSprint.alwaysJumping = configValue.equals("1");
	}

	@Override
	public final String serialize() {
		return toggleSprint.alwaysJumping ? "1" : "0";
	}

	@Override
	public Drawer createDrawer(final int x, final int y, final int width) {
		return new BooleanSettingDrawer(this, x, y, width);
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) {
		super.handleMouseClick(minecraft, mouseButton);

		toggleSprint.alwaysJumping = !toggleSprint.alwaysJumping;
	}

	@Override
	public boolean getAsBoolean() {
		return toggleSprint.alwaysJumping;
	}

	@Override
	public final String getAsString() {
		return toggleSprint.alwaysJumping ? "ON" : "OFF";
	}
}