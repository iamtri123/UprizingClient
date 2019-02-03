package uprizing.setting.defaults.basicdraggable;

import net.minecraft.client.Minecraft;
import uprizing.draggable.BasicDraggable;
import uprizing.draw.Drawer;
import uprizing.draw.defaults.BooleanSettingDrawer;
import uprizing.setting.Setting;

public class BasicDraggableShowBackgroundSetting extends Setting {

	private final BasicDraggable basicDraggable;

	public BasicDraggableShowBackgroundSetting(final BasicDraggable basicDraggable) {
		super("Show Background", basicDraggable.getName() + " Draggable - Show Background");

		this.basicDraggable = basicDraggable;

		super.setCategory(basicDraggable.getSettingCategory());
	}

	@Override
	public final void deserialize(String configValue) {
		basicDraggable.setShowBackground(configValue.equals("1"));
	}

	@Override
	public final String serialize() {
		return basicDraggable.isShowBackground() ? "1" : "0";
	}

	@Override
	public Drawer createDrawer(final int x, final int y, final int width) {
		return new BooleanSettingDrawer(this, x, y, width);
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) {
		super.handleMouseClick(minecraft, mouseButton);

		basicDraggable.setShowBackground(!basicDraggable.isShowBackground());
	}

	@Override
	public boolean getAsBoolean() {
		return basicDraggable.isShowBackground();
	}

	@Override
	public final String getAsString() {
		return basicDraggable.isShowBackground() ? "ON" : "OFF";
	}
}