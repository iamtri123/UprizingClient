package uprizing.keybinding;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.resources.I18n;

public class KeyBinding implements Comparable {

    @Getter private final String name;
    @Getter private final int defaultKeyCode;
    @Getter private final String categoryName;
    @Getter @Setter private int keyCode; // TODO: voir si c'est update dans la classe truc

    boolean pressed;
    int presses;

    public KeyBinding(String name, int defaultKeyCode, String categoryName) {
        this.name = name;
        this.keyCode = defaultKeyCode;
        this.defaultKeyCode = defaultKeyCode;
        this.categoryName = categoryName;
    }

    public boolean getIsKeyPressed() {
        return this.pressed;
    }

    public boolean isPressed() {
        if (this.presses == 0) {
            return false;
        } else {
            --this.presses;
            return true;
        }
    }

    public void unpressKey() {
        this.presses = 0;
        this.pressed = false;
    }

    public int compareTo(KeyBinding p_compareTo_1_) {
        int var2 = I18n.format(categoryName).compareTo(I18n.format(p_compareTo_1_.categoryName));

        if (var2 == 0) {
            var2 = I18n.format(name).compareTo(I18n.format(p_compareTo_1_.name));
        }

        return var2;
    }

    public int compareTo(Object p_compareTo_1_) {
        return this.compareTo((KeyBinding) p_compareTo_1_);
    }
}