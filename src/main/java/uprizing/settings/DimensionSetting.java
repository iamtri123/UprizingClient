package uprizing.settings;

import net.minecraft.client.Minecraft;
import uprizing.Dimension;
import uprizing.Uprizing;
import uprizing.setting.AbstractSetting;

public class DimensionSetting extends AbstractSetting { // TODO: Entity.dimension, WorldProvider (dimensionId), WorldProviderEnd, WorldProviderHell

	private final Dimension dimension = Uprizing.getInstance().getDimension();

	public DimensionSetting() {
		super("Dimension");
	}

	@Override
	public final String getConfigValue() {
		return "" + dimension.get();
	}

	@Override
	public final void parseValue(String configValue) {
		dimension.set(Integer.parseInt(configValue));
	}

	@Override
	public final String getAsString() { // Nether = -1, Overworld: 0, End = 1
		return dimension.getDisplayName();
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
}