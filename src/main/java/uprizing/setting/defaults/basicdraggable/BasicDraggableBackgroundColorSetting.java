package uprizing.setting.defaults.basicdraggable;

import net.minecraft.client.Minecraft;
import uprizing.draggable.BasicDraggable;
import uprizing.setting.Setting;

public class BasicDraggableBackgroundColorSetting extends Setting {

	private final BasicDraggable basicDraggable;

	public BasicDraggableBackgroundColorSetting(final BasicDraggable basicDraggable) {
		super("Background Color", basicDraggable.getName() + " Draggable - Background Color");

		this.basicDraggable = basicDraggable;
	}

	@Override
	public final void deserialize(String configValue) {
		basicDraggable.setBackgroundColor(Integer.valueOf(configValue));
	}

	@Override
	public final String serialize() {
		return getAsString();
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) {
		super.handleMouseClick(minecraft, mouseButton);
	}

	@Override
	public final String getAsString() {
		return "" + basicDraggable.getBackgroundColor();
	}
}