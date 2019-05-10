package uprizing.keybinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import net.minecraft.util.IntHashMap;
import org.lwjgl.input.Keyboard;

public class KeyBindings {

    private final File file;

    public final KeyBinding openMenu;
    public final KeyBinding addWaypoint;
    public final KeyBinding openWaypoints;
    public final KeyBinding packetListener;
    public final KeyBinding openStaffPanel;
    public final KeyBinding forward;
    public final KeyBinding left;
    public final KeyBinding back;
    public final KeyBinding right;
    public final KeyBinding jump;
    public final KeyBinding sneak;
    public final KeyBinding inventory;
    public final KeyBinding attack;
    public final KeyBinding drop;
    public final KeyBinding useItem;
    public final KeyBinding pickBlock;
    public final KeyBinding sprint;
    public final KeyBinding chat;
    public final KeyBinding playerList;
    public final KeyBinding command;
    public final KeyBinding screenshot;
    public final KeyBinding togglePerspective;
    public final KeyBinding smoothCamera;
    public final KeyBinding fullscreen;
    public final KeyBinding zoom;
    public final KeyBinding[] hotbar = new KeyBinding[9];

    public KeyBindings(final File mainDir) {
        /* -- UprizingClient -- */

        openMenu = addKeyBinding(new KeyBinding("Open Menu", Keyboard.KEY_G, Categories.UPRIZING_CLIENT));
        addWaypoint = addKeyBinding(new KeyBinding("Add Waypoint", Keyboard.KEY_B, Categories.UPRIZING_CLIENT));
        openWaypoints = addKeyBinding(new KeyBinding("Open Waypoints Menu", Keyboard.KEY_M, Categories.UPRIZING_CLIENT));

        /* -- Uprizing -- */

        openStaffPanel = addKeyBinding(new KeyBinding("Open Staff Panel", Keyboard.KEY_H, Categories.UPRIZING_SERVER));

        /* -- Developer -- */

        packetListener = addKeyBinding(new KeyBinding("Packet Listener", Keyboard.KEY_X, Categories.DEVELOPER));

        /* -- Vanilla -- */

        forward = addKeyBinding(new KeyBinding("key.forward", 17, Categories.MOVEMENT));
        back = addKeyBinding(new KeyBinding("key.back", 31, Categories.MOVEMENT));
        left = addKeyBinding(new KeyBinding("key.left", 30, Categories.MOVEMENT));
        right = addKeyBinding(new KeyBinding("key.right", 32, Categories.MOVEMENT));
        jump = addKeyBinding(new KeyBinding("key.jump", 57, Categories.MOVEMENT));
        sneak = addKeyBinding(new KeyBinding("key.sneak", 42, Categories.MOVEMENT));

        inventory = addKeyBinding(new KeyBinding("key.inventory", 18, Categories.INVENTORY));

        for (int index = 0; index < hotbar.length; index++) {
            hotbar[index] = addKeyBinding(new KeyBinding("key.hotbar." + (index + 1), index + 2, Categories.INVENTORY));
        }

        attack = addKeyBinding(new KeyBinding("key.attack", -100, Categories.GAMEPLAY));
        drop = addKeyBinding(new KeyBinding("key.drop", 16, Categories.GAMEPLAY));
        useItem = addKeyBinding(new KeyBinding("key.use", -99, Categories.GAMEPLAY));
        pickBlock = addKeyBinding(new KeyBinding("key.pickItem", -98, Categories.GAMEPLAY));
        sprint = addKeyBinding(new KeyBinding("key.sprint", 29, Categories.GAMEPLAY));

        chat = addKeyBinding(new KeyBinding("key.chat", 20, Categories.MULTIPLAYER));
        playerList = addKeyBinding(new KeyBinding("key.playerlist", 15, Categories.MULTIPLAYER));
        command = addKeyBinding(new KeyBinding("key.command", 53, Categories.MULTIPLAYER));

        screenshot = addKeyBinding(new KeyBinding("key.screenshot", 60, Categories.MISC));
        togglePerspective = addKeyBinding(new KeyBinding("key.togglePerspective", 63, Categories.MISC));
        smoothCamera = addKeyBinding(new KeyBinding("key.smoothCamera", 0, Categories.MISC));
        fullscreen = addKeyBinding(new KeyBinding("key.fullscreen", 87, Categories.MISC));
        zoom = addKeyBinding(new KeyBinding("Zoom", 29, Categories.MISC)); // Optifine

        file = new File(mainDir, "keybindings.txt");

        loadFromFile();
    }

