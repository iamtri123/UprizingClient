package uprizing.settings.draggables;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggable.Draggable;
import uprizing.option.AbstractOption;

public class ShowBackgroundOption extends AbstractOption {

	private final Draggable draggable;

	public ShowBackgroundOption(final int draggableIndex) {
		this(Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public ShowBackgroundOption(final Draggable draggable) {
		super("Show Background", draggable.getName());
		this.draggable = draggable;
	}

	@Override
	public final String getConfigValue() {
		return draggable.isShowBackground() ? "1" : "0";
	}

	@Override
	public final void parseValue(String configValue) {
		draggable.setShowBackground(configValue.equals("1"));
	}

	@Override
	public final String getAsString() {
		return draggable.isShowBackground() ? "ON" : "OFF";
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		draggable.setShowBackground(!draggable.isShowBackground());
	}
}