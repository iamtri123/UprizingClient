package uprizing;

public class ClicksPerSecond {

	private static final int SECOND = 20;

	private int elements[] = {};
	private int size;
	private int ticks;

	public final void tick() {
		ticks++;
	}

	public final int get() {
		return size;
	}

	public final int updateAndGet() {
		if (size == 0) {
			if (ticks != 0)
				ticks = 0; // not huge int
			return size;
		}

		final int duration = ticks - SECOND;

		for (int index = 0; index < size; index++) {
			final int leftClick = elements[index];
			if (leftClick < duration) {
				fastRemove(index);
			}
		}

		return size;
	}

	public final void add() {
		growCapacity(size + 1);
		elements[size++] = ticks;
	}

	private void growCapacity(int minCapacity) {
		if (minCapacity - elements.length > 0) {
			final int[] result = new int[minCapacity];
			System.arraycopy(elements, 0, result, 0, elements.length);
			elements = result;
		}
	}

	private void fastRemove(int index) {
		final int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(elements, index+1, elements, index,
					numMoved);
		elements[--size] = 0; // clear to let GC do its work
	}
}