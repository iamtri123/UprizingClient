package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggables.Draggable;
import uprizing.settings.Setting;

public class DraggableShowBackgroundSetting extends Setting {

	private final Draggable draggable;

	public DraggableShowBackgroundSetting(final int draggableIndex) {
		this(Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public DraggableShowBackgroundSetting(final Draggable draggable) {
		super("Show Background", draggable.getName() + " Draggable - ");
		this.draggable = draggable;
	}

	@Override
	public final void foo(String configValue) {
		draggable.setShowBackground(configValue.equals("1"));
	}

	@Override
	public final String bar() {
		return draggable.isShowBackground() ? "1" : "0";
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		draggable.setShowBackground(!draggable.isShowBackground());
	}

	@Override
	public final String getAsString() {
		return draggable.isShowBackground() ? "§aYes" : "§cNo";
	}
}