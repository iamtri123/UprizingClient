package uprizing.category;

import uprizing.categories.*;

public class Categories {

	private final transient Category[] elements = new Category[3];

	public Categories() {
		elements[0] = new GeneralCategory();
		//elements[1] = new ModsCategory();
		//elements[2] = new ProfilesCategory();
	}

	public final Category getByIndex(int index) {
		return elements[index];
	}
}