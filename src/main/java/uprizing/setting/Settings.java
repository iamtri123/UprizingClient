package uprizing.setting;

import uprizing.settings.DimensionSetting;
import uprizing.settings.GlassRenderingSetting;
import uprizing.settings.MotionBlurSetting;
import uprizing.settings.WorldTimeSetting;
import uprizing.settings.draggables.BackgroundColorSetting;
import uprizing.settings.draggables.EnabledSetting;
import uprizing.settings.draggables.PositionXSetting;
import uprizing.settings.draggables.PositionYSetting;
import uprizing.settings.draggables.ScaleSetting;
import uprizing.settings.draggables.ShowBackgroundSetting;
import uprizing.settings.draggables.TextColorSetting;

public class Settings extends AbstractSettings {

	public static final int CHAT_BACKGROUND = 0, GLASS_RENDERING = 1, WORLD_TIME = 2, DIMENSION = 3, MOTION_BLUR = 4,
		SCOREBOARD_SCORES = 5, SCOREBOARD_TEXT_SHADOW = 6;

	private transient Setting[] elements = {};

	private int size;
	
	public Settings() {
		addSubCategory(new SubCategory(this, "Spongebob")
			.add(new BooleanSetting("Chat Background"))
			.add(new GlassRenderingSetting())
			.add(new WorldTimeSetting())
			.add(new DimensionSetting())
			.add(new MotionBlurSetting()));

		addSubCategory(new SubCategory(this, "Scoreboard")
			.add(new BooleanSetting("Scoreboard Scores"))
			.add(new BooleanSetting("Scoreboard Text Shadow")));

		addSubCategory(new SubCategory(this, "CPS")
			.add(new EnabledSetting(0))
			.add(new PositionXSetting(0))
			.add(new PositionYSetting(0))
			.add(new ScaleSetting(0))
			.add(new ShowBackgroundSetting(0))
			.add(new TextColorSetting(0))
			.add(new BackgroundColorSetting(0)));

		addSubCategory(new SubCategory(this, "FPS")
			.add(new EnabledSetting(1))
			.add(new PositionXSetting(1))
			.add(new PositionYSetting(1))
			.add(new ScaleSetting(1))
			.add(new ShowBackgroundSetting(1))
			.add(new TextColorSetting(1))
			.add(new BackgroundColorSetting(1)));
	}

	public final int size() {
		return size;
	}

	public final Setting get(int index) {
		return elements[index];
	}

	public void add(Setting setting) {
		elements = growCapacity();
		elements[size++] = setting;
	}

	private Setting[] growCapacity() {
		final Setting[] result = new Setting[elements.length + 1];
		System.arraycopy(elements, 0, result, 0, elements.length);
		return result;
	}
}