package uprizing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public class UprizingUser {

    private final int id; // unique user identifier on the Uprizing servers
    private final int groupId; // for cape
}