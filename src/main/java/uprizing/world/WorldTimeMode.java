package uprizing.world;

public class WorldTimeMode {

    private int value = 0;
    private final String[] names = { "Vanilla", "Sunrise", "Noon", "SunSet", "Midnight" };
    private final long[] times = { 0, 6000, 12000, 18000 };
    public static final int VANILLA = 0;

    public final int get() {
        return value;
    }

    public final String getName() {
        return names[value];
    }

    public final long getTime() {
        return times[value - 1];
    }

    public final void next() {
        value = value == names.length - 1 ? 0 : value + 1;
    }

    public final void set(int value) {
        this.value = value;
    }
}