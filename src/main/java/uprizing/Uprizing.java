package uprizing;

import java.io.File;
import java.net.InetAddress;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import optifine.Config;
import uprizing.beerus.BeerusServer;
import uprizing.beerus.BeerusServers;
import uprizing.draggable.Draggables;
import uprizing.draggable.defaults.ClicksPerSecond;
import uprizing.draggable.defaults.FramesPerSecond;
import uprizing.draw.Drawers;
import uprizing.gui.GuiUprizingMenu;
import uprizing.gui.waypoint.GuiWaypoint;
import uprizing.gui.waypoint.GuiWaypointsMenu;
import uprizing.setting.Setting;
import uprizing.setting.SettingUtils;
import uprizing.setting.Settings;
import uprizing.setting.defaults.DimensionSetting;
import uprizing.util.Constants;
import uprizing.waypoint.Waypoints;

public class Uprizing {

	@Getter private static Uprizing instance;

	@Getter private final Minecraft minecraft;

	@Getter private final Settings settings;
	private final File settingsFile;

	@Getter private final Draggables draggables = new Draggables();
	@Getter private final ClicksPerSecond clicksPerSecond;
	public final FramesPerSecond framesPerSecond;

	@Getter private final Drawers drawers;
	@Getter private final BeerusServers beerusServers;
	@Getter private final SidebarDrawer sidebarDrawer = new SidebarDrawer(this);

	private ClientProperties defaultProperties = ClientProperties.vanilla();
	@Getter private ClientProperties properties;
	@Getter private String serverHostAddress;
	public BeerusServer currentServer;

	@Getter private final ToggleSprint toggleSprint;
	/*!*/@Getter private final Waypoints waypoints;
	/*!*/@Getter public final DimensionSetting dimension;

	public Uprizing(final Minecraft minecraft, final File mainDir) {
		instance = this;

		this.minecraft = minecraft;

		this.clicksPerSecond = new ClicksPerSecond();
		this.framesPerSecond = new FramesPerSecond();

		this.toggleSprint = new ToggleSprint();

		this.settings = new Settings(this);
		this.settingsFile = new File(mainDir, "settings.txt");
		this.drawers = new Drawers(settings);
		this.beerusServers = new BeerusServers();

		this.properties = defaultProperties;

		SettingUtils.loadFromFile(settingsFile, settings);

		this.waypoints = new Waypoints(minecraft, mainDir);
		this.dimension = getSetting(Settings.DIMENSION);

		Config.initUprizing(this);
	}

	public void runRenderTick() { // TODO: fix tick
		if (minecraft.currentScreen == null) {
			if (minecraft.keyBindings.openMenu.isPressed()) {
				minecraft.displayGuiScreen(new GuiUprizingMenu(this));
			} else if (minecraft.keyBindings.addWaypoint.isPressed()) {
				minecraft.displayGuiScreen(new GuiWaypoint(this));
			} else if (minecraft.keyBindings.openWaypoints.isPressed()) {
				minecraft.displayGuiScreen(new GuiWaypointsMenu(this));
			} else if (!minecraft.gameSettings.showDebugInfo) { // && !minecraft.gameSettings.hideGUI
				draggables.draw(minecraft.fontRenderer);
			}
		}
	}

	public void onWorldLoading() {
		waypoints.handleWorldLoading(minecraft);
	}

	public final int onConnecting(InetAddress address) { // TODO: dans Uprizing
		serverHostAddress = address.getHostAddress();

		for (BeerusServer beerusServer : beerusServers.getAsArray()) {
			if (serverHostAddress.equals(beerusServer.hostAddress)) { // TODO: beerusServer isLocal
				currentServer = beerusServer;
				break;
			}
		}

		int protocol = Constants.NOTCHIAN_PROTOCOL;

		if (currentServer != null) {
			properties = currentServer.getClientProperties();
			protocol = Constants.ALGERIAN_PROTOCOL;
		}

		return protocol;
	}

	public final void reset() {
		serverHostAddress = null;
		properties = defaultProperties;
		currentServer = null;
	}

	public final boolean getBoolean(int index) {
		return settings.getByIndex(index).getAsBoolean();
	}

	public final <T extends Setting> T  getSetting(int index) {
		return (T) settings.getByIndex(index);
	}

	public final void saveSettings() {
		SettingUtils.saveToFile(settingsFile, settings);
	}
}