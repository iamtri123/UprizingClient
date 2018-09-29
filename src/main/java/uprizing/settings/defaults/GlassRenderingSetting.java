package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.settings.BooleanSetting;

public class GlassRenderingSetting extends BooleanSetting {

	public GlassRenderingSetting() {
		super("Glass Rendering");
	}

	@Override
	public final void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		minecraft.renderGlobal.loadRenderers();
	}
}