package uprizing;

public class ClientProperties {

    public static ClientProperties vanilla() {
        final ClientProperties properties = new ClientProperties();
        properties.itemAnimation = true;
        return properties;
    }

    public boolean itemAnimation; // TODO: Uprizing.hasItemAnimation()
}