package uprizing.settings.draggables;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggable.Draggable;
import uprizing.setting.AbstractSetting;

public class BackgroundColorSetting extends AbstractSetting {

	private final Draggable draggable;

	public BackgroundColorSetting(final int draggableIndex) {
		this(Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public BackgroundColorSetting(final Draggable draggable) {
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
	public void pressButton(Minecraft minecraft) { // TODO: only show for the moment
		super.pressButton(minecraft);
	}
}