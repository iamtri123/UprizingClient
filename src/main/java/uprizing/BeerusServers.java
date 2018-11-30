package uprizing;

public class BeerusServers {

	private final BeerusServer[] elements = new BeerusServer[2];
	private int size;

	public BeerusServers() {
		elements[size++] = new BeerusServer(Constants.ADDRESS);
		elements[size++] = new BeerusServer.Localhost();
	}

	public final BeerusServer[] toArray() {
		return elements;
	}

	public final boolean isBeerusServer(String serverIp) {
		for (int index = 0; index < size; index++)
			if (serverIp.startsWith(elements[index].hostAddress)) {
				return true;
			}
		return false;
	}
}