package uprizing.beerus;

public class Beerus { // client-side anticheat provider

	private final BeerusOptions options;

	@Deprecated
	public Beerus() {
		this(new BeerusOptions(30));
	}

	private Beerus(BeerusOptions options) { // given by the server with PacketBeerusOutOptions
		this.options = options;
	}

	private static final int INTERVAL = 20;
	private int delay = INTERVAL, clicks;
	private boolean changed;

	public final void addLeftClick() {
		clicks++;

		if (!changed) {
			changed = true;
		}
	}

	public void tick() {
		if (--delay == 0) {
			delay = INTERVAL;

			if (!changed) return;

			if (clicks >= options.autoclicker) {
				System.out.println(clicks + " CPS >= AUTOCLICKER:[" + options.autoclicker + "] (+" + (clicks - options.autoclicker) + " clicks)");

				// TODO: send PacketBeerusInCheating/PacketPlayInCheating:
				// 	- Int: 			Cheat Identifier 	(Autoclicker: 1)
				//  - Array of X: 	Parameters			(Clicks Per Second: 37)
			}

			clicks = 0;
			changed = false;
		}
	}
}