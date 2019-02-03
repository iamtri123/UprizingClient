package uprizing.draggable.defaults;

import uprizing.Uprizing;
import uprizing.draggable.BasicDraggable;
import uprizing.setting.SettingCategory;

public class FramesPerSecond extends BasicDraggable {

	private final String suffix = " FPS";

	private int value = 0;
	public int debug = 0;

	public FramesPerSecond() {
		super("FramesPerSecond");

		Uprizing.getInstance().getDraggables().addDraggable(this);
	}

	public final int debug() {
		return debug = value;
	}

	public final void increment() {
		value++;
	}

	public final void reset() {
		value = 0;
	}

	/* -- BasicDraggable -- */

	@Override
	public final SettingCategory getSettingCategory() {
		return SettingCategory.FPS;
	}

	@Override
	public final String getText() {
		return debug + suffix;
	}

	@Override
	public final int getTextWidth() {
		return debug > 9 ? 12 : 15;
	}
}