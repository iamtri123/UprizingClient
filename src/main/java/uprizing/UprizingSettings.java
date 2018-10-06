package uprizing;

import lombok.Getter;
import uprizing.settings.BaseSettings;
import uprizing.settings.BooleanSetting;
import uprizing.settings.SettingType;
import uprizing.settings.SettingsDrawer;
import uprizing.settings.defaults.*;

public class UprizingSettings extends BaseSettings {

	public static final int CHAT_BACKGROUND = 0;
	public static final int GLASS_RENDERING = 1;
	public static final int WORLD_TIME = 2;
	public static final int DIMENSION = 3;
	public static final int MOTION_BLUR = 4;
	public static final int SCOREBOARD_SCORES = 5;
	public static final int SCOREBOARD_TEXT_SHADOW = 6;

	private final SettingType[] types = UprizingUtils.types(5, "Spongebob", "Scoreboard", "CPS", "FPS", "ToggleSprint");
	@Getter private final SettingsDrawer drawer;

	public UprizingSettings(final Uprizing instance) {
		/* Spongebob */

		addSetting(new BooleanSetting("Chat Background").type(types[0]));
		addSetting(new GlassRenderingSetting().type(types[0]));
		addSetting(new WorldTimeSetting().type(types[0]));
		addSetting(new DimensionSetting().type(types[0]));
		addSetting(new MotionBlurSetting().type(types[0]));

		/* Scoreboard */

		addSetting(new BooleanSetting("Scoreboard Scores").type(types[1]));
		addSetting(new BooleanSetting("Scoreboard Text Shadow", true).type(types[1]));

		/* CPS */

		addSetting(new SimpleDraggableEnabledSetting(0).type(types[2]));
		addSetting(new SimpleDraggableTextColorSetting(0));
		addSetting(new SimpleDraggableBackgroundColorSetting(0));
		addSetting(new SimpleDraggableShowBackgroundSetting(0).type(types[2]));
		addSetting(new DraggablePositionXSetting(0));
		addSetting(new DraggablePositionYSetting(0));

		/* FPS */

		addSetting(new SimpleDraggableEnabledSetting(1).type(types[3]));
		addSetting(new SimpleDraggableTextColorSetting(1));
		addSetting(new SimpleDraggableBackgroundColorSetting(1));
		addSetting(new SimpleDraggableShowBackgroundSetting(1).type(types[3]));
		addSetting(new DraggablePositionXSetting(1));
		addSetting(new DraggablePositionYSetting(1));

		/* ToggleSprint */

		addSetting(new ToggleSprintEnabledSetting().type(types[4]));
		addSetting(new ToggleSprintShowTextSetting().type(types[4]));
		addSetting(new DraggablePositionXSetting(2));
		addSetting(new DraggablePositionYSetting(2));
		addSetting(new ToggleSprintSneakEnabledSetting().type(types[4]));
		addSetting(new ToggleSprintJumpEnabledSetting().type(types[4]));
		addSetting(new ToggleSprintSprintEnabledSetting().type(types[4]));
		addSetting(new ToggleSprintAlwaysSprintingSetting().type(types[4]));
		addSetting(new ToggleSprintFlyingBoostSetting().type(types[4]));

		for (TextAndColor textAndColor : instance.getToggleSprint().getTextAndColors()) {
			addSetting(new TextAndColorTextSetting(textAndColor));
			addSetting(new TextAndColorColorSetting(textAndColor));
		}

		drawer = new SettingsDrawer(this);
	}
}