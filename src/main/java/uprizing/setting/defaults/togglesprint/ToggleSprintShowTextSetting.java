package uprizing.setting.defaults.togglesprint;

import net.minecraft.client.Minecraft;
import uprizing.ToggleSprint;
import uprizing.Uprizing;
import uprizing.draw.Drawer;
import uprizing.draw.defaults.BooleanSettingDrawer;
import uprizing.setting.Setting;

public class ToggleSprintShowTextSetting extends Setting {

	private final ToggleSprint toggleSprint = Uprizing.getInstance().getToggleSprint();

	public ToggleSprintShowTextSetting() {
		super("Show Text", "ToggleSprint - Show Text");
	}

	@Override
	public final void deserialize(String configValue) {
		toggleSprint.showText = configValue.equals("1");
	}

	@Override
	public final String serialize() {
		return toggleSprint.showText ? "1" : "0";
	}

	@Override
	public Drawer createDrawer(final int x, final int y, final int width) {
		return new BooleanSettingDrawer(this, x, y, width);
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) {
		super.handleMouseClick(minecraft, mouseButton);

		toggleSprint.showText = !toggleSprint.showText;
	}

	@Override
	public boolean getAsBoolean() {
		return toggleSprint.showText;
	}

	@Override
	public final String getAsString() {
		return toggleSprint.showText ? "ON" : "OFF";
	}
}