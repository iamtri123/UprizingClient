package uprizing.setting.defaults;

import net.minecraft.client.Minecraft;
import uprizing.draw.Drawer;
import uprizing.draw.defaults.SliderSettingDrawer;
import uprizing.setting.Setting;

public class WorldTimeSetting extends Setting {

	public static final int VANILLA = 0;

	private final String[] names = { "Vanilla", "§eSunrise", "§bNoon", "§6SunSet", "§9Midnight" };
	private final long[] times = { 0, 6000, 12000, 18000 };

	protected int index = 0;
	private final int maximum = 4;

	public WorldTimeSetting() {
		super("World Time", "World Time"); // == names.length - 1
	}

	public final boolean isNotVanilla() {
		return index != VANILLA;
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
	public Drawer createDrawer(final int x, final int y, final int width) {
		return new SliderSettingDrawer(this, x, y, width);
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) {
		super.handleMouseClick(minecraft, mouseButton);

		if (mouseButton == 1) {
			index = index == 0 ? times.length : index - 1;
		} else {
			index = index == times.length ? 0 : index + 1;
		}
	}

	@Override
	public final String getAsString() {
		return names[index];
	}

	@Override
	public final long getAsLong() {
		return times[index - 1];
	}
}