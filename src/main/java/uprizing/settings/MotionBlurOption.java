package uprizing.settings;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import uprizing.MotionBlur;
import uprizing.Uprizing;
import uprizing.option.AbstractOption;

@Getter @Setter
public class MotionBlurOption extends AbstractOption {

	private final MotionBlur motionBlur = Uprizing.getInstance().getMotionBlur();

	public MotionBlurOption() {
		super("Motion Blur");
	}

	@Override
	public final String getConfigValue() {
		return "" + motionBlur.index;
	}

	@Override
	public final void parseValue(String configValue) {
		// TODO: Fix this bug on the next updates
	}

	@Override
	public final String getAsString() {
		return motionBlur.index == 0 ? "OFF" : "" + motionBlur.index;
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
}