package uprizing.beerus.servers;

import net.minecraft.util.Session;
import uprizing.beerus.BeerusServer;

public class LocalhostServer extends BeerusServer {

	private final String stawlker = "89a06faf-d916-4aa5-88a9-9fa08b3168f7";

	public LocalhostServer() {
		super("127.0.0.1");
	}

	@Override
	public final boolean isAllowed(String serverHostAddress, Session session) {
		return serverHostAddress.equals(hostAddress) && session.getPlayerID().equals(stawlker);
	}
}