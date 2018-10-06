package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.ToggleSprint;
import uprizing.Uprizing;
import uprizing.settings.Setting;

public class ToggleSprintSneakEnabledSetting extends Setting {

	private final ToggleSprint toggleSprint = Uprizing.getInstance().getToggleSprint();

	public ToggleSprintSneakEnabledSetting() {
		super("Sneak Enabled", "ToggleSprint - ");
	}

	@Override
	public final void foo(String configValue) {
		toggleSprint.sneakEnabled = configValue.equals("1");
	}

	@Override
	public final String bar() {
		return toggleSprint.sneakEnabled ? "1" : "0";
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		toggleSprint.sneakEnabled = !toggleSprint.sneakEnabled;
	}

	@Override
	public final String getAsString() {
		return toggleSprint.sneakEnabled ? "§aYes" : "§cNo";
	}
}