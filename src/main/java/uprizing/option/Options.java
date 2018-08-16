package uprizing.option;

import uprizing.settings.DimensionOption;
import uprizing.settings.GlassRenderingOption;
import uprizing.settings.MotionBlurOption;
import uprizing.settings.WorldTimeOption;
import uprizing.settings.draggables.BackgroundColorOption;
import uprizing.settings.draggables.EnabledOption;
import uprizing.settings.draggables.PositionXOption;
import uprizing.settings.draggables.PositionYOption;
import uprizing.settings.draggables.ScaleOption;
import uprizing.settings.draggables.ShowBackgroundOption;
import uprizing.settings.draggables.TextColorOption;

public class Options extends AbstractOptions {

	public static final int CHAT_BACKGROUND = 0, GLASS_RENDERING = 1, WORLD_TIME = 2, DIMENSION = 3, MOTION_BLUR = 4,
		SCOREBOARD_SCORES = 5, SCOREBOARD_TEXT_SHADOW = 6;

	private transient Option[] elements = {};

	private int size;
	
	public Options() {
		addSubCategory(new SubCategory(this, "Spongebob")
			.add(new BooleanOption("Chat Background"))
			.add(new GlassRenderingOption())
			.add(new WorldTimeOption())
			.add(new DimensionOption())
			.add(new MotionBlurOption()));

		addSubCategory(new SubCategory(this, "Scoreboard")
			.add(new BooleanOption("Scoreboard Scores"))
			.add(new BooleanOption("Scoreboard Text Shadow")));

		addSubCategory(new SubCategory(this, "CPS")
			.add(new EnabledOption(0))
			.add(new PositionXOption(0))
			.add(new PositionYOption(0))
			.add(new ScaleOption(0))
			.add(new ShowBackgroundOption(0))
			.add(new TextColorOption(0))
			.add(new BackgroundColorOption(0)));

		addSubCategory(new SubCategory(this, "FPS")
			.add(new EnabledOption(1))
			.add(new PositionXOption(1))
			.add(new PositionYOption(1))
			.add(new ScaleOption(1))
			.add(new ShowBackgroundOption(1))
			.add(new TextColorOption(1))
			.add(new BackgroundColorOption(1)));
	}

	public final int size() {
		return size;
	}

	public final Option get(int index) {
		return elements[index];
	}

	public void add(Option option) {
		elements = growCapacity();
		elements[size++] = option;
	}

	private Option[] growCapacity() {
		final Option[] result = new Option[elements.length + 1];
		System.arraycopy(elements, 0, result, 0, elements.length);
		return result;
	}
}