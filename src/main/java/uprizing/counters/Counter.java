package uprizing.counters;

public class Counter {

	protected int val = 0;

	public final int get() {
		return val;
	}

	public final void set(int value) {
		this.val = value;
	}

	public final void increment() {
		val++;
	}

	public final void reset() {
		val = 0;
	}
}