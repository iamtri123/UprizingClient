package uprizing.settings;

import net.minecraft.client.Minecraft;
import uprizing.option.BooleanOption;

public class GlassRenderingOption extends BooleanOption {

	public GlassRenderingOption() {
		super("Glass Rendering");
	}

	@Override
	public final void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);
		minecraft.renderGlobal.loadRenderers();
	}
}