    /* -- Categories -- */

    public class Categories {

        public static final int SIZE = 8;
        static final String MOVEMENT = "key.categories.movement";
        static final String GAMEPLAY = "key.categories.gameplay";
        static final String MULTIPLAYER = "key.categories.multiplayer";
        static final String MISC = "key.categories.misc";
        static final String INVENTORY = "key.categories.inventory";
        static final String UPRIZING_CLIENT = "Uprizing Client";
        static final String UPRIZING_SERVER = "Uprizing Server";
        static final String DEVELOPER = "Developer";
    }

    /* -- System -- */

    private final IntHashMap byKey = new IntHashMap();

    public void setKeyBindState(int keyCode, boolean pressed) { // mouse and keyboard
        if (keyCode != 0) {
            KeyBinding keyBinding = (KeyBinding) byKey.lookup(keyCode);

            if (keyBinding != null) {
                keyBinding.pressed = pressed;
            }
        }
    }

    public void onTick(int keyCode) { // mouse and keyboard
        if (keyCode != 0) {
            KeyBinding keyBinding = (KeyBinding) byKey.lookup(keyCode);

            if (keyBinding != null) {
                keyBinding.presses++;
            }
        }
    }

    public void unPressAllKeys() {
        for (KeyBinding keyBinding : elements) {
            keyBinding.unpressKey();
        }
    }

    public void resetKeyBindingArrayAndHash() {
        byKey.clearMap();

        for (KeyBinding keyBinding : elements) {
            byKey.addKey(keyBinding.getKeyCode(), keyBinding);
        }
    }

    /* -- File -- */

    public void loadFromFile() {
        try {
            if (!file.exists()) return;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    String[] args = line.split(":");

                    int cursor = 0;

                    while (cursor != elements.length) {
                        final KeyBinding keyBinding = elements[cursor++];

                        if (args[0].equals(keyBinding.getName())) {
                            keyBinding.setKeyCode(Integer.parseInt(args[1]));
                        }
                    }
                } catch (Exception exception) {
                    System.out.println("Skipping bad setting: " + line);
                    exception.printStackTrace();
                }
            }

            reader.close();
        } catch (Exception exception) {
            System.out.println("Failed to load settings.");
            exception.printStackTrace();
        }
    }

    public void saveToFile() {
        try {
            final PrintWriter writer = new PrintWriter(new FileWriter(file));
            int cursor = 0;

            while (cursor != elements.length) {
                final KeyBinding keyBinding = elements[cursor++];
                writer.println(keyBinding.getName() + ":" + keyBinding.getKeyCode());
            }

            writer.close();
        } catch (Exception exception) {
            System.out.println("Failed to save options.");
            exception.printStackTrace();
        }
    }

    public void setKeyCodeSave(KeyBinding p_151440_1_, int p_151440_2_) {
        p_151440_1_.setKeyCode(p_151440_2_);
        this.saveToFile();
    }

    /* -- Storage -- */

    private KeyBinding[] elements = {};
    private int size = 0;

    public final int size() {
        return size;
    }

    public final KeyBinding getByIndex(int index) {
        return elements[index];
    }

    public KeyBinding addKeyBinding(KeyBinding keyBinding) {
        final KeyBinding[] result = new KeyBinding[elements.length + 1];
        System.arraycopy(elements, 0, result, 0, elements.length);
        elements = result;
        elements[size++] = keyBinding;
        return keyBinding;
    }
}