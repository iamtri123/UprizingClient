package uprizing.draggable.draggables;

import uprizing.draggable.AbstractDraggable;

public class CPSDraggable extends AbstractDraggable {

	private static final String SUFFIX = " CPS";
	private static final int SECOND = 20;
	private transient int elements[] = {};
	private int ticks, size;

	public CPSDraggable() {
		super("CPS", 58, 13);
	}

	public void tick() {
		ticks++;
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

	private void updateClicksPerSecond() {
		if (size == 0) {
			if (ticks != 0)
				ticks = 0; // not huge int
			return;
		}

		final int duration = ticks - SECOND;

		for (int index = 0; index < size; index++) {
			final int leftClick = elements[index];
			if (leftClick < duration) {
				fastRemove(index);
			}
		}
	}

	private void fastRemove(int index) {
		final int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(elements, index+1, elements, index,
							 numMoved);
		elements[--size] = 0; // clear to let GC do its work
	}

	@Override
	public String getText() {
		updateClicksPerSecond();
		return size + SUFFIX;
	}

	@Override
	public int getTextWidth() {
		return size > 9 ? 12 : 15;
	}
}