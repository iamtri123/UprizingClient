package uprizing;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import optifine.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

@Getter
public class Uprizing {

    /** I'm Gay */
    private static final Logger logger = LogManager.getLogger();
    private transient final Minecraft minecraft;
    private final File file;

    /** Options */
    public boolean scoreboardNumbers = false;
    public boolean chatBackground = false;

    public Uprizing(final Minecraft minecraft, final File mainDir) {
        this.minecraft = minecraft;
        this.file = new File(mainDir, "uprizing.txt");
        this.loadOptions();
        Config.initUprizing(this);
    }

    private void loadOptions() {
        try {
            if (!file.exists()) return;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    final String[] args = line.split(":");

                    if (args[0].equals("scoreboardNumbers")) {
                        scoreboardNumbers = args[1].equals("true");
                    } else if (args[0].equals("chatBackground")) {
                        chatBackground = args[1].equals("true");
                    }
                } catch (Exception var91) {
                    logger.warn("Skipping bad option: " + line);
                    var91.printStackTrace();
                }
            }

            reader.close();
        } catch (Exception var101) {
            logger.error("Failed to load options", var101);
        }
    }

    public void saveOptions() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println("scoreboardNumbers:" + scoreboardNumbers);
            writer.println("chatBackground:" + chatBackground);
            writer.close();
        } catch (Exception var71) {
            logger.error("Failed to save options", var71);
        }
    }

    public String getKeyBinding(Options option) {
        final String prefix = option.getName() + ": ";

        if (option == Options.SCOREBOARD_NUMBERS) {
            return scoreboardNumbers ? prefix + "ON" : prefix + "OFF";
        } else if (option == Options.CHAT_BACKGROUND) {
            return chatBackground ? prefix + "ON" : prefix + "OFF";
        } else {
            return prefix;
        }
    }

    public void setOptionValue(Options option) {
        if (option == Options.SCOREBOARD_NUMBERS) {
            scoreboardNumbers = !scoreboardNumbers;
        } else if (option == Options.CHAT_BACKGROUND) {
            chatBackground = !chatBackground;
        }
    }
}