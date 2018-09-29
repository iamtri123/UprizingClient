package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggables.Draggable;
import uprizing.settings.Setting;

public class DraggablePositionXSetting extends Setting {

	private final Draggable draggable;

	public DraggablePositionXSetting(final int draggableIndex) {
		this(Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public DraggablePositionXSetting(final Draggable draggable) {
		super("Position X", draggable.getName() + " Draggable - ");
		this.draggable = draggable;
	}

	@Override
	public final String bar() {
		return getAsString();
	}

	@Override
	public final void foo(String configValue) {
		draggable.setPosX(Integer.parseInt(configValue));
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) { // TODO: only show for the moment
		super.pressButton(minecraft, mouseButton);
	}

	@Override
	public final String getAsString() {
		return "" + draggable.getPosX();
	}
}