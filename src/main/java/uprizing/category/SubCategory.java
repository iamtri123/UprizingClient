package uprizing.category;

import lombok.Getter;
import lombok.Setter;
import uprizing.setting.Setting;
import uprizing.Merguez;

@Getter @Setter
public class SubCategory {

	private final String name;

	private final transient Setting[] elements;

	private int textX, textY;

	public SubCategory(final String name, final CategoryBuilder builder) {
		this.name = name + Merguez.S;
		this.elements = builder.get();
	}

	public final int size() {
		return elements.length;
	}

	public final Setting get(int index) {
		return elements[index];
	}
}