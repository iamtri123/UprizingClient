package uprizing.draggables.defaults;

import lombok.Getter;
import uprizing.ClicksPerSecond;
import uprizing.draggables.SimpleDraggable;

public class CPSDraggable extends SimpleDraggable {

	private static final String SUFFIX = " CPS";

	@Getter private final String sexyText = "99 CPS";
	private final ClicksPerSecond clicksPerSecond;

	public CPSDraggable(final ClicksPerSecond clicksPerSecond) {
		super("CPS", 58, 13);
		this.clicksPerSecond = clicksPerSecond;
	}

	@Override
	public final String getText() {
		return clicksPerSecond.updateAndGet() + SUFFIX;
	}

	@Override
	public final int getTextWidth() {
		return clicksPerSecond.get() > 9 ? 12 : 15;
	}
}