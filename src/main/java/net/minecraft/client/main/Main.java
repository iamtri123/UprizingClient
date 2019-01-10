package net.minecraft.client.main;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.io.File;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class Main {

    public static void main(String[] args) {
        args = concat(new String[] { "--assetIndex", "1.7.10" }, args);

        System.setProperty("java.net.preferIPv4Stack", "true");

        final OptionParser optionParser = new OptionParser();
        optionParser.allowsUnrecognizedOptions();
        optionParser.accepts("fullscreen");
        optionParser.accepts("demo"); // TODO: remove Demo core

        final ArgumentAcceptingOptionSpec server = optionParser.accepts("server").withRequiredArg();
        final ArgumentAcceptingOptionSpec port = optionParser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565);

        final ArgumentAcceptingOptionSpec assetsDir = optionParser.accepts("assetsDir").withRequiredArg().ofType(File.class);
        final ArgumentAcceptingOptionSpec resourcePackDir = optionParser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);

        final ArgumentAcceptingOptionSpec email = optionParser.accepts("e-mail").withRequiredArg().required(); // TODO: Temporary
        final ArgumentAcceptingOptionSpec password = optionParser.accepts("password").withRequiredArg().required(); // TODO: Temporary

        final ArgumentAcceptingOptionSpec width = optionParser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854);
        final ArgumentAcceptingOptionSpec height = optionParser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480);

        final OptionSet optionSet = optionParser.parse(args);
        final List ignoredArguments = optionSet.valuesOf(optionParser.nonOptions());

        final boolean fullscreen = optionSet.has("fullscreen");
        final boolean demo = optionSet.has("demo");

        final File gameDir = new File(".");
        final File assetsFolder = optionSet.has(assetsDir) ? (File) optionSet.valueOf(assetsDir) : new File(gameDir, "assets/");
        final File resourcePackFolder = optionSet.has(resourcePackDir) ? (File) optionSet.valueOf(resourcePackDir) : new File(gameDir, "resourcepacks/");

        final Session session = createSession((String) optionSet.valueOf(email), (String) optionSet.valueOf(password));
        final Minecraft minecraft = new Minecraft(session, (Integer) optionSet.valueOf(width), (Integer) optionSet.valueOf(height), fullscreen, demo, gameDir, assetsFolder, resourcePackFolder);

        final String serverAddress = (String) optionSet.valueOf(server);

        if (serverAddress != null) {
            minecraft.setServer(serverAddress, (Integer) optionSet.valueOf(port));
        }

        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread") {
            public void run() {
                Minecraft.stopIntegratedServer();
            }
        });

        if (!ignoredArguments.isEmpty()) {
            System.out.println("Completely ignored arguments: " + ignoredArguments);
        }

        Thread.currentThread().setName("Client thread");
        minecraft.run();
    }

    private static Session createSession(String email, String password) {
        /**
         * TODO:
         * - uuid
         * - username
         * - accessToken
         */

        final YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
        final UserAuthentication userAuthentication = authenticationService.createUserAuthentication(Agent.MINECRAFT);
        userAuthentication.setUsername(email);
        userAuthentication.setPassword(password);

        try {
            userAuthentication.logIn();
        } catch (AuthenticationException exception) {
            exception.printStackTrace();
        }

        final GameProfile profile = userAuthentication.getSelectedProfile();
        return new Session(profile.getName(), profile.getId().toString(), userAuthentication.getAuthenticatedToken());
    }

    private static <T> T[] concat(T[] first, T[] second) {
        final T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}