package uprizing.setting.defaults;

import net.minecraft.client.Minecraft;
import uprizing.draw.Drawer;
import uprizing.draw.defaults.SliderSettingDrawer;
import uprizing.setting.Setting;

public class DimensionSetting extends Setting {  // Nether = -1, Overworld: 0, End = 1

	private static final int MINIMUM = 0;
	private static final int MAXIMUM = 2;

	private final String[] names = { "§cNether", "§aOverworld", "§8End" };
	private int index = 1;

	/**
	 * TODO:
	 * - Entity.dimension
	 * - WorldProvider (dimensionId)
	 * - WorldProviderEnd
	 * - WorldProviderHell
	 */

	public DimensionSetting() {
		super("Dimension", "Dimension");
	}

	public final int getId() {
		return index - 1; // Nether = -1, Overworld: 0, End = 1
	}

	public final boolean isNether() {
		return index == 0;
	}

	public final boolean isOverWorld() {
		return index == 1;
	}

	public final boolean isEnd() {
		return index == 2;
	}

	@Override
	public final void deserialize(String configValue) {
		index = Integer.parseInt(configValue);
	}

	@Override
	public final String serialize() {
		return "" + index;
	}

	@Override
	public Drawer createDrawer(int x, int y, int width) {
		return new SliderSettingDrawer(this, x, y, width);
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) {
		super.handleMouseClick(minecraft, mouseButton);

		if (mouseButton == 1) {
			index = index == MAXIMUM ? MINIMUM : index + 1;
		} else {
			index = index == MINIMUM ? MAXIMUM : index - 1;
		}

		minecraft.renderGlobal.loadRenderers();
	}

	@Override
	public final String getAsString() {
		return names[index];
	}
}