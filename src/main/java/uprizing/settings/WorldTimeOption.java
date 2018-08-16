package uprizing.settings;

import uprizing.option.IntOption;

public class WorldTimeOption extends IntOption {

	public static final int VANILLA = 0;
	private final String[] names = { "Vanilla", "Sunrise", "Noon", "SunSet", "Midnight" };
	private final long[] times = { 0, 6000, 12000, 18000 };

	public WorldTimeOption() {
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