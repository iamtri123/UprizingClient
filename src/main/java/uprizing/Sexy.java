package uprizing;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Sexy { // store fields when the player join a world (or a server)

    private String serverHostAddress;

    public void reset() {
        serverHostAddress = null;
    }
}