package uprizing;

import lombok.Getter;

@Getter
public enum Dimensions {

    NETHER(-1, "Nether"),
    OVERWORLD(0, "OverWorld"),
    THE_END(1, "End");

    private final int value;

    private final String name;

    Dimensions(final int value, final String name) {
        this.value = value;
        this.name = name;
    }

    public final int getId() {
        return ordinal();
    }

    public static Dimensions getByValue(int value) {
        return value == 0 ? OVERWORLD : value == -1 ? NETHER : THE_END;
    }
}