package uprizing.settings.defaults;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import uprizing.MotionBlur;
import uprizing.Uprizing;
import uprizing.settings.Setting;

@Getter @Setter
public class MotionBlurSetting extends Setting {

	private final MotionBlur motionBlur = Uprizing.getInstance().getMotionBlur();

	public MotionBlurSetting() {
		super("Motion Blur");
	}

	@Override
	public final void foo(String configValue) {
		// TODO: Fix this bug on the next updates
	}

	@Override
	public final String bar() {
		return "" + motionBlur.index;
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		if (mouseButton == 1) {
			motionBlur.decrement();
		} else {
			motionBlur.increment();
		}
	}

	@Override
	public final String getAsString() {
		// OFF
		return motionBlur.index == 0 ? "§cOff" : "§7" + motionBlur.index;
	}
}