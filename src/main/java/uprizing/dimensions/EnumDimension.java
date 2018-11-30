package uprizing.dimensions;

import lombok.Getter;

@Getter
public enum EnumDimension {

    NETHER(-1, "Nether"),
    OVERWORLD(0, "Overworld"),
    THE_END(1, "End");

    private final int value;
    private final String name;

    EnumDimension(final int value, final String name) {
        this.value = value;
        this.name = name;
    }

    public final int getIndex() {
        return ordinal();
    }

    public static EnumDimension getByValue(final int value) {
        return value == 0 ? OVERWORLD : value == -1 ? NETHER : THE_END;
    }
}