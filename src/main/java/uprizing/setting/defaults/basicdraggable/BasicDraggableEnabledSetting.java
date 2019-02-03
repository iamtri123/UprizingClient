package uprizing.setting.defaults.basicdraggable;

import net.minecraft.client.Minecraft;
import uprizing.draggable.BasicDraggable;
import uprizing.draw.Drawer;
import uprizing.draw.defaults.BooleanSettingDrawer;
import uprizing.setting.Setting;

public class BasicDraggableEnabledSetting extends Setting {

	private final BasicDraggable basicDraggable;

	public BasicDraggableEnabledSetting(final BasicDraggable basicDraggable) {
		super("Enabled", basicDraggable.getName() + " Draggable - Enabled");

		this.basicDraggable = basicDraggable;

		super.setCategory(basicDraggable.getSettingCategory());
	}

	@Override
	public final void deserialize(String configValue) {
		basicDraggable.setEnabled(configValue.equals("1"));
	}

	@Override
	public final String serialize() {
		return basicDraggable.isEnabled() ? "1" : "0";
	}

	@Override
	public Drawer createDrawer(final int x, final int y, final int width) {
		return new BooleanSettingDrawer(this, x, y, width);
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) {
		super.handleMouseClick(minecraft, mouseButton);

		basicDraggable.setEnabled(!basicDraggable.isEnabled());
	}

	@Override
	public boolean getAsBoolean() {
		return basicDraggable.isEnabled();
	}

	@Override
	public final String getAsString() {
		return basicDraggable.isEnabled() ? "ON" : "OFF";
	}
}