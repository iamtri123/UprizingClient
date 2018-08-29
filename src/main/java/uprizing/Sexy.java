package uprizing;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import uprizing.beerus.BeerusServer;
import uprizing.beerus.servers.LocalhostServer;

import java.net.InetAddress;

@Getter
public class Sexy {

	private static final int DEFAULT_PROTOCOL = 5, ALGERIAN_PROTOCOL = -213;

	private final Minecraft minecraft;
	private final BeerusServer[] servers;

    private String serverHostAddress;
    public boolean isOnBeerusServer = false;

    public Sexy(final Minecraft minecraft) {
    	this.minecraft = minecraft;
    	this.servers = loadServers();
    }

    private BeerusServer[] loadServers() { // TODO: Defined by the HTTPS Rest-API
    	return new BeerusServer[] {
			new LocalhostServer(), new BeerusServer("149.91.80.12")
    	};
	}

    public final int dance(InetAddress address) {
    	serverHostAddress = address.getHostAddress();

		for (BeerusServer server : servers) {
			if (server.isAllowed(serverHostAddress, minecraft.session)) {
				isOnBeerusServer = true;
					return ALGERIAN_PROTOCOL;
			}
		}

    	return DEFAULT_PROTOCOL;
	}

    public final void reset() {
        serverHostAddress = null;
        isOnBeerusServer = false;
    }
}