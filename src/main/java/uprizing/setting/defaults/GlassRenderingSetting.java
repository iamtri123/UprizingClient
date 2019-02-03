package uprizing.setting.defaults;

import net.minecraft.client.Minecraft;
import uprizing.setting.BooleanSetting;

public class GlassRenderingSetting extends BooleanSetting {

	public GlassRenderingSetting() {
		super("Glass Rendering", false);
	}

	@Override
	public final void handleMouseClick(Minecraft minecraft, int mouseButton) {
		super.handleMouseClick(minecraft, mouseButton);

		minecraft.renderGlobal.loadRenderers();
	}
}