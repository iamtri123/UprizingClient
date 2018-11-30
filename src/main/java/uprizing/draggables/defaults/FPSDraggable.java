package uprizing.draggables.defaults;

import lombok.Getter;
import uprizing.counters.FPSCounter;
import uprizing.draggables.SimpleDraggable;

public class FPSDraggable extends SimpleDraggable {

	private static final String SUFFIX = " FPS";

	@Getter private final String sexyText = "9999 FPS";
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
		return counter.debug > 9 ? 12 : 15;
	}
}