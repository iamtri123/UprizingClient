package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggables.Draggable;
import uprizing.settings.Setting;

public class DraggablePositionYSetting extends Setting {

	private final Draggable draggable;

	public DraggablePositionYSetting(final int draggableIndex) {
		this(Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public DraggablePositionYSetting(final Draggable draggable) {
		super("Position Y", draggable.getName() + " Draggable - ");
		this.draggable = draggable;
	}

	@Override
	public final void foo(String configValue) {
		draggable.setPosY(Integer.parseInt(configValue));
	}

	@Override
	public final String bar() {
		return getAsString();
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) { // TODO: only show for the moment
		super.pressButton(minecraft, mouseButton);
	}

	@Override
	public final String getAsString() {
		return "" + draggable.getPosY();
	}
}