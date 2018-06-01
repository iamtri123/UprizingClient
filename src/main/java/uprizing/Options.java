package uprizing;

import lombok.Getter;

@Getter
public enum Options {

    SCOREBOARD_NUMBERS("Scoreboard Numbers"),
    SCOREBOARD_SHADOW("Scoreboard Shadow"),
    CHAT_BACKGROUND("Chat Background"),
    CLEAR_GLASS("Clear Glass"),
    WORLD_TIME_MODE("World Time Mode"),
    MOTION_BLUR("Motion Blur");

    private final String name;

    Options(final String name) {
        this.name = name;
    }
}