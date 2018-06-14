package uprizing;

public class Count {

	private int value = 0;

	public final int get() {
		return value;
	}

	public final void set(int value) {
		this.value = value;
	}

	public final void increment() {
		value++;
	}
}