package uprizing.draggables.defaults;

import uprizing.ClicksPerSecond;
import uprizing.draggables.AbstractDraggable;

public class CPSDraggable extends AbstractDraggable {

	private static final String SUFFIX = " CPS";

	private final ClicksPerSecond clicksPerSecond;

	public CPSDraggable(final ClicksPerSecond clicksPerSecond) {
		super("CPS", 58, 13);
		this.clicksPerSecond = clicksPerSecond;
	}

	@Override
	public String getText() {
		return clicksPerSecond.updateAndGet() + SUFFIX;
	}

	@Override
	public int getTextWidth() {
		return clicksPerSecond.get() > 9 ? 12 : 15;
	}
}