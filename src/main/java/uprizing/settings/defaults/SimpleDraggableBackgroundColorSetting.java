package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggables.SimpleDraggable;
import uprizing.settings.Setting;

public class SimpleDraggableBackgroundColorSetting extends Setting {

	private final SimpleDraggable draggable;

	public SimpleDraggableBackgroundColorSetting(final int draggableIndex) {
		this((SimpleDraggable) Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public SimpleDraggableBackgroundColorSetting(final SimpleDraggable draggable) {
		super("Background Color", draggable.getName() + " Draggable - ");
		this.draggable = draggable;
	}

	@Override
	public final void foo(String configValue) {
		draggable.setBackgroundColor(Integer.valueOf(configValue));
	}

	@Override
	public final String bar() {
		return getAsString();
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);
	}

	@Override
	public final String getAsString() {
		return "" + draggable.getBackgroundColor();
	}
}