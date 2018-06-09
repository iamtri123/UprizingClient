package uprizing.settings;

import net.minecraft.client.Minecraft;
import uprizing.setting.BooleanSetting;

public class GlassRenderingSetting extends BooleanSetting {

	public GlassRenderingSetting() {
		super("Glass Rendering");
	}

	@Override
	public final void pressButton(Minecraft minecraft) {
		super.pressButton(minecraft);
		minecraft.renderGlobal.loadRenderers();
	}
}