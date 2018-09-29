package uprizing;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import optifine.Config;
import org.lwjgl.input.Keyboard;
import uprizing.dimensions.Dimension;
import uprizing.draggables.Draggables;
import uprizing.gui.GuiMenu;
import uprizing.settings.Setting;
import uprizing.settings.SettingUtils;
import uprizing.waypoints.WaypointsMod;

import java.io.File;
import java.net.InetAddress;

@Getter
public class Uprizing {

	@Getter private static Uprizing instance;
	private static final int DEFAULT_PROTOCOL = 5;
	private static final int ALGERIAN_PROTOCOL = -213;

	private final Minecraft minecraft;
	private final File settingsFile;
	private final BeerusServers servers = new BeerusServers();

	private String serverHostAddress;
	public boolean isOnBeerusServer;

	@Getter private final ClicksPerSecond clicksPerSecond = new ClicksPerSecond();
	private WaypointsMod waypointsMod;
	private final Draggables draggables;
	private final UprizingSettings settings;

	public final Dimension dimension = new Dimension();
	private final MotionBlur motionBlur;
	private final SidebarDrawer sidebarDrawer = new SidebarDrawer(this);

	private final KeyBinding openMenuKeyBinding = UprizingUtils.keyBinding("Open Menu", Keyboard.KEY_G, "Uprizing Client");

	public Uprizing(final Minecraft minecraft, final File mainDir) {
		instance = this;

		this.minecraft = minecraft;
		this.settingsFile = new File(mainDir, "uprizing.txt");
		this.draggables = new Draggables(clicksPerSecond, minecraft);
		this.motionBlur = new MotionBlur();
		this.settings = new UprizingSettings();
		SettingUtils.loadFromFile(settingsFile, settings);
		this.waypointsMod = new WaypointsMod(this, mainDir);

		this.initKeyBindings();

		Config.initUprizing(this);
		SettingUtils.saveToFile(settingsFile, settings);
	}

	public final String getServerHostAddress() { // TODO: CurrentServer, ClientProperties class
		return serverHostAddress;
	}

	public final int dance(InetAddress address) { // TODO: dans Uprizing
		serverHostAddress = address.getHostAddress();

		for (BeerusServer server : servers.toArray()) {
			if (server.isAllowed(serverHostAddress, minecraft.session)) {
				isOnBeerusServer = true;
				return ALGERIAN_PROTOCOL;
			}
		}

		return DEFAULT_PROTOCOL;
	}

	public final void reset() {
		serverHostAddress = null;
		isOnBeerusServer = false;
	}

	public final void saveSettings() {
		SettingUtils.saveToFile(settingsFile, settings);
	}

	@Deprecated
	private void initKeyBindings() {
		KeyBinding[] gameSettings = minecraft.gameSettings.keyBindings;
		KeyBinding[] uprizing = waypointsMod.getKeyBindings();

		KeyBinding[] keyBindings = new KeyBinding[gameSettings.length + uprizing.length + 1];
		keyBindings[keyBindings.length - 1] = openMenuKeyBinding;

		System.arraycopy(gameSettings, 0, keyBindings, 0, gameSettings.length);
		System.arraycopy(uprizing, 0, keyBindings, gameSettings.length, uprizing.length);

		minecraft.gameSettings.keyBindings = keyBindings;
	}

	public void runRenderTick() {
		if (minecraft.currentScreen == null) {
			clicksPerSecond.tick();

			if (openMenuKeyBinding.isPressed()) {
				minecraft.displayGuiScreen(new GuiMenu(this));
			} else if (!minecraft.gameSettings.showDebugInfo) { // && !minecraft.gameSettings.hideGUI
				draggables.draw(minecraft.fontRenderer);
			}
		}

		waypointsMod.onRenderTick();
	}

	public final boolean getBoolean(int index) {
		return settings.get(index).getAsBoolean();
	}

	public final Setting getSetting(int index) {
		return settings.get(index);
	}
}