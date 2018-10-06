package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.ToggleSprint;
import uprizing.Uprizing;
import uprizing.settings.Setting;

public class ToggleSprintAlwaysSprintingSetting extends Setting {

	private final ToggleSprint toggleSprint = Uprizing.getInstance().getToggleSprint();

	public ToggleSprintAlwaysSprintingSetting() {
		super("Always Sprinting", "ToggleSprint - ");
	}

	@Override
	public final void foo(String configValue) {
		toggleSprint.alwaysSprinting = configValue.equals("1");
	}

	@Override
	public final String bar() {
		return toggleSprint.alwaysSprinting ? "1" : "0";
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		toggleSprint.alwaysSprinting = !toggleSprint.alwaysSprinting;
	}

	@Override
	public final String getAsString() {
		return toggleSprint.alwaysSprinting ? "§aYes" : "§cNo";
	}
}