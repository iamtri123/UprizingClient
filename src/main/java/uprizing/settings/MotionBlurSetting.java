package uprizing.settings;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import uprizing.MotionBlur;
import uprizing.Uprizing;
import uprizing.setting.AbstractSetting;

@Getter @Setter
public class MotionBlurSetting extends AbstractSetting {

	private final MotionBlur motionBlur = Uprizing.getInstance().getMotionBlur();

	public MotionBlurSetting() {
		super("Motion Blur");
	}

	@Override
	public final String getConfigValue() {
		return "" + motionBlur.index;
	}

	@Override
	public final void parseValue(String configValue) {
		motionBlur.load(Integer.parseInt(configValue));
	}

	@Override
	public final String getAsString() {
		return motionBlur.index == 0 ? "OFF" : "" + motionBlur.index;
	}

	@Override
	public void pressButton(Minecraft minecraft) {
		super.pressButton(minecraft);

		motionBlur.next();
	}
}