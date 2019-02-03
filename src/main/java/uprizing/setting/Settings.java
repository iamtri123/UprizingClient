package uprizing.setting;

import uprizing.util.TextAndColor;
import uprizing.Uprizing;
import uprizing.setting.defaults.DimensionSetting;
import uprizing.setting.defaults.WorldTimeSetting;
import uprizing.draggable.BasicDraggable;
import uprizing.draggable.Draggable;
import uprizing.setting.defaults.GlassRenderingSetting;
import uprizing.setting.defaults.TextAndColorSetting;
import uprizing.setting.defaults.basicdraggable.BasicDraggableBackgroundColorSetting;
import uprizing.setting.defaults.basicdraggable.BasicDraggableEnabledSetting;
import uprizing.setting.defaults.basicdraggable.BasicDraggableShowBackgroundSetting;
import uprizing.setting.defaults.basicdraggable.BasicDraggableTextColorSetting;
import uprizing.setting.defaults.draggable.DraggablePosXSetting;
import uprizing.setting.defaults.draggable.DraggablePosYSetting;
import uprizing.setting.defaults.togglesprint.ToggleSprintAlwaysSprintingSetting;
import uprizing.setting.defaults.togglesprint.ToggleSprintAlwaysJumpingSetting;
import uprizing.setting.defaults.togglesprint.ToggleSprintShowTextSetting;
import uprizing.setting.defaults.togglesprint.ToggleSprintAlwaysSneakingSetting;
import uprizing.setting.defaults.togglesprint.ToggleSprintDoubleTappingSetting;

public class Settings extends BaseSettings {

	public static int CHAT_BACKGROUND = -1;
	public static int SCOREBOARD_SCORES = -1;
	public static int SCOREBOARD_TEXT_SHADOW = -1;
	public static int GLASS_RENDERING = -1;
	public static int WORLD_TIME = -1;

	public static final int DIMENSION = 2;

	public Settings(final Uprizing instance) {
		/* Spongebob */

		addSetting(new GlassRenderingSetting().setCategory(SettingCategory.GENERAL));
		addSetting(new WorldTimeSetting().setCategory(SettingCategory.GENERAL));
		/*!*/addSetting(new DimensionSetting().setCategory(SettingCategory.GENERAL));

		/* Chat */

		addSetting(new BooleanSetting("Chat Background", false).setCategory(SettingCategory.CHAT));

		/* Scoreboard */

		addSetting(new BooleanSetting("Scoreboard Scores", false).setCategory(SettingCategory.SCOREBOARD));
		addSetting(new BooleanSetting("Scoreboard Text Shadow", true).setCategory(SettingCategory.SCOREBOARD));

		/* Draggables */

		for (Draggable draggable : instance.getDraggables().getAsArray()) {
			addSetting(new DraggablePosXSetting(draggable));
			addSetting(new DraggablePosYSetting(draggable));

			if (draggable instanceof BasicDraggable) {
				final BasicDraggable basicDraggable = (BasicDraggable) draggable;

				addSetting(new BasicDraggableEnabledSetting(basicDraggable));
				addSetting(new BasicDraggableTextColorSetting(basicDraggable)); // TODO: colorPicker
				addSetting(new BasicDraggableShowBackgroundSetting(basicDraggable));
				addSetting(new BasicDraggableBackgroundColorSetting(basicDraggable)); // TODO: colorPicker
			}
		}

		/* ToggleSprint */

		addSetting(new ToggleSprintShowTextSetting().setCategory(SettingCategory.TOGGLESPRINT));
		addSetting(new ToggleSprintAlwaysSneakingSetting().setCategory(SettingCategory.TOGGLESPRINT));
		addSetting(new ToggleSprintAlwaysJumpingSetting().setCategory(SettingCategory.TOGGLESPRINT));
		addSetting(new ToggleSprintDoubleTappingSetting().setCategory(SettingCategory.TOGGLESPRINT));
		addSetting(new ToggleSprintAlwaysSprintingSetting().setCategory(SettingCategory.TOGGLESPRINT));
		//addSetting(new ToggleSprintFlyingBoostSetting().setCategory(SettingCategory.TOGGLESPRINT));

		for (TextAndColor textAndColor : instance.getToggleSprint().getTextAndColors()) {
			addSetting(new TextAndColorSetting(textAndColor));
		}

		initializeStaticFields();
	}

	private void initializeStaticFields() {
		CHAT_BACKGROUND = super.getByName("Chat Background").getIndex();
		SCOREBOARD_SCORES = super.getByName("Scoreboard Scores").getIndex();
		SCOREBOARD_TEXT_SHADOW = super.getByName("Scoreboard Text Shadow").getIndex();
		GLASS_RENDERING = super.getByName("Glass Rendering").getIndex();
		WORLD_TIME = super.getByName("World Time").getIndex();
	}
}