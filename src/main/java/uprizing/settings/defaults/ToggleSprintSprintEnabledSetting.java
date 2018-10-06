package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.ToggleSprint;
import uprizing.Uprizing;
import uprizing.settings.Setting;

public class ToggleSprintSprintEnabledSetting extends Setting {

	private final ToggleSprint toggleSprint = Uprizing.getInstance().getToggleSprint();

	public ToggleSprintSprintEnabledSetting() {
		super("Sprint Enabled", "ToggleSprint - ");
	}

	@Override
	public final void foo(String configValue) {
		toggleSprint.sprintEnabled = configValue.equals("1");
	}

	@Override
	public final String bar() {
		return toggleSprint.sprintEnabled ? "1" : "0";
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		toggleSprint.sprintEnabled = !toggleSprint.sprintEnabled;
	}

	@Override
	public final String getAsString() {
		return toggleSprint.sprintEnabled ? "§aYes" : "§cNo";
	}
}