package net.minecraft.util;

import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Session {

    private final String username, playerID, token;
    private final Session.Type sessionType;

    public Session(String p_i1098_1_, String p_i1098_2_, String p_i1098_3_) {
        this.username = p_i1098_1_;
        this.playerID = p_i1098_2_;
        this.token = p_i1098_3_;
        this.sessionType = Session.Type.MOJANG;
    }

    public String getSessionID() {
        return "token:" + token + ":" + playerID;
    }

    public GameProfile getProfile() {
        try {
            UUID var1 = UUIDTypeAdapter.fromString(this.getPlayerID());
            return new GameProfile(var1, this.getUsername());
        } catch (IllegalArgumentException var2) {
            return new GameProfile(null, this.getUsername());
        }
    }

    public Session.Type getSessionType() {
        return sessionType;
    }

    public enum Type {

        LEGACY("LEGACY", 0, "legacy"),
        MOJANG("MOJANG", 1, "mojang");

        Type(final String p_i1096_1_, final int p_i1096_2_, final String p_i1096_3_) {}
    }
}