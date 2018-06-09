package uprizing.settings;

import uprizing.setting.IntSetting;

public class WorldTimeSetting extends IntSetting {

	public static final int VANILLA = 0;
	private final String[] names = { "Vanilla", "Sunrise", "Noon", "SunSet", "Midnight" };
	private final long[] times = { 0, 6000, 12000, 18000 };

	public WorldTimeSetting() {
		super("World Time", 4); // == names.length - 1
	}

	@Override
	public String getAsString() {
		return names[value];
	}

	@Override
	public long getAsLong() {
		return times[value - 1];
	}
}