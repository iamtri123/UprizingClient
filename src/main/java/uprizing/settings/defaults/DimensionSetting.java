package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.dimensions.Dimension;
import uprizing.Uprizing;
import uprizing.settings.Setting;

public class DimensionSetting extends Setting { // TODO: Entity.dimension, WorldProvider (dimensionId), WorldProviderEnd, WorldProviderHell

	private final Dimension dimension = Uprizing.getInstance().getDimension();

	public DimensionSetting() {
		super("Dimension");
	}

	@Override
	public final void foo(String configValue) {
		dimension.set(Integer.parseInt(configValue));
	}

	@Override
	public final String bar() {
		return "" + dimension.get();
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		if (mouseButton == 1) {
			dimension.decrement();
		} else {
			dimension.increment();
		}

		minecraft.renderGlobal.loadRenderers();
	}

	@Override
	public final String getAsString() { // Nether = -1, Overworld: 0, End = 1
		return dimension.getDisplayName();
	}
}