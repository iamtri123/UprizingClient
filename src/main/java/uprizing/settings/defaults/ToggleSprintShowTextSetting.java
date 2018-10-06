package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.ToggleSprint;
import uprizing.Uprizing;
import uprizing.settings.Setting;

public class ToggleSprintShowTextSetting extends Setting {

	private final ToggleSprint toggleSprint = Uprizing.getInstance().getToggleSprint();

	public ToggleSprintShowTextSetting() {
		super("Show Text", "ToggleSprint - ");
	}

	@Override
	public final void foo(String configValue) {
		toggleSprint.showText = configValue.equals("1");
	}

	@Override
	public final String bar() {
		return toggleSprint.showText ? "1" : "0";
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		toggleSprint.showText = !toggleSprint.showText;
	}

	@Override
	public final String getAsString() {
		return toggleSprint.showText ? "§aYes" : "§cNo";
	}
}