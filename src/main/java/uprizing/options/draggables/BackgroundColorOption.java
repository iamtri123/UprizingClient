package uprizing.options.draggables;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggable.Draggable;
import uprizing.option.AbstractOption;

public class BackgroundColorOption extends AbstractOption {

	private final Draggable draggable;

	public BackgroundColorOption(final int draggableIndex) {
		this(Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public BackgroundColorOption(final Draggable draggable) {
		super("Background Color", draggable.getName());
		this.draggable = draggable;
	}

	@Override
	public final String getConfigValue() {
		return getAsString();
	}

	@Override
	public final void parseValue(String configValue) {
		draggable.setBackgroundColor(Integer.parseInt(configValue));
	}

	@Override
	public final String getAsString() {
		return "" + draggable.getBackgroundColor();
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) { // TODO: only show for the moment
		super.pressButton(minecraft, mouseButton);
	}
}