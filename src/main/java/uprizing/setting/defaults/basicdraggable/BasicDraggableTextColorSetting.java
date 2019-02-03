package uprizing.setting.defaults.basicdraggable;

import net.minecraft.client.Minecraft;
import uprizing.draggable.BasicDraggable;
import uprizing.setting.Setting;

public class BasicDraggableTextColorSetting extends Setting {

	private final BasicDraggable basicDraggable;

	public BasicDraggableTextColorSetting(final BasicDraggable basicDraggable) {
		super("Text Color", basicDraggable.getName() + " Draggable - Text Color");

		this.basicDraggable = basicDraggable;
	}

	@Override
	public final void deserialize(String configValue) {
		basicDraggable.setTextColor(Integer.valueOf(configValue));
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
		return "" + basicDraggable.getTextColor();
	}
}