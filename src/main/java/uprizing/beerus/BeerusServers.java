package uprizing.beerus;

import uprizing.util.Constants;

public class BeerusServers {

	private BeerusServer[] elements = {};
	private int size;

	public BeerusServers() { // TODO: rest-api
		addServer(new BeerusServer(Constants.SERVER_ADDRESS));
		//addServer(new BeerusServer("127.0.0.1"));
		//addServer(new BeerusServer("localhost"));
	}

	public final void addServer(BeerusServer server) {
		final BeerusServer[] result = new BeerusServer[elements.length + 1];
		System.arraycopy(elements, 0, result, 0, elements.length);
		elements = result;
		elements[size++] = server;
	}

	public final BeerusServer[] getAsArray() {
		return elements;
	}

	public final BeerusServer getByServerIp(String serverIp) { // for BeerusServerListEntry
		for (int index = 0; index < size; index++)
			if (serverIp.startsWith(elements[index].hostAddress)) {
				return elements[index];
			}
		return null;
	}
}