package uprizing;

import lombok.Getter;
import uprizing.settings.BaseSettings;
import uprizing.settings.BooleanSetting;
import uprizing.settings.SettingType;
import uprizing.settings.SettingsDrawer;
import uprizing.settings.defaults.DimensionSetting;
import uprizing.settings.defaults.DraggableEnabledSetting;
import uprizing.settings.defaults.DraggablePositionXSetting;
import uprizing.settings.defaults.DraggablePositionYSetting;
import uprizing.settings.defaults.DraggableShowBackgroundSetting;
import uprizing.settings.defaults.GlassRenderingSetting;
import uprizing.settings.defaults.MotionBlurSetting;
import uprizing.settings.defaults.WorldTimeSetting;

public class UprizingSettings extends BaseSettings {

	public static final int CHAT_BACKGROUND = 0;
	public static final int GLASS_RENDERING = 1;
	public static final int WORLD_TIME = 2;
	public static final int DIMENSION = 3;
	public static final int MOTION_BLUR = 4;
	public static final int SCOREBOARD_SCORES = 5;
	public static final int SCOREBOARD_TEXT_SHADOW = 6;

	private final SettingType[] types = UprizingUtils.types(4, "Spongebob", "Scoreboard", "CPS", "FPS");
	@Getter private final SettingsDrawer drawer;

	public UprizingSettings() {
		/* Spongebob */

		addSetting(new BooleanSetting("Chat Background").type(types[0]));
		addSetting(new GlassRenderingSetting().type(types[0]));
		addSetting(new WorldTimeSetting().type(types[0]));
		addSetting(new DimensionSetting().type(types[0]));
		addSetting(new MotionBlurSetting().type(types[0]));

		/* Scoreboard */

		addSetting(new BooleanSetting("Scoreboard Scores").type(types[1]));
		addSetting(new BooleanSetting("Scoreboard Text Shadow").type(types[1]));

		/* CPS */

		addSetting(new DraggableEnabledSetting(0).type(types[2]));
		addSetting(new DraggableShowBackgroundSetting(0).type(types[2]));
		addSetting(new DraggablePositionXSetting(0));
		addSetting(new DraggablePositionYSetting(0));

		/* FPS */

		addSetting(new DraggableEnabledSetting(1).type(types[3]));
		addSetting(new DraggableShowBackgroundSetting(1).type(types[3]));
		addSetting(new DraggablePositionXSetting(1));
		addSetting(new DraggablePositionYSetting(1));

		drawer = new SettingsDrawer(this);
	}
}