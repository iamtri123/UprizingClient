package uprizing.util;

import lombok.Getter;

@Getter
public class NameAndValue {

    private final String key;
    private final String value;

    public NameAndValue(String key, float value) {
        this(key, Float.toString(value));
    }

    public NameAndValue(String key, byte value) {
        this(key, Byte.toString(value));
    }

    public NameAndValue(String key, int value) {
        this(key, Integer.toString(value));
    }

    public NameAndValue(String key, double value) {
        this(key, Double.toString(value));
    }

    public NameAndValue(String key, String value) {
        this.key = key;
        this.value = value;
    }
}