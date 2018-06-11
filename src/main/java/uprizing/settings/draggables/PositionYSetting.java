package uprizing.settings.draggables;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggable.Draggable;
import uprizing.setting.AbstractSetting;

public class PositionYSetting extends AbstractSetting {

	private final Draggable draggable;

	public PositionYSetting(final int draggableIndex) {
		this(Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public PositionYSetting(final Draggable draggable) {
		super("Position Y", draggable.getName());
		this.draggable = draggable;
	}

	@Override
	public final String getConfigValue() {
		return getAsString();
	}

	@Override
	public final void parseValue(String configValue) {
		draggable.setPosY(Integer.parseInt(configValue));
	}

	@Override
	public final String getAsString() {
		return "" + draggable.getPosY();
	}

	@Override
	public void pressButton(Minecraft minecraft) { // TODO: only show for the moment
		super.pressButton(minecraft);
	}
}