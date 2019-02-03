package uprizing.beerus;

import lombok.Getter;

public class BeerusServer {

	public final String hostAddress;
	@Getter private final BeerusClientProperties clientProperties = new BeerusClientProperties(); // TODO: rest-api

	public BeerusServer(final String hostAddress) {
		this.hostAddress = hostAddress;
	}

	@Deprecated
	public final boolean isLocal() {
		return hostAddress.equals("localhost") || hostAddress.equals("127.0.0.1");
	}
}