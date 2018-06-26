package uprizing.category.categories;

import uprizing.settings.MotionBlurSetting;
import uprizing.category.Category;
import uprizing.category.CategoryBuilder;
import uprizing.category.SubCategory;
import uprizing.setting.BooleanSetting;
import uprizing.settings.*;
import uprizing.settings.draggables.*;

public class GeneralCategory extends Category {

	public GeneralCategory() {
		super("General");

		addSubCategory(new SubCategory("Spongebob", new CategoryBuilder()
			.add(new BooleanSetting("Chat Background"))
			.add(new GlassRenderingSetting())
			.add(new WorldTimeSetting())
			.add(new DimensionSetting())
			.add(new MotionBlurSetting())));

		addSubCategory(new SubCategory("Scoreboard", new CategoryBuilder()
			.add(new BooleanSetting("Scoreboard Scores"))
			.add(new BooleanSetting("Scoreboard Text Shadow"))));

		addSubCategory(new SubCategory("CPS", new CategoryBuilder()
			.add(new PositionXSetting(0))
			.add(new PositionYSetting(0))
			.add(new ScaleSetting(0))
			.add(new ShowBackgroundSetting(0))
			.add(new TextColorSetting(0))
			.add(new BackgroundColorSetting(0))));

		addSubCategory(new SubCategory("FPS", new CategoryBuilder()
			.add(new PositionXSetting(1))
			.add(new PositionYSetting(1))
			.add(new ScaleSetting(1))
			.add(new ShowBackgroundSetting(1))
			.add(new TextColorSetting(1))
			.add(new BackgroundColorSetting(1))));
	}
}