package uprizing;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import optifine.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import uprizing.category.Categories;
import uprizing.category.Category;
import uprizing.gui.GuiMenu;
import uprizing.mods.ModRepository;
import uprizing.mods.waypoints.WaypointsMod;
import uprizing.setting.Setting;
import uprizing.setting.Settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

@Getter
public class Uprizing {

    /** I'm Gay */
    private static Uprizing instance;
    private static final Logger logger = LogManager.getLogger();
    private transient final Minecraft minecraft;
    private final File file;

    public static Uprizing getInstance() {
    	return instance;
	}

    /** X0 Tour Llif3  */
    private final Categories categories;
    private final Settings settings = new Settings();

	/** Mods */
    private final ModRepository modRepository = new ModRepository();
    private transient WaypointsMod waypointsMod;

    /** Key Bindings */
	private final KeyBinding openMenuKeyBinding = Stawlker.keyBinding("Open Menu", Keyboard.KEY_G, "Uprizing Client");

    public Uprizing(final Minecraft minecraft, final File mainDir) {
		instance = this;
        this.minecraft = minecraft;
        this.file = new File(mainDir, "uprizing.txt");
        this.categories = new Categories();
        this.initMods(mainDir);
        this.initKeyBindings();
		this.loadSettings();
        Config.initUprizing(this);
    }

    private void initMods(File mainDir) {
        waypointsMod = new WaypointsMod(this, mainDir);
        modRepository.addMod(waypointsMod);
    }

    private void initKeyBindings() { // TODO: KeyBindings object in Mod (for mod disabling)
        KeyBinding[] gameSettings = minecraft.gameSettings.keyBindings;
        KeyBinding[] uprizing = modRepository.getKeyBindings();

        KeyBinding[] keyBindings = new KeyBinding[gameSettings.length + uprizing.length + 1];
        keyBindings[keyBindings.length - 1] = openMenuKeyBinding;

        System.arraycopy(gameSettings, 0, keyBindings, 0, gameSettings.length);
        System.arraycopy(uprizing, 0, keyBindings, gameSettings.length, uprizing.length);

        minecraft.gameSettings.keyBindings = keyBindings;
    }

    public void runTick(TickType tickType) {
    	if (minecraft.currentScreen == null && openMenuKeyBinding.isPressed()) {
    		minecraft.displayGuiScreen(new GuiMenu(this));
		}

        while (modRepository.hasNext())
            modRepository.next().runTick(tickType);
        modRepository.close();
    }

    public final Category getDefaultCategory() {
    	return categories.getByIndex(0);
	}

    public final boolean getBoolean(int index) {
    	return settings.get(index).getAsBoolean();
	}

	public final Setting getSetting(int index) {
    	return settings.get(index);
	}

    private void loadSettings() {
        try {
            if (!file.exists()) return;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    final String[] args = line.split(":");
                    if (args[0].equals("openMenuKeyBinding")) { // TODO: keyBindings.txt
                    	openMenuKeyBinding.setKeyCode(Integer.parseInt(args[1]));
					}

                    for (int i = 0; i < settings.size(); i++) {
                    	final Setting setting = settings.get(i);
                    	if (setting.getConfigKey().equals(args[0])) // TODO: ArrayIndexOutOfBoundsException
							setting.parseValue(args[1]);
					}
                } catch (Exception var91) {
                    logger.warn("Skipping bad setting: " + line);
                    var91.printStackTrace();
                }
            }

            reader.close();
        } catch (Exception exception) {
            logger.error("Failed to load settings.", exception);
        }
    }

    public void saveSettings() {
        try {
            final PrintWriter writer = new PrintWriter(new FileWriter(file));
			writer.println("openMenuKeyBinding:" + openMenuKeyBinding.getKeyCode());
			for (int i = 0; i < settings.size(); i++)
				writer.println(settings.get(i).getConfigKeyAndValue());
            writer.close();
        } catch (Exception exception) {
            logger.error("Failed to save settings.", exception);
        }
    }
}