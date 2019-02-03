package uprizing.setting.defaults.draggable;

import net.minecraft.client.Minecraft;
import uprizing.draggable.Draggable;
import uprizing.setting.Setting;

public class DraggablePosYSetting extends Setting {

	private final Draggable draggable;

	public DraggablePosYSetting(final Draggable draggable) {
		super("Position Y", draggable.getName() + " Draggable - Position Y");
		this.draggable = draggable;
	}

	@Override
	public final void deserialize(String configValue) {
		draggable.setPosY(Integer.parseInt(configValue));
	}

	@Override
	public final String serialize() {
		return getAsString();
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) { // TODO: only show for the moment
		super.handleMouseClick(minecraft, mouseButton);
	}

	@Override
	public final String getAsString() {
		return "" + draggable.getPosY();
	}
}