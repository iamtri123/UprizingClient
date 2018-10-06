package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.ToggleSprint;
import uprizing.Uprizing;
import uprizing.settings.Setting;

public class ToggleSprintJumpEnabledSetting extends Setting {

	private final ToggleSprint toggleSprint = Uprizing.getInstance().getToggleSprint();

	public ToggleSprintJumpEnabledSetting() {
		super("Jump Enabled", "ToggleSprint - ");
	}

	@Override
	public final void foo(String configValue) {
		toggleSprint.jumpEnabled = configValue.equals("1");
	}

	@Override
	public final String bar() {
		return toggleSprint.jumpEnabled ? "1" : "0";
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		toggleSprint.jumpEnabled = !toggleSprint.jumpEnabled;
	}

	@Override
	public final String getAsString() {
		return toggleSprint.jumpEnabled ? "§aYes" : "§cNo";
	}
}