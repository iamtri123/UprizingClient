package uprizing;

import lombok.Getter;

@Getter
public enum Options {

    SCOREBOARD_NUMBERS("Scoreboard Numbers");

    private final String name;

    Options(final String name) {
        this.name = name;
    }
}