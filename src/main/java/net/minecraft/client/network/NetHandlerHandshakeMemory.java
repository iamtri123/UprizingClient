package net.minecraft.client.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.Validate;

public class NetHandlerHandshakeMemory implements INetHandlerHandshakeServer
{
    private final MinecraftServer field_147385_a;
    private final NetworkManager field_147384_b;

    public NetHandlerHandshakeMemory(MinecraftServer p_i45287_1_, NetworkManager p_i45287_2_)
    {
        this.field_147385_a = p_i45287_1_;
        this.field_147384_b = p_i45287_2_;
    }

    /**
     * There are two recognized intentions for initiating a handshake: logging in and acquiring server status. The
     * NetworkManager's protocol will be reconfigured according to the specified intention, although a login-intention
     * must pass a versioncheck or receive a disconnect otherwise
     */
    public void processHandshake(C00Handshake packetIn)
    {
        this.field_147384_b.setConnectionState(packetIn.getRequestedState());
    }

    /**
     * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
     */
    public void onDisconnect(IChatComponent reason) {}

    /**
     * Allows validation of the connection state transition. Parameters: from, to (connection state). Typically throws
     * IllegalStateException or UnsupportedOperationException if validation fails
     */
    public void onConnectionStateTransition(EnumConnectionState oldState, EnumConnectionState newState)
    {
        Validate.validState(newState == EnumConnectionState.LOGIN || newState == EnumConnectionState.STATUS, "Unexpected protocol " + newState);

        switch (NetHandlerHandshakeMemory.SwitchEnumConnectionState.field_151263_a[newState.ordinal()])
        {
            case 1:
                this.field_147384_b.setNetHandler(new NetHandlerLoginServer(this.field_147385_a, this.field_147384_b));
                break;

            case 2:
                throw new UnsupportedOperationException("NYI");

            default:
        }
    }

    /**
     * For scheduled network tasks. Used in NetHandlerPlayServer to send keep-alive packets and in NetHandlerLoginServer
     * for a login-timeout
     */
    public void onNetworkTick() {}

    static final class SwitchEnumConnectionState
    {
        static final int[] field_151263_a = new int[EnumConnectionState.values().length];

        static
        {
            try
            {
                field_151263_a[EnumConnectionState.LOGIN.ordinal()] = 1;
            }
            catch (NoSuchFieldError var2)
            {
            }

            try
            {
                field_151263_a[EnumConnectionState.STATUS.ordinal()] = 2;
            }
            catch (NoSuchFieldError var1)
            {
            }
        }
    }
}