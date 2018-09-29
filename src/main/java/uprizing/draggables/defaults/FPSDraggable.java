package uprizing.draggables.defaults;

import uprizing.counters.FPSCounter;
import uprizing.draggables.AbstractDraggable;

public class FPSDraggable extends AbstractDraggable {

	private static final String SUFFIX = " FPS";

	private final FPSCounter counter;

	public FPSDraggable(final FPSCounter counter) {
		super("FPS", 58, 13);
		this.counter = counter;
	}

	@Override
	public final String getText() {
		return counter.debug + SUFFIX;
	}

	@Override
	public final int getTextWidth() {
		return 12; // return fpsCount.get() > 9 ? 12 : 15;
	}
}