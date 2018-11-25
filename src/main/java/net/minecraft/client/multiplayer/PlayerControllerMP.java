package net.minecraft.client.multiplayer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C11PacketEnchantItem;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public class PlayerControllerMP
{
    /** The Minecraft instance. */
    private final Minecraft mc;
    private final NetHandlerPlayClient netClientHandler;

    /** PosX of the current block being destroyed */
    private int currentBlockX = -1;

    /** PosY of the current block being destroyed */
    private int currentBlockY = -1;

    /** PosZ of the current block being destroyed */
    private int currentblockZ = -1;

    /** The Item currently being used to destroy a block */
    private ItemStack currentItemHittingBlock;

    /** Current block damage (MP) */
    private float curBlockDamageMP;

    /**
     * Tick counter, when it hits 4 it resets back to 0 and plays the step sound
     */
    private float stepSoundTickCounter;

    /**
     * Delays the first damage on the block after the first click on the block
     */
    private int blockHitDelay;

    /** Tells if the player is hitting a block */
    private boolean isHittingBlock;

    /** Current game type for the player */
    private WorldSettings.GameType currentGameType;

    /** Index of the current item held by the player in the inventory hotbar */
    private int currentPlayerItem;
    private static final String __OBFID = "CL_00000881";

    public PlayerControllerMP(Minecraft p_i45062_1_, NetHandlerPlayClient p_i45062_2_)
    {
        this.currentGameType = WorldSettings.GameType.SURVIVAL;
        this.mc = p_i45062_1_;
        this.netClientHandler = p_i45062_2_;
    }

    /**
     * Block dig operation in creative mode (instantly digs the block).
     */
    public static void clickBlockCreative(Minecraft minecraftIn, PlayerControllerMP playerController, int x, int y, int z, int side)
    {
        if (!minecraftIn.theWorld.extinguishFire(minecraftIn.thePlayer, x, y, z, side))
        {
            playerController.onPlayerDestroyBlock(x, y, z, side);
        }
    }

    /**
     * Sets player capabilities depending on current gametype. params: player
     */
    public void setPlayerCapabilities(EntityPlayer p_78748_1_)
    {
        this.currentGameType.configurePlayerCapabilities(p_78748_1_.capabilities);
    }

    /**
     * If modified to return true, the player spins around slowly around (0, 68.5, 0). The GUI is disabled, the view is
     * set to first person, and both chat and menu are disabled. Unless the server is modified to ignore illegal
     * stances, attempting to enter a world at all will result in an immediate kick due to an illegal stance. Appears to
     * be left-over debug, or demo code.
     */
    public boolean enableEverythingIsScrewedUpMode()
    {
        return false;
    }

    /**
     * Sets the game type for the player.
     */
    public void setGameType(WorldSettings.GameType p_78746_1_)
    {
        this.currentGameType = p_78746_1_;
        this.currentGameType.configurePlayerCapabilities(this.mc.thePlayer.capabilities);
    }

    /**
     * Flips the player around. Args: player
     */
    public void flipPlayer(EntityPlayer player)
    {
        player.rotationYaw = -180.0F;
    }

    public boolean shouldDrawHUD()
    {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    /**
     * Called when a player completes the destruction of a block
     */
    public boolean onPlayerDestroyBlock(int x, int y, int z, int side)
    {
        if (this.currentGameType.isAdventure() && !this.mc.thePlayer.isCurrentToolAdventureModeExempt(x, y, z))
        {
            return false;
        }
        else if (this.currentGameType.isCreative() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
        {
            return false;
        }
        else
        {
            WorldClient var5 = this.mc.theWorld;
            Block var6 = var5.getBlock(x, y, z);

            if (var6.getMaterial() == Material.air)
            {
                return false;
            }
            else
            {
                var5.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(var6) + (var5.getBlockMetadata(x, y, z) << 12));
                int var7 = var5.getBlockMetadata(x, y, z);
                boolean var8 = var5.setBlockToAir(x, y, z);

                if (var8)
                {
                    var6.onBlockDestroyedByPlayer(var5, x, y, z, var7);
                }

                this.currentBlockY = -1;

                if (!this.currentGameType.isCreative())
                {
                    ItemStack var9 = this.mc.thePlayer.getCurrentEquippedItem();

                    if (var9 != null)
                    {
                        var9.onBlockDestroyed(var5, var6, x, y, z, this.mc.thePlayer);

                        if (var9.stackSize == 0)
                        {
                            this.mc.thePlayer.destroyCurrentEquippedItem();
                        }
                    }
                }

                return var8;
            }
        }
    }

    /**
     * Called by Minecraft class when the player is hitting a block with an item. Args: x, y, z, side
     */
    public void clickBlock(int x, int y, int z, int side)
    {
        if (!this.currentGameType.isAdventure() || this.mc.thePlayer.isCurrentToolAdventureModeExempt(x, y, z))
        {
            if (this.currentGameType.isCreative())
            {
                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(0, x, y, z, side));
                clickBlockCreative(this.mc, this, x, y, z, side);
                this.blockHitDelay = 5;
            }
            else if (!this.isHittingBlock || !this.sameToolAndBlock(x, y, z))
            {
                if (this.isHittingBlock)
                {
                    this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(1, this.currentBlockX, this.currentBlockY, this.currentblockZ, side));
                }

                this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(0, x, y, z, side));
                Block var5 = this.mc.theWorld.getBlock(x, y, z);
                boolean var6 = var5.getMaterial() != Material.air;

                if (var6 && this.curBlockDamageMP == 0.0F)
                {
                    var5.onBlockClicked(this.mc.theWorld, x, y, z, this.mc.thePlayer);
                }

                if (var6 && var5.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, x, y, z) >= 1.0F)
                {
                    this.onPlayerDestroyBlock(x, y, z, side);
                }
                else
                {
                    this.isHittingBlock = true;
                    this.currentBlockX = x;
                    this.currentBlockY = y;
                    this.currentblockZ = z;
                    this.currentItemHittingBlock = this.mc.thePlayer.getHeldItem();
                    this.curBlockDamageMP = 0.0F;
                    this.stepSoundTickCounter = 0.0F;
                    this.mc.theWorld.destroyBlockInWorldPartially(this.mc.thePlayer.getEntityId(), this.currentBlockX, this.currentBlockY, this.currentblockZ, (int)(this.curBlockDamageMP * 10.0F) - 1);
                }
            }
        }
    }

    /**
     * Resets current block damage and isHittingBlock
     */
    public void resetBlockRemoving()
    {
        if (this.isHittingBlock)
        {
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(1, this.currentBlockX, this.currentBlockY, this.currentblockZ, -1));
        }

        this.isHittingBlock = false;
        this.curBlockDamageMP = 0.0F;
        this.mc.theWorld.destroyBlockInWorldPartially(this.mc.thePlayer.getEntityId(), this.currentBlockX, this.currentBlockY, this.currentblockZ, -1);
    }

    /**
     * Called when a player damages a block and updates damage counters
     */
    public void onPlayerDamageBlock(int x, int y, int z, int side)
    {
        this.syncCurrentPlayItem();

        if (this.blockHitDelay > 0)
        {
            --this.blockHitDelay;
        }
        else if (this.currentGameType.isCreative())
        {
            this.blockHitDelay = 5;
            this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(0, x, y, z, side));
            clickBlockCreative(this.mc, this, x, y, z, side);
        }
        else
        {
            if (this.sameToolAndBlock(x, y, z))
            {
                Block var5 = this.mc.theWorld.getBlock(x, y, z);

                if (var5.getMaterial() == Material.air)
                {
                    this.isHittingBlock = false;
                    return;
                }

                this.curBlockDamageMP += var5.getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.thePlayer.worldObj, x, y, z);

                if (this.stepSoundTickCounter % 4.0F == 0.0F)
                {
                    this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(var5.stepSound.func_150498_e()), (var5.stepSound.func_150497_c() + 1.0F) / 8.0F, var5.stepSound.func_150494_d() * 0.5F, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F));
                }

                ++this.stepSoundTickCounter;

                if (this.curBlockDamageMP >= 1.0F)
                {
                    this.isHittingBlock = false;
                    this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(2, x, y, z, side));
                    this.onPlayerDestroyBlock(x, y, z, side);
                    this.curBlockDamageMP = 0.0F;
                    this.stepSoundTickCounter = 0.0F;
                    this.blockHitDelay = 5;
                }

                this.mc.theWorld.destroyBlockInWorldPartially(this.mc.thePlayer.getEntityId(), this.currentBlockX, this.currentBlockY, this.currentblockZ, (int)(this.curBlockDamageMP * 10.0F) - 1);
            }
            else
            {
                this.clickBlock(x, y, z, side);
            }
        }
    }

    /**
     * player reach distance = 4F
     */
    public float getBlockReachDistance()
    {
        return this.currentGameType.isCreative() ? 5.0F : 4.5F;
    }

    public void updateController()
    {
        this.syncCurrentPlayItem();

        if (this.netClientHandler.getNetworkManager().isChannelOpen())
        {
            this.netClientHandler.getNetworkManager().processReceivedPackets();
        }
        else if (this.netClientHandler.getNetworkManager().getExitMessage() != null)
        {
            this.netClientHandler.getNetworkManager().getNetHandler().onDisconnect(this.netClientHandler.getNetworkManager().getExitMessage());
        }
        else
        {
            this.netClientHandler.getNetworkManager().getNetHandler().onDisconnect(new ChatComponentText("Disconnected from server"));
        }
    }

    private boolean sameToolAndBlock(int x, int y, int z)
    {
        ItemStack var4 = this.mc.thePlayer.getHeldItem();
        boolean var5 = this.currentItemHittingBlock == null && var4 == null;

        if (this.currentItemHittingBlock != null && var4 != null)
        {
            var5 = var4.getItem() == this.currentItemHittingBlock.getItem() && ItemStack.areItemStackTagsEqual(var4, this.currentItemHittingBlock) && (var4.isItemStackDamageable() || var4.getItemDamage() == this.currentItemHittingBlock.getItemDamage());
        }

        return x == this.currentBlockX && y == this.currentBlockY && z == this.currentblockZ && var5;
    }

    /**
     * Syncs the current player item with the server
     */
    private void syncCurrentPlayItem()
    {
        int var1 = this.mc.thePlayer.inventory.currentItem;

        if (var1 != this.currentPlayerItem)
        {
            this.currentPlayerItem = var1;
            this.netClientHandler.addToSendQueue(new C09PacketHeldItemChange(this.currentPlayerItem));
        }
    }

    /**
     * Handles a players right click. Args: player, world, x, y, z, side, hitVec
     */
    public boolean onPlayerRightClick(EntityPlayer player, World worldIn, ItemStack itemStackIn, int x, int y, int z, int side, Vec3 hitVector)
    {
        this.syncCurrentPlayItem();
        float var9 = (float)hitVector.xCoord - (float)x;
        float var10 = (float)hitVector.yCoord - (float)y;
        float var11 = (float)hitVector.zCoord - (float)z;
        boolean var12 = false;

        if ((!player.isSneaking() || player.getHeldItem() == null) && worldIn.getBlock(x, y, z).onBlockActivated(worldIn, x, y, z, player, side, var9, var10, var11))
        {
            var12 = true;
        }

        if (!var12 && itemStackIn != null && itemStackIn.getItem() instanceof ItemBlock)
        {
            ItemBlock var13 = (ItemBlock)itemStackIn.getItem();

            if (!var13.func_150936_a(worldIn, x, y, z, side, player, itemStackIn))
            {
                return false;
            }
        }

        this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(x, y, z, side, player.inventory.getCurrentItem(), var9, var10, var11));

        if (var12)
        {
            return true;
        }
        else if (itemStackIn == null)
        {
            return false;
        }
        else if (this.currentGameType.isCreative())
        {
            int var16 = itemStackIn.getItemDamage();
            int var14 = itemStackIn.stackSize;
            boolean var15 = itemStackIn.tryPlaceItemIntoWorld(player, worldIn, x, y, z, side, var9, var10, var11);
            itemStackIn.setItemDamage(var16);
            itemStackIn.stackSize = var14;
            return var15;
        }
        else
        {
            return itemStackIn.tryPlaceItemIntoWorld(player, worldIn, x, y, z, side, var9, var10, var11);
        }
    }

    /**
     * Notifies the server of things like consuming food, etc...
     */
    public boolean sendUseItem(EntityPlayer player, World worldIn, ItemStack itemStackIn)
    {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C08PacketPlayerBlockPlacement(-1, -1, -1, 255, player.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
        int var4 = itemStackIn.stackSize;
        ItemStack var5 = itemStackIn.useItemRightClick(worldIn, player);

        if (var5 == itemStackIn && (var5 == null || var5.stackSize == var4))
        {
            return false;
        }
        else
        {
            player.inventory.mainInventory[player.inventory.currentItem] = var5;

            if (var5.stackSize == 0)
            {
                player.inventory.mainInventory[player.inventory.currentItem] = null;
            }

            return true;
        }
    }

    public EntityClientPlayerMP createPlayer(World worldIn, StatFileWriter stats)
    {
        return new EntityClientPlayerMP(this.mc, worldIn, this.mc.getSession(), this.netClientHandler, stats);
    }

    /**
     * Attacks an entity
     */
    public void attackEntity(EntityPlayer player, Entity targetEntity)
    {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.ATTACK));
        player.attackTargetEntityWithCurrentItem(targetEntity);
    }

    /**
     * Send packet to server - player is interacting with another entity (left click)
     */
    public boolean interactWithEntitySendPacket(EntityPlayer player, Entity targetEntity)
    {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C02PacketUseEntity(targetEntity, C02PacketUseEntity.Action.INTERACT));
        return player.interactWith(targetEntity);
    }

    public ItemStack windowClick(int windowId, int slotId, int p_78753_3_, int p_78753_4_, EntityPlayer player)
    {
        short var6 = player.openContainer.getNextTransactionID(player.inventory);
        ItemStack var7 = player.openContainer.slotClick(slotId, p_78753_3_, p_78753_4_, player);
        this.netClientHandler.addToSendQueue(new C0EPacketClickWindow(windowId, slotId, p_78753_3_, p_78753_4_, var7, var6));
        return var7;
    }

    /**
     * GuiEnchantment uses this during multiplayer to tell PlayerControllerMP to send a packet indicating the
     * enchantment action the player has taken.
     */
    public void sendEnchantPacket(int p_78756_1_, int p_78756_2_)
    {
        this.netClientHandler.addToSendQueue(new C11PacketEnchantItem(p_78756_1_, p_78756_2_));
    }

    /**
     * Used in PlayerControllerMP to update the server with an ItemStack in a slot.
     */
    public void sendSlotPacket(ItemStack itemStackIn, int slotId)
    {
        if (this.currentGameType.isCreative())
        {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(slotId, itemStackIn));
        }
    }

    /**
     * Sends a Packet107 to the server to drop the item on the ground
     */
    public void sendPacketDropItem(ItemStack itemStackIn)
    {
        if (this.currentGameType.isCreative() && itemStackIn != null)
        {
            this.netClientHandler.addToSendQueue(new C10PacketCreativeInventoryAction(-1, itemStackIn));
        }
    }

    public void onStoppedUsingItem(EntityPlayer player)
    {
        this.syncCurrentPlayItem();
        this.netClientHandler.addToSendQueue(new C07PacketPlayerDigging(5, 0, 0, 0, 255));
        player.stopUsingItem();
    }

    public boolean gameIsSurvivalOrAdventure()
    {
        return this.currentGameType.isSurvivalOrAdventure();
    }

    /**
     * Checks if the player is not creative, used for checking if it should break a block instantly
     */
    public boolean isNotCreative()
    {
        return !this.currentGameType.isCreative();
    }

    /**
     * returns true if player is in creative mode
     */
    public boolean isInCreativeMode()
    {
        return this.currentGameType.isCreative();
    }

    /**
     * true for hitting entities far away.
     */
    public boolean extendedReach()
    {
        return this.currentGameType.isCreative();
    }

    public boolean isRidingHorse()
    {
        return this.mc.thePlayer.isRiding() && this.mc.thePlayer.ridingEntity instanceof EntityHorse;
    }
}