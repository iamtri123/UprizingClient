package uprizing.draggable.draggables;

import uprizing.Uprizing;
import uprizing.count.Count;
import uprizing.draggable.AbstractDraggable;

public class FPSDraggable extends AbstractDraggable {

	private final Count fpsCount;
	private static final String SUFFIX = " FPS";

	public FPSDraggable(final Uprizing uprizing) {
		super("FPS", 58, 13);
		fpsCount = uprizing.getFpsCount();
	}

	@Override
	public String getText() {
		return fpsCount.get() + SUFFIX;
	}

	@Override
	public int getTextWidth() {
		return 12; // return fpsCount.get() > 9 ? 12 : 15;
	}
}