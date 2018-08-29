package uprizing.options.draggables;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggable.Draggable;
import uprizing.option.AbstractOption;

public class EnabledOption extends AbstractOption {

	private final Draggable draggable;

	public EnabledOption(final int draggableIndex) {
		this(Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public EnabledOption(final Draggable draggable) {
		super("Enable", draggable.getName());
		this.draggable = draggable;
	}

	@Override
	public final String getConfigValue() {
		return draggable.isEnabled() ? "1" : "0";
	}

	@Override
	public final void parseValue(String configValue) {
		draggable.setEnabled(configValue.equals("1"));
	}

	@Override
	public final String getAsString() {
		return draggable.isEnabled() ? "ON" : "OFF";
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		draggable.setEnabled(!draggable.isEnabled());
	}
}