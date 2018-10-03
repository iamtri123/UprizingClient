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
}