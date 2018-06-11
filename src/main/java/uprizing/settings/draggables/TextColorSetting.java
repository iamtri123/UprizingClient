package uprizing.settings.draggables;

import net.minecraft.client.Minecraft;
import uprizing.Uprizing;
import uprizing.draggable.Draggable;
import uprizing.setting.AbstractSetting;

public class TextColorSetting extends AbstractSetting {

	private final Draggable draggable;

	public TextColorSetting(final int draggableIndex) {
		this(Uprizing.getInstance().getDraggables().getByIndex(draggableIndex));
	}

	public TextColorSetting(final Draggable draggable) {
		super("Text Color", draggable.getName());
		this.draggable = draggable;
	}

	@Override
	public final String getConfigValue() {
		return getAsString();
	}

	@Override
	public final void parseValue(String configValue) {
		draggable.setTextColor(Integer.parseInt(configValue));
	}

	@Override
	public final String getAsString() {
		return "" + draggable.getTextColor();
	}

	@Override
	public void pressButton(Minecraft minecraft) { // TODO: only show for the moment
		super.pressButton(minecraft);
	}
}