package uprizing;

public class Dimension { // Nether = -1, Overworld: 0, End = 1

	private final String[] names = { "§cNether", "§aOverworld", "§8End" };

	private static final int MINIMUM = 0, MAXIMUM = 2;
	
	private int value;

	public final boolean isNether() {
		return value == 0;
	}

	public final boolean isOverWorld() {
		return value == 1;
	}

	public final boolean isEnd() {
		return value == 2;
	}

	public final int asSexyCopy() {
		return value - 1;
	}
	
	public final int get() {
		return value;
	}
	
	public final void increment() {
		value = value == MAXIMUM ? MINIMUM : value + 1;
	}
	
	public final void decrement() {
		value = value == MINIMUM ? MAXIMUM : value - 1;
	}
	
	public final void set(int value) {
		this.value = value;
	}
	
	public final String getDisplayName() {
		return names[value];
	}
}