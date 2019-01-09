package net.minecraft.client.main;

import com.google.common.collect.HashMultimap;
import com.google.gson.Gson;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class Main {

    private static final java.lang.reflect.Type field_152370_a = new ParameterizedType() {

        public java.lang.reflect.Type[] getActualTypeArguments() {
            return new java.lang.reflect.Type[] { String.class, new ParameterizedType() {

                public java.lang.reflect.Type[] getActualTypeArguments() {
                    return new java.lang.reflect.Type[] { String.class };
                }

                public java.lang.reflect.Type getRawType() {
                    return Collection.class;
                }

                public java.lang.reflect.Type getOwnerType() {
                    return null;
                }
            }};
        }

        public java.lang.reflect.Type getRawType() {
            return Map.class;
        }

        public java.lang.reflect.Type getOwnerType() {
            return null;
        }
    };

    public static void main(String[] p_main_0_) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        OptionParser var1 = new OptionParser();
        var1.allowsUnrecognizedOptions();
        var1.accepts("demo");
        var1.accepts("fullscreen");
        ArgumentAcceptingOptionSpec var2 = var1.accepts("server").withRequiredArg();
        ArgumentAcceptingOptionSpec var3 = var1.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(25565);
        ArgumentAcceptingOptionSpec var4 = var1.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."));
        ArgumentAcceptingOptionSpec var5 = var1.accepts("assetsDir").withRequiredArg().ofType(File.class);
        ArgumentAcceptingOptionSpec var6 = var1.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
        ArgumentAcceptingOptionSpec var7 = var1.accepts("proxyHost").withRequiredArg();
        ArgumentAcceptingOptionSpec var8 = var1.accepts("proxyPort").withRequiredArg().defaultsTo("8080").ofType(Integer.class);
        ArgumentAcceptingOptionSpec var9 = var1.accepts("proxyUser").withRequiredArg();
        ArgumentAcceptingOptionSpec var10 = var1.accepts("proxyPass").withRequiredArg();
        ArgumentAcceptingOptionSpec var11 = var1.accepts("username").withRequiredArg(); // TODO: From the launcher
        ArgumentAcceptingOptionSpec var12 = var1.accepts("uuid").withRequiredArg(); // TODO: From the launcher
        ArgumentAcceptingOptionSpec var13 = var1.accepts("accessToken").withRequiredArg().required(); // TODO: From the launcher
        ArgumentAcceptingOptionSpec var14 = var1.accepts("version").withRequiredArg().required();
        ArgumentAcceptingOptionSpec var15 = var1.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(854);
        ArgumentAcceptingOptionSpec var16 = var1.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(480);
        ArgumentAcceptingOptionSpec var17 = var1.accepts("userProperties").withRequiredArg().required();
        ArgumentAcceptingOptionSpec var18 = var1.accepts("assetIndex").withRequiredArg();
        ArgumentAcceptingOptionSpec var19 = var1.accepts("userType").withRequiredArg().defaultsTo("legacy");
        ArgumentAcceptingOptionSpec var44 = var1.accepts("e-mail").withRequiredArg().required(); // TODO: Temporary
        ArgumentAcceptingOptionSpec var45 = var1.accepts("password").withRequiredArg().required(); // TODO: Temporary
        NonOptionArgumentSpec var20 = var1.nonOptions();
        OptionSet var21 = var1.parse(p_main_0_);
        List var22 = var21.valuesOf(var20);
        String var23 = (String)var21.valueOf(var7);
        Proxy var24 = Proxy.NO_PROXY;

        if (var23 != null) {
            try {
                var24 = new Proxy(Type.SOCKS, new InetSocketAddress(var23, (Integer) var21.valueOf(var8)));
            } catch (Exception ignored) {}
        }

        final String var25 = (String) var21.valueOf(var9);
        final String var26 = (String) var21.valueOf(var10);

        if (!var24.equals(Proxy.NO_PROXY) && func_110121_a(var25) && func_110121_a(var26)) {
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(var25, var26.toCharArray());
                }
            });
        }

        int var27 = (Integer) var21.valueOf(var15);
        int var28 = (Integer) var21.valueOf(var16);
        boolean var29 = var21.has("fullscreen");
        boolean var30 = var21.has("demo");
        String var31 = (String) var21.valueOf(var14);
        HashMultimap var32 = HashMultimap.create();
        Iterator var33 = ((Map) (new Gson()).fromJson((String) var21.valueOf(var17), field_152370_a)).entrySet().iterator();

        while (var33.hasNext()) {
            Entry var34 = (Entry) var33.next();
            var32.putAll(var34.getKey(), (Iterable) var34.getValue());
        }

        File var42 = (File) var21.valueOf(var4);
        File var43 = var21.has(var5) ? (File) var21.valueOf(var5) : new File(var42, "assets/");
        File var35 = var21.has(var6) ? (File) var21.valueOf(var6) : new File(var42, "resourcepacks/");
        String var37 = var21.has(var18) ? (String) var18.value(var21) : null;

        // TODO: Temporary
        YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
        UserAuthentication userAuthentication = authenticationService.createUserAuthentication(Agent.MINECRAFT);
        userAuthentication.setUsername((String) var21.valueOf(var44));
        userAuthentication.setPassword((String) var21.valueOf(var45));

        try {
            userAuthentication.logIn();
        } catch (AuthenticationException exception){
            exception.printStackTrace();
        }

        GameProfile profile = userAuthentication.getSelectedProfile();
        Session session = new Session(profile.getName(), profile.getId().toString(), userAuthentication.getAuthenticatedToken());
        // TODO: Temporary

        Minecraft var39 = new Minecraft(session, var27, var28, var29, var30, var42, var43, var35, var24, var31, var37);
        String var40 = (String) var21.valueOf(var2);

        if (var40 != null) {
            var39.setServer(var40, (Integer) var21.valueOf(var3));
        }

        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread") {

            public void run() {
                Minecraft.stopIntegratedServer();
            }
        });

        if (!var22.isEmpty()) {
            System.out.println("Completely ignored arguments: " + var22);
        }

        Thread.currentThread().setName("Client thread");
        var39.run();
    }

    private static boolean func_110121_a(String p_110121_0_) {
        return p_110121_0_ != null && !p_110121_0_.isEmpty();
    }
}