package uprizing;

public class BeerusServers {

	private final BeerusServer[] elements = new BeerusServer[2];
	private int size;

	public BeerusServers() {
		elements[size++] = new BeerusServer("149.91.80.12");
		elements[size++] = new BeerusServer.Localhost();
	}

	public final BeerusServer[] toArray() {
		return elements;
	}
}