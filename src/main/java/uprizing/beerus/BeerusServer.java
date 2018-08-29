package uprizing.beerus;

import net.minecraft.util.Session;

public class BeerusServer {

	protected final String hostAddress;

	public BeerusServer(final String hostAddress) {
		this.hostAddress = hostAddress;
	}

	public boolean isAllowed(String serverHostAddress, Session session) {
		return serverHostAddress.equals(hostAddress);
	}
}