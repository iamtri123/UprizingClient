package uprizing;

import net.minecraft.util.Session;

public class BeerusServer {

	final String hostAddress;

	BeerusServer(final String hostAddress) {
		this.hostAddress = hostAddress;
	}

	public boolean isAllowed(String serverHostAddress, Session session) {
		return serverHostAddress.equals(hostAddress);
	}

	static final class Localhost extends BeerusServer {

		private final String stawlker = "89a06faf-d916-4aa5-88a9-9fa08b3168f7";

		Localhost() {
			super("127.0.0.1");
		}

		@Override
		public final boolean isAllowed(String serverHostAddress, Session session) {
			return serverHostAddress.equals(hostAddress) && session.getPlayerID().equals(stawlker);
		}
	}
}