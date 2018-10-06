package uprizing.settings.defaults;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggables.SimpleDraggable;
import uprizing.settings.Setting;

public class SimpleDraggableEnabledSetting extends Setting {

	private final SimpleDraggable draggable;

	public SimpleDraggableEnabledSetting(final int draggableIndex) {
		this((SimpleDraggable) Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public SimpleDraggableEnabledSetting(final SimpleDraggable draggable) {
		super("Enabled", draggable.getName() + " Draggable - ");
		this.draggable = draggable;
	}

	@Override
	public final void foo(String configValue) {
		draggable.setEnabled(configValue.equals("1"));
	}

	@Override
	public final String bar() {
		return draggable.isEnabled() ? "1" : "0";
	}

	@Override
	public void pressButton(Minecraft minecraft, int mouseButton) {
		super.pressButton(minecraft, mouseButton);

		draggable.setEnabled(!draggable.isEnabled());
	}

	@Override
	public final String getAsString() {
		return draggable.isEnabled() ? "§aYes" : "§cNo";
	}
}