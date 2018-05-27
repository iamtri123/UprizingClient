package uprizing;

import lombok.Getter;

@Getter
public enum Options {

    SCOREBOARD_NUMBERS("Scoreboard Numbers"),
    CHAT_BACKGROUND("Chat Background"),
    CLEAR_GLASS("Clear Glass");

    private final String name;

    Options(final String name) {
        this.name = name;
    }
}