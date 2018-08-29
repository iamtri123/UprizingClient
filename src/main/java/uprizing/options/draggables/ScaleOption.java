package uprizing.options.draggables;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggable.Draggable;
import uprizing.option.AbstractOption;

public class ScaleOption extends AbstractOption {

	private final Draggable draggable;

	public ScaleOption(final int draggableIndex) {
		this(Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public ScaleOption(final Draggable draggable) {
		super("Scale", draggable.getName());
		this.draggable = draggable;
	}

	@Override
	public final String getConfigValue() {
		return getAsString();
	}

	@Override
	public final void parseValue(String configValue) {
		draggable.setScale(Double.parseDouble(configValue));
	}

	@Override
	public final String getAsString() {
		return "" + draggable.getScale();
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) { // TODO: only show for the moment
		super.pressButton(minecraft, mouseButton);
	}
}