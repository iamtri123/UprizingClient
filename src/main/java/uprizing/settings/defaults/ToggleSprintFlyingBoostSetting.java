package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.ToggleSprint;
import uprizing.Uprizing;
import uprizing.settings.Setting;

public class ToggleSprintFlyingBoostSetting extends Setting {

	private static final double MAXIMUM = 10.0;

	private final ToggleSprint toggleSprint = Uprizing.getInstance().getToggleSprint();

	public ToggleSprintFlyingBoostSetting() {
		super("Flying Boost", "ToggleSprint - ");
	}

	@Override
	public final void foo(String configValue) {
		toggleSprint.flyingBoost = Double.parseDouble(configValue);
	}

	@Override
	public final String bar() {
		return "" + toggleSprint.flyingBoost;
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		if (mouseButton == 1) {
			toggleSprint.flyingBoost = toggleSprint.flyingBoost == 0 ? MAXIMUM : toggleSprint.flyingBoost - 1;
		} else {
			toggleSprint.flyingBoost = toggleSprint.flyingBoost == MAXIMUM ? 0 : toggleSprint.flyingBoost + 1;
		}
	}

	@Override
	public final String getAsString() {
		return toggleSprint.flyingBoost == 0 ? "§cOff" : "§7" + toggleSprint.flyingBoost;
	}
}