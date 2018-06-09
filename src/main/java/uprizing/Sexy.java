package uprizing;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Sexy {

    private String serverHostAddress;

    public void reset() {
        serverHostAddress = null;
    }
}