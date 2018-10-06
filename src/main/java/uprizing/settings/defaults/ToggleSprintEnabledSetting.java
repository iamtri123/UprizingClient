package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.ToggleSprint;
import uprizing.Uprizing;
import uprizing.settings.Setting;

public class ToggleSprintEnabledSetting extends Setting {

	private final ToggleSprint toggleSprint = Uprizing.getInstance().getToggleSprint();

	public ToggleSprintEnabledSetting() {
		super("Enabled", "ToggleSprint - ");
	}

	@Override
	public final void foo(String configValue) {
		toggleSprint.enabled = configValue.equals("1");
	}

	@Override
	public final String bar() {
		return toggleSprint.enabled ? "1" : "0";
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		if (toggleSprint.enabled) {
			toggleSprint.disable();
		} else {
			toggleSprint.enable();
		}
	}

	@Override
	public final String getAsString() {
		return toggleSprint.enabled ? "§aYes" : "§cNo";
	}
}