package uprizing.setting.defaults.draggable;

import net.minecraft.client.Minecraft;
import uprizing.draggable.Draggable;
import uprizing.setting.Setting;

public class DraggablePosXSetting extends Setting {

	private final Draggable draggable;

	public DraggablePosXSetting(final Draggable draggable) {
		super("Position X", draggable.getName() + " Draggable - Position X");
		this.draggable = draggable;
	}

	@Override
	public final String serialize() {
		return getAsString();
	}

	@Override
	public final void deserialize(String configValue) {
		draggable.setPosX(Integer.parseInt(configValue));
	}

	@Override
	public void handleMouseClick(Minecraft minecraft, int mouseButton) { // TODO: only show for the moment
		super.handleMouseClick(minecraft, mouseButton);
	}

	@Override
	public final String getAsString() {
		return "" + draggable.getPosX();
	}
}