package uprizing.settings.defaults;

import uprizing.settings.NumberSetting;

public class WorldTimeSetting extends NumberSetting {

	public static final int VANILLA = 0;

	private final String[] names = { "§7Vanilla", "§eSunrise", "§bNoon", "§6SunSet", "§9Midnight" };
	private final long[] times = { 0, 6000, 12000, 18000 };

	public WorldTimeSetting() {
		super("World Time", 4); // == names.length - 1
	}

	@Override
	public final String getAsString() {
		return names[value];
	}

	@Override
	public final long getAsLong() {
		return times[value - 1];
	}
}