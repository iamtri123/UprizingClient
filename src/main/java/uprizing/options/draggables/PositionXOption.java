package uprizing.options.draggables;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggable.Draggable;
import uprizing.option.AbstractOption;

public class PositionXOption extends AbstractOption {

	private final Draggable draggable;

	public PositionXOption(final int draggableIndex) {
		this(Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public PositionXOption(final Draggable draggable) {
		super("Position X", draggable.getName());
		this.draggable = draggable;
	}

	@Override
	public final String getConfigValue() {
		return getAsString();
	}

	@Override
	public final void parseValue(String configValue) {
		draggable.setPosX(Integer.parseInt(configValue));
	}

	@Override
	public final String getAsString() {
		return "" + draggable.getPosX();
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) { // TODO: only show for the moment
		super.pressButton(minecraft, mouseButton);
	}
}