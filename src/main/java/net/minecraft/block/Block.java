package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.RegistryNamespaced;
import net.minecraft.util.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Block {

    public static final RegistryNamespaced blockRegistry = new RegistryNamespacedDefaultedByKey("air");
    private CreativeTabs displayOnCreativeTab;
    protected String textureName;
    public static final Block.SoundType soundTypeStone = new Block.SoundType("stone", 1.0F, 1.0F);

    /**
     * the wood sound type
     */
    public static final Block.SoundType soundTypeWood = new Block.SoundType("wood", 1.0F, 1.0F);

    /**
     * the gravel sound type
     */
    public static final Block.SoundType soundTypeGravel = new Block.SoundType("gravel", 1.0F, 1.0F);
    public static final Block.SoundType soundTypeGrass = new Block.SoundType("grass", 1.0F, 1.0F);

    /**
     * The piston step sound
     */
    public static final Block.SoundType soundTypePiston = new Block.SoundType("stone", 1.0F, 1.0F);

    /**
     * The metal sound type
     */
    public static final Block.SoundType soundTypeMetal = new Block.SoundType("stone", 1.0F, 1.5F);

    /**
     * Glass footsteps
     */
    public static final Block.SoundType soundTypeGlass = new Block.SoundType("stone", 1.0F, 1.0F) {
        public String func_150495_a() {
            return "dig.glass";
        }

        public String func_150496_b() {
            return "step.stone";
        }
    };

    /**
     * Sound for cloth and carpets
     */
    public static final Block.SoundType soundTypeCloth = new Block.SoundType("cloth", 1.0F, 1.0F);
    public static final Block.SoundType field_149776_m = new Block.SoundType("sand", 1.0F, 1.0F);
    public static final Block.SoundType soundTypeSnow = new Block.SoundType("snow", 1.0F, 1.0F);

    /**
     * The ladder sound type
     */
    public static final Block.SoundType soundTypeLadder = new Block.SoundType("ladder", 1.0F, 1.0F) {

        public String func_150495_a() {
            return "dig.wood";
        }
    };

    /**
     * The anvil sound type
     */
    public static final Block.SoundType soundTypeAnvil = new Block.SoundType("anvil", 0.3F, 1.0F) {

        public String func_150495_a() {
            return "dig.stone";
        }

        public String func_150496_b() {
            return "random.anvil_land";
        }
    };

    protected boolean opaque;

    /**
     * How much light is subtracted for going through this block
     */
    protected int lightOpacity;
    protected boolean canBlockGrass;

    /**
     * Amount of light emitted
     */
    protected int lightValue;
    protected boolean field_149783_u;

    /**
     * Indicates how many hits it takes to break a block.
     */
    protected float blockHardness;
    protected float blockResistance;
    protected boolean blockConstructorCalled = true;
    protected boolean enableStats = true;

    /**
     * Flags whether or not this block is of a type that needs random ticking. Ref-counted by ExtendedBlockStorage in
     * order to broadly cull a chunk from the random chunk update list for efficiency's sake.
     */
    protected boolean needsRandomTick;

    /**
     * true if the Block contains a Tile Entity
     */
    protected boolean isBlockContainer;
    protected double field_149759_B;
    protected double field_149760_C;
    protected double field_149754_D;
    protected double field_149755_E;
    protected double field_149756_F;
    protected double maxZ;

    /**
     * Sound of stepping on the block
     */
    public Block.SoundType stepSound;
    public float blockParticleGravity;
    protected final Material blockMaterial;

    /**
     * Determines how much velocity is maintained while moving on top of this block
     */
    public float slipperiness;
    private String unlocalizedNameBlock;
    protected IIcon blockIcon;

    public static int getIdFromBlock(Block blockIn) {
        return blockRegistry.getIDForObject(blockIn);
    }

    public static Block getBlockById(int id) {
        return (Block) blockRegistry.getObjectForID(id);
    }

    public static Block getBlockFromItem(Item itemIn) {
        return getBlockById(Item.getIdFromItem(itemIn));
    }

    public static Block getBlockFromName(String name) {
        if (blockRegistry.containsKey(name)) {
            return (Block) blockRegistry.getObject(name);
        } else {
            try {
                return (Block) blockRegistry.getObjectForID(Integer.parseInt(name));
            } catch (NumberFormatException var2) {
                return null;
            }
        }
    }

    public boolean func_149730_j() {
        return this.opaque;
    }

    public int getLightOpacity() {
        return this.lightOpacity;
    }

    public boolean getCanBlockGrass() {
        return this.canBlockGrass;
    }

    public int getLightValue() {
        return this.lightValue;
    }

    public boolean func_149710_n() {
        return this.field_149783_u;
    }

    public Material getMaterial() {
        return this.blockMaterial;
    }

    public MapColor getMapColor(int meta) {
        return this.getMaterial().getMaterialMapColor();
    }

    public static void registerBlocks() {
        blockRegistry.addObject(0, "air", (new BlockAir()).setBlockName("air"));
        blockRegistry.addObject(1, "stone", (new BlockStone()).setHardness(1.5F).setResistance(10.0F).setStepSound(soundTypePiston).setBlockName("stone").setBlockTextureName("stone"));
        blockRegistry.addObject(2, "grass", (new BlockGrass()).setHardness(0.6F).setStepSound(soundTypeGrass).setBlockName("grass").setBlockTextureName("grass"));
        blockRegistry.addObject(3, "dirt", (new BlockDirt()).setHardness(0.5F).setStepSound(soundTypeGravel).setBlockName("dirt").setBlockTextureName("dirt"));
        Block var0 = (new Block(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setBlockName("stonebrick").setCreativeTab(CreativeTabs.tabBlock).setBlockTextureName("cobblestone");
        blockRegistry.addObject(4, "cobblestone", var0);
        Block var1 = (new BlockWood()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setBlockName("wood").setBlockTextureName("planks");
        blockRegistry.addObject(5, "planks", var1);
        blockRegistry.addObject(6, "sapling", (new BlockSapling()).setHardness(0.0F).setStepSound(soundTypeGrass).setBlockName("sapling").setBlockTextureName("sapling"));
        blockRegistry.addObject(7, "bedrock", (new Block(Material.rock)).setBlockUnbreakable().setResistance(6000000.0F).setStepSound(soundTypePiston).setBlockName("bedrock").disableStats().setCreativeTab(CreativeTabs.tabBlock).setBlockTextureName("bedrock"));
        blockRegistry.addObject(8, "flowing_water", (new BlockDynamicLiquid(Material.water)).setHardness(100.0F).setLightOpacity(3).setBlockName("water").disableStats().setBlockTextureName("water_flow"));
        blockRegistry.addObject(9, "water", (new BlockStaticLiquid(Material.water)).setHardness(100.0F).setLightOpacity(3).setBlockName("water").disableStats().setBlockTextureName("water_still"));
        blockRegistry.addObject(10, "flowing_lava", (new BlockDynamicLiquid(Material.lava)).setHardness(100.0F).setLightLevel(1.0F).setBlockName("lava").disableStats().setBlockTextureName("lava_flow"));
        blockRegistry.addObject(11, "lava", (new BlockStaticLiquid(Material.lava)).setHardness(100.0F).setLightLevel(1.0F).setBlockName("lava").disableStats().setBlockTextureName("lava_still"));
        blockRegistry.addObject(12, "sand", (new BlockSand()).setHardness(0.5F).setStepSound(field_149776_m).setBlockName("sand").setBlockTextureName("sand"));
        blockRegistry.addObject(13, "gravel", (new BlockGravel()).setHardness(0.6F).setStepSound(soundTypeGravel).setBlockName("gravel").setBlockTextureName("gravel"));
        blockRegistry.addObject(14, "gold_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setBlockName("oreGold").setBlockTextureName("gold_ore"));
        blockRegistry.addObject(15, "iron_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setBlockName("oreIron").setBlockTextureName("iron_ore"));
        blockRegistry.addObject(16, "coal_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setBlockName("oreCoal").setBlockTextureName("coal_ore"));
        blockRegistry.addObject(17, "log", (new BlockOldLog()).setBlockName("log").setBlockTextureName("log"));
        blockRegistry.addObject(18, "leaves", (new BlockOldLeaf()).setBlockName("leaves").setBlockTextureName("leaves"));
        blockRegistry.addObject(19, "sponge", (new BlockSponge()).setHardness(0.6F).setStepSound(soundTypeGrass).setBlockName("sponge").setBlockTextureName("sponge"));
        blockRegistry.addObject(20, "glass", (new BlockGlass(Material.glass, false)).setHardness(0.3F).setStepSound(soundTypeGlass).setBlockName("glass").setBlockTextureName("glass"));
        blockRegistry.addObject(21, "lapis_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setBlockName("oreLapis").setBlockTextureName("lapis_ore"));
        blockRegistry.addObject(22, "lapis_block", (new BlockCompressed(MapColor.lapisColor)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setBlockName("blockLapis").setCreativeTab(CreativeTabs.tabBlock).setBlockTextureName("lapis_block"));
        blockRegistry.addObject(23, "dispenser", (new BlockDispenser()).setHardness(3.5F).setStepSound(soundTypePiston).setBlockName("dispenser").setBlockTextureName("dispenser"));
        Block var2 = (new BlockSandStone()).setStepSound(soundTypePiston).setHardness(0.8F).setBlockName("sandStone").setBlockTextureName("sandstone");
        blockRegistry.addObject(24, "sandstone", var2);
        blockRegistry.addObject(25, "noteblock", (new BlockNote()).setHardness(0.8F).setBlockName("musicBlock").setBlockTextureName("noteblock"));
        blockRegistry.addObject(26, "bed", (new BlockBed()).setHardness(0.2F).setBlockName("bed").disableStats().setBlockTextureName("bed"));
        blockRegistry.addObject(27, "golden_rail", (new BlockRailPowered()).setHardness(0.7F).setStepSound(soundTypeMetal).setBlockName("goldenRail").setBlockTextureName("rail_golden"));
        blockRegistry.addObject(28, "detector_rail", (new BlockRailDetector()).setHardness(0.7F).setStepSound(soundTypeMetal).setBlockName("detectorRail").setBlockTextureName("rail_detector"));
        blockRegistry.addObject(29, "sticky_piston", (new BlockPistonBase(true)).setBlockName("pistonStickyBase"));
        blockRegistry.addObject(30, "web", (new BlockWeb()).setLightOpacity(1).setHardness(4.0F).setBlockName("web").setBlockTextureName("web"));
        blockRegistry.addObject(31, "tallgrass", (new BlockTallGrass()).setHardness(0.0F).setStepSound(soundTypeGrass).setBlockName("tallgrass"));
        blockRegistry.addObject(32, "deadbush", (new BlockDeadBush()).setHardness(0.0F).setStepSound(soundTypeGrass).setBlockName("deadbush").setBlockTextureName("deadbush"));
        blockRegistry.addObject(33, "piston", (new BlockPistonBase(false)).setBlockName("pistonBase"));
        blockRegistry.addObject(34, "piston_head", new BlockPistonExtension());
        blockRegistry.addObject(35, "wool", (new BlockColored(Material.cloth)).setHardness(0.8F).setStepSound(soundTypeCloth).setBlockName("cloth").setBlockTextureName("wool_colored"));
        blockRegistry.addObject(36, "piston_extension", new BlockPistonMoving());
        blockRegistry.addObject(37, "yellow_flower", (new BlockFlower(0)).setHardness(0.0F).setStepSound(soundTypeGrass).setBlockName("flower1").setBlockTextureName("flower_dandelion"));
        blockRegistry.addObject(38, "red_flower", (new BlockFlower(1)).setHardness(0.0F).setStepSound(soundTypeGrass).setBlockName("flower2").setBlockTextureName("flower_rose"));
        blockRegistry.addObject(39, "brown_mushroom", (new BlockMushroom()).setHardness(0.0F).setStepSound(soundTypeGrass).setLightLevel(0.125F).setBlockName("mushroom").setBlockTextureName("mushroom_brown"));
        blockRegistry.addObject(40, "red_mushroom", (new BlockMushroom()).setHardness(0.0F).setStepSound(soundTypeGrass).setBlockName("mushroom").setBlockTextureName("mushroom_red"));
        blockRegistry.addObject(41, "gold_block", (new BlockCompressed(MapColor.goldColor)).setHardness(3.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setBlockName("blockGold").setBlockTextureName("gold_block"));
        blockRegistry.addObject(42, "iron_block", (new BlockCompressed(MapColor.ironColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setBlockName("blockIron").setBlockTextureName("iron_block"));
        blockRegistry.addObject(43, "double_stone_slab", (new BlockStoneSlab(true)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setBlockName("stoneSlab"));
        blockRegistry.addObject(44, "stone_slab", (new BlockStoneSlab(false)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setBlockName("stoneSlab"));
        Block var3 = (new Block(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setBlockName("brick").setCreativeTab(CreativeTabs.tabBlock).setBlockTextureName("brick");
        blockRegistry.addObject(45, "brick_block", var3);
        blockRegistry.addObject(46, "tnt", (new BlockTNT()).setHardness(0.0F).setStepSound(soundTypeGrass).setBlockName("tnt").setBlockTextureName("tnt"));
        blockRegistry.addObject(47, "bookshelf", (new BlockBookshelf()).setHardness(1.5F).setStepSound(soundTypeWood).setBlockName("bookshelf").setBlockTextureName("bookshelf"));
        blockRegistry.addObject(48, "mossy_cobblestone", (new Block(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setBlockName("stoneMoss").setCreativeTab(CreativeTabs.tabBlock).setBlockTextureName("cobblestone_mossy"));
        blockRegistry.addObject(49, "obsidian", (new BlockObsidian()).setHardness(50.0F).setResistance(2000.0F).setStepSound(soundTypePiston).setBlockName("obsidian").setBlockTextureName("obsidian"));
        blockRegistry.addObject(50, "torch", (new BlockTorch()).setHardness(0.0F).setLightLevel(0.9375F).setStepSound(soundTypeWood).setBlockName("torch").setBlockTextureName("torch_on"));
        blockRegistry.addObject(51, "fire", (new BlockFire()).setHardness(0.0F).setLightLevel(1.0F).setStepSound(soundTypeWood).setBlockName("fire").disableStats().setBlockTextureName("fire"));
        blockRegistry.addObject(52, "mob_spawner", (new BlockMobSpawner()).setHardness(5.0F).setStepSound(soundTypeMetal).setBlockName("mobSpawner").disableStats().setBlockTextureName("mob_spawner"));
        blockRegistry.addObject(53, "oak_stairs", (new BlockStairs(var1, 0)).setBlockName("stairsWood"));
        blockRegistry.addObject(54, "chest", (new BlockChest(0)).setHardness(2.5F).setStepSound(soundTypeWood).setBlockName("chest"));
        blockRegistry.addObject(55, "redstone_wire", (new BlockRedstoneWire()).setHardness(0.0F).setStepSound(soundTypeStone).setBlockName("redstoneDust").disableStats().setBlockTextureName("redstone_dust"));
        blockRegistry.addObject(56, "diamond_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setBlockName("oreDiamond").setBlockTextureName("diamond_ore"));
        blockRegistry.addObject(57, "diamond_block", (new BlockCompressed(MapColor.diamondColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setBlockName("blockDiamond").setBlockTextureName("diamond_block"));
        blockRegistry.addObject(58, "crafting_table", (new BlockWorkbench()).setHardness(2.5F).setStepSound(soundTypeWood).setBlockName("workbench").setBlockTextureName("crafting_table"));
        blockRegistry.addObject(59, "wheat", (new BlockCrops()).setBlockName("crops").setBlockTextureName("wheat"));
        Block var4 = (new BlockFarmland()).setHardness(0.6F).setStepSound(soundTypeGravel).setBlockName("farmland").setBlockTextureName("farmland");
        blockRegistry.addObject(60, "farmland", var4);
        blockRegistry.addObject(61, "furnace", (new BlockFurnace(false)).setHardness(3.5F).setStepSound(soundTypePiston).setBlockName("furnace").setCreativeTab(CreativeTabs.tabDecorations));
        blockRegistry.addObject(62, "lit_furnace", (new BlockFurnace(true)).setHardness(3.5F).setStepSound(soundTypePiston).setLightLevel(0.875F).setBlockName("furnace"));
        blockRegistry.addObject(63, "standing_sign", (new BlockSign(TileEntitySign.class, true)).setHardness(1.0F).setStepSound(soundTypeWood).setBlockName("sign").disableStats());
        blockRegistry.addObject(64, "wooden_door", (new BlockDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setBlockName("doorWood").disableStats().setBlockTextureName("door_wood"));
        blockRegistry.addObject(65, "ladder", (new BlockLadder()).setHardness(0.4F).setStepSound(soundTypeLadder).setBlockName("ladder").setBlockTextureName("ladder"));
        blockRegistry.addObject(66, "rail", (new BlockRail()).setHardness(0.7F).setStepSound(soundTypeMetal).setBlockName("rail").setBlockTextureName("rail_normal"));
        blockRegistry.addObject(67, "stone_stairs", (new BlockStairs(var0, 0)).setBlockName("stairsStone"));
        blockRegistry.addObject(68, "wall_sign", (new BlockSign(TileEntitySign.class, false)).setHardness(1.0F).setStepSound(soundTypeWood).setBlockName("sign").disableStats());
        blockRegistry.addObject(69, "lever", (new BlockLever()).setHardness(0.5F).setStepSound(soundTypeWood).setBlockName("lever").setBlockTextureName("lever"));
        blockRegistry.addObject(70, "stone_pressure_plate", (new BlockPressurePlate("stone", Material.rock, BlockPressurePlate.Sensitivity.mobs)).setHardness(0.5F).setStepSound(soundTypePiston).setBlockName("pressurePlate"));
        blockRegistry.addObject(71, "iron_door", (new BlockDoor(Material.iron)).setHardness(5.0F).setStepSound(soundTypeMetal).setBlockName("doorIron").disableStats().setBlockTextureName("door_iron"));
        blockRegistry.addObject(72, "wooden_pressure_plate", (new BlockPressurePlate("planks_oak", Material.wood, BlockPressurePlate.Sensitivity.everything)).setHardness(0.5F).setStepSound(soundTypeWood).setBlockName("pressurePlate"));
        blockRegistry.addObject(73, "redstone_ore", (new BlockRedstoneOre(false)).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setBlockName("oreRedstone").setCreativeTab(CreativeTabs.tabBlock).setBlockTextureName("redstone_ore"));
        blockRegistry.addObject(74, "lit_redstone_ore", (new BlockRedstoneOre(true)).setLightLevel(0.625F).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setBlockName("oreRedstone").setBlockTextureName("redstone_ore"));
        blockRegistry.addObject(75, "unlit_redstone_torch", (new BlockRedstoneTorch(false)).setHardness(0.0F).setStepSound(soundTypeWood).setBlockName("notGate").setBlockTextureName("redstone_torch_off"));
        blockRegistry.addObject(76, "redstone_torch", (new BlockRedstoneTorch(true)).setHardness(0.0F).setLightLevel(0.5F).setStepSound(soundTypeWood).setBlockName("notGate").setCreativeTab(CreativeTabs.tabRedstone).setBlockTextureName("redstone_torch_on"));
        blockRegistry.addObject(77, "stone_button", (new BlockButtonStone()).setHardness(0.5F).setStepSound(soundTypePiston).setBlockName("button"));
        blockRegistry.addObject(78, "snow_layer", (new BlockSnow()).setHardness(0.1F).setStepSound(soundTypeSnow).setBlockName("snow").setLightOpacity(0).setBlockTextureName("snow"));
        blockRegistry.addObject(79, "ice", (new BlockIce()).setHardness(0.5F).setLightOpacity(3).setStepSound(soundTypeGlass).setBlockName("ice").setBlockTextureName("ice"));
        blockRegistry.addObject(80, "snow", (new BlockSnowBlock()).setHardness(0.2F).setStepSound(soundTypeSnow).setBlockName("snow").setBlockTextureName("snow"));
        blockRegistry.addObject(81, "cactus", (new BlockCactus()).setHardness(0.4F).setStepSound(soundTypeCloth).setBlockName("cactus").setBlockTextureName("cactus"));
        blockRegistry.addObject(82, "clay", (new BlockClay()).setHardness(0.6F).setStepSound(soundTypeGravel).setBlockName("clay").setBlockTextureName("clay"));
        blockRegistry.addObject(83, "reeds", (new BlockReed()).setHardness(0.0F).setStepSound(soundTypeGrass).setBlockName("reeds").disableStats().setBlockTextureName("reeds"));
        blockRegistry.addObject(84, "jukebox", (new BlockJukebox()).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setBlockName("jukebox").setBlockTextureName("jukebox"));
        blockRegistry.addObject(85, "fence", (new BlockFence("planks_oak", Material.wood)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setBlockName("fence"));
        Block var5 = (new BlockPumpkin(false)).setHardness(1.0F).setStepSound(soundTypeWood).setBlockName("pumpkin").setBlockTextureName("pumpkin");
        blockRegistry.addObject(86, "pumpkin", var5);
        blockRegistry.addObject(87, "netherrack", (new BlockNetherrack()).setHardness(0.4F).setStepSound(soundTypePiston).setBlockName("hellrock").setBlockTextureName("netherrack"));
        blockRegistry.addObject(88, "soul_sand", (new BlockSoulSand()).setHardness(0.5F).setStepSound(field_149776_m).setBlockName("hellsand").setBlockTextureName("soul_sand"));
        blockRegistry.addObject(89, "glowstone", (new BlockGlowstone(Material.glass)).setHardness(0.3F).setStepSound(soundTypeGlass).setLightLevel(1.0F).setBlockName("lightgem").setBlockTextureName("glowstone"));
        blockRegistry.addObject(90, "portal", (new BlockPortal()).setHardness(-1.0F).setStepSound(soundTypeGlass).setLightLevel(0.75F).setBlockName("portal").setBlockTextureName("portal"));
        blockRegistry.addObject(91, "lit_pumpkin", (new BlockPumpkin(true)).setHardness(1.0F).setStepSound(soundTypeWood).setLightLevel(1.0F).setBlockName("litpumpkin").setBlockTextureName("pumpkin"));
        blockRegistry.addObject(92, "cake", (new BlockCake()).setHardness(0.5F).setStepSound(soundTypeCloth).setBlockName("cake").disableStats().setBlockTextureName("cake"));
        blockRegistry.addObject(93, "unpowered_repeater", (new BlockRedstoneRepeater(false)).setHardness(0.0F).setStepSound(soundTypeWood).setBlockName("diode").disableStats().setBlockTextureName("repeater_off"));
        blockRegistry.addObject(94, "powered_repeater", (new BlockRedstoneRepeater(true)).setHardness(0.0F).setLightLevel(0.625F).setStepSound(soundTypeWood).setBlockName("diode").disableStats().setBlockTextureName("repeater_on"));
        blockRegistry.addObject(95, "stained_glass", (new BlockStainedGlass(Material.glass)).setHardness(0.3F).setStepSound(soundTypeGlass).setBlockName("stainedGlass").setBlockTextureName("glass"));
        blockRegistry.addObject(96, "trapdoor", (new BlockTrapDoor(Material.wood)).setHardness(3.0F).setStepSound(soundTypeWood).setBlockName("trapdoor").disableStats().setBlockTextureName("trapdoor"));
        blockRegistry.addObject(97, "monster_egg", (new BlockSilverfish()).setHardness(0.75F).setBlockName("monsterStoneEgg"));
        Block var6 = (new BlockStoneBrick()).setHardness(1.5F).setResistance(10.0F).setStepSound(soundTypePiston).setBlockName("stonebricksmooth").setBlockTextureName("stonebrick");
        blockRegistry.addObject(98, "stonebrick", var6);
        blockRegistry.addObject(99, "brown_mushroom_block", (new BlockHugeMushroom(Material.wood, 0)).setHardness(0.2F).setStepSound(soundTypeWood).setBlockName("mushroom").setBlockTextureName("mushroom_block"));
        blockRegistry.addObject(100, "red_mushroom_block", (new BlockHugeMushroom(Material.wood, 1)).setHardness(0.2F).setStepSound(soundTypeWood).setBlockName("mushroom").setBlockTextureName("mushroom_block"));
        blockRegistry.addObject(101, "iron_bars", (new BlockPane("iron_bars", "iron_bars", Material.iron, true)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setBlockName("fenceIron"));
        blockRegistry.addObject(102, "glass_pane", (new BlockPane("glass", "glass_pane_top", Material.glass, false)).setHardness(0.3F).setStepSound(soundTypeGlass).setBlockName("thinGlass"));
        Block var7 = (new BlockMelon()).setHardness(1.0F).setStepSound(soundTypeWood).setBlockName("melon").setBlockTextureName("melon");
        blockRegistry.addObject(103, "melon_block", var7);
        blockRegistry.addObject(104, "pumpkin_stem", (new BlockStem(var5)).setHardness(0.0F).setStepSound(soundTypeWood).setBlockName("pumpkinStem").setBlockTextureName("pumpkin_stem"));
        blockRegistry.addObject(105, "melon_stem", (new BlockStem(var7)).setHardness(0.0F).setStepSound(soundTypeWood).setBlockName("pumpkinStem").setBlockTextureName("melon_stem"));
        blockRegistry.addObject(106, "vine", (new BlockVine()).setHardness(0.2F).setStepSound(soundTypeGrass).setBlockName("vine").setBlockTextureName("vine"));
        blockRegistry.addObject(107, "fence_gate", (new BlockFenceGate()).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setBlockName("fenceGate"));
        blockRegistry.addObject(108, "brick_stairs", (new BlockStairs(var3, 0)).setBlockName("stairsBrick"));
        blockRegistry.addObject(109, "stone_brick_stairs", (new BlockStairs(var6, 0)).setBlockName("stairsStoneBrickSmooth"));
        blockRegistry.addObject(110, "mycelium", (new BlockMycelium()).setHardness(0.6F).setStepSound(soundTypeGrass).setBlockName("mycel").setBlockTextureName("mycelium"));
        blockRegistry.addObject(111, "waterlily", (new BlockLilyPad()).setHardness(0.0F).setStepSound(soundTypeGrass).setBlockName("waterlily").setBlockTextureName("waterlily"));
        Block var8 = (new Block(Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setBlockName("netherBrick").setCreativeTab(CreativeTabs.tabBlock).setBlockTextureName("nether_brick");
        blockRegistry.addObject(112, "nether_brick", var8);
        blockRegistry.addObject(113, "nether_brick_fence", (new BlockFence("nether_brick", Material.rock)).setHardness(2.0F).setResistance(10.0F).setStepSound(soundTypePiston).setBlockName("netherFence"));
        blockRegistry.addObject(114, "nether_brick_stairs", (new BlockStairs(var8, 0)).setBlockName("stairsNetherBrick"));
        blockRegistry.addObject(115, "nether_wart", (new BlockNetherWart()).setBlockName("netherStalk").setBlockTextureName("nether_wart"));
        blockRegistry.addObject(116, "enchanting_table", (new BlockEnchantmentTable()).setHardness(5.0F).setResistance(2000.0F).setBlockName("enchantmentTable").setBlockTextureName("enchanting_table"));
        blockRegistry.addObject(117, "brewing_stand", (new BlockBrewingStand()).setHardness(0.5F).setLightLevel(0.125F).setBlockName("brewingStand").setBlockTextureName("brewing_stand"));
        blockRegistry.addObject(118, "cauldron", (new BlockCauldron()).setHardness(2.0F).setBlockName("cauldron").setBlockTextureName("cauldron"));
        blockRegistry.addObject(119, "end_portal", (new BlockEndPortal(Material.Portal)).setHardness(-1.0F).setResistance(6000000.0F));
        blockRegistry.addObject(120, "end_portal_frame", (new BlockEndPortalFrame()).setStepSound(soundTypeGlass).setLightLevel(0.125F).setHardness(-1.0F).setBlockName("endPortalFrame").setResistance(6000000.0F).setCreativeTab(CreativeTabs.tabDecorations).setBlockTextureName("endframe"));
        blockRegistry.addObject(121, "end_stone", (new Block(Material.rock)).setHardness(3.0F).setResistance(15.0F).setStepSound(soundTypePiston).setBlockName("whiteStone").setCreativeTab(CreativeTabs.tabBlock).setBlockTextureName("end_stone"));
        blockRegistry.addObject(122, "dragon_egg", (new BlockDragonEgg()).setHardness(3.0F).setResistance(15.0F).setStepSound(soundTypePiston).setLightLevel(0.125F).setBlockName("dragonEgg").setBlockTextureName("dragon_egg"));
        blockRegistry.addObject(123, "redstone_lamp", (new BlockRedstoneLight(false)).setHardness(0.3F).setStepSound(soundTypeGlass).setBlockName("redstoneLight").setCreativeTab(CreativeTabs.tabRedstone).setBlockTextureName("redstone_lamp_off"));
        blockRegistry.addObject(124, "lit_redstone_lamp", (new BlockRedstoneLight(true)).setHardness(0.3F).setStepSound(soundTypeGlass).setBlockName("redstoneLight").setBlockTextureName("redstone_lamp_on"));
        blockRegistry.addObject(125, "double_wooden_slab", (new BlockWoodSlab(true)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setBlockName("woodSlab"));
        blockRegistry.addObject(126, "wooden_slab", (new BlockWoodSlab(false)).setHardness(2.0F).setResistance(5.0F).setStepSound(soundTypeWood).setBlockName("woodSlab"));
        blockRegistry.addObject(127, "cocoa", (new BlockCocoa()).setHardness(0.2F).setResistance(5.0F).setStepSound(soundTypeWood).setBlockName("cocoa").setBlockTextureName("cocoa"));
        blockRegistry.addObject(128, "sandstone_stairs", (new BlockStairs(var2, 0)).setBlockName("stairsSandStone"));
        blockRegistry.addObject(129, "emerald_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setBlockName("oreEmerald").setBlockTextureName("emerald_ore"));
        blockRegistry.addObject(130, "ender_chest", (new BlockEnderChest()).setHardness(22.5F).setResistance(1000.0F).setStepSound(soundTypePiston).setBlockName("enderChest").setLightLevel(0.5F));
        blockRegistry.addObject(131, "tripwire_hook", (new BlockTripWireHook()).setBlockName("tripWireSource").setBlockTextureName("trip_wire_source"));
        blockRegistry.addObject(132, "tripwire", (new BlockTripWire()).setBlockName("tripWire").setBlockTextureName("trip_wire"));
        blockRegistry.addObject(133, "emerald_block", (new BlockCompressed(MapColor.emeraldColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setBlockName("blockEmerald").setBlockTextureName("emerald_block"));
        blockRegistry.addObject(134, "spruce_stairs", (new BlockStairs(var1, 1)).setBlockName("stairsWoodSpruce"));
        blockRegistry.addObject(135, "birch_stairs", (new BlockStairs(var1, 2)).setBlockName("stairsWoodBirch"));
        blockRegistry.addObject(136, "jungle_stairs", (new BlockStairs(var1, 3)).setBlockName("stairsWoodJungle"));
        blockRegistry.addObject(137, "command_block", (new BlockCommandBlock()).setBlockUnbreakable().setResistance(6000000.0F).setBlockName("commandBlock").setBlockTextureName("command_block"));
        blockRegistry.addObject(138, "beacon", (new BlockBeacon()).setBlockName("beacon").setLightLevel(1.0F).setBlockTextureName("beacon"));
        blockRegistry.addObject(139, "cobblestone_wall", (new BlockWall(var0)).setBlockName("cobbleWall"));
        blockRegistry.addObject(140, "flower_pot", (new BlockFlowerPot()).setHardness(0.0F).setStepSound(soundTypeStone).setBlockName("flowerPot").setBlockTextureName("flower_pot"));
        blockRegistry.addObject(141, "carrots", (new BlockCarrot()).setBlockName("carrots").setBlockTextureName("carrots"));
        blockRegistry.addObject(142, "potatoes", (new BlockPotato()).setBlockName("potatoes").setBlockTextureName("potatoes"));
        blockRegistry.addObject(143, "wooden_button", (new BlockButtonWood()).setHardness(0.5F).setStepSound(soundTypeWood).setBlockName("button"));
        blockRegistry.addObject(144, "skull", (new BlockSkull()).setHardness(1.0F).setStepSound(soundTypePiston).setBlockName("skull").setBlockTextureName("skull"));
        blockRegistry.addObject(145, "anvil", (new BlockAnvil()).setHardness(5.0F).setStepSound(soundTypeAnvil).setResistance(2000.0F).setBlockName("anvil"));
        blockRegistry.addObject(146, "trapped_chest", (new BlockChest(1)).setHardness(2.5F).setStepSound(soundTypeWood).setBlockName("chestTrap"));
        blockRegistry.addObject(147, "light_weighted_pressure_plate", (new BlockPressurePlateWeighted("gold_block", Material.iron, 15)).setHardness(0.5F).setStepSound(soundTypeWood).setBlockName("weightedPlate_light"));
        blockRegistry.addObject(148, "heavy_weighted_pressure_plate", (new BlockPressurePlateWeighted("iron_block", Material.iron, 150)).setHardness(0.5F).setStepSound(soundTypeWood).setBlockName("weightedPlate_heavy"));
        blockRegistry.addObject(149, "unpowered_comparator", (new BlockRedstoneComparator(false)).setHardness(0.0F).setStepSound(soundTypeWood).setBlockName("comparator").disableStats().setBlockTextureName("comparator_off"));
        blockRegistry.addObject(150, "powered_comparator", (new BlockRedstoneComparator(true)).setHardness(0.0F).setLightLevel(0.625F).setStepSound(soundTypeWood).setBlockName("comparator").disableStats().setBlockTextureName("comparator_on"));
        blockRegistry.addObject(151, "daylight_detector", (new BlockDaylightDetector()).setHardness(0.2F).setStepSound(soundTypeWood).setBlockName("daylightDetector").setBlockTextureName("daylight_detector"));
        blockRegistry.addObject(152, "redstone_block", (new BlockCompressedPowered(MapColor.tntColor)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypeMetal).setBlockName("blockRedstone").setBlockTextureName("redstone_block"));
        blockRegistry.addObject(153, "quartz_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setStepSound(soundTypePiston).setBlockName("netherquartz").setBlockTextureName("quartz_ore"));
        blockRegistry.addObject(154, "hopper", (new BlockHopper()).setHardness(3.0F).setResistance(8.0F).setStepSound(soundTypeWood).setBlockName("hopper").setBlockTextureName("hopper"));
        Block var9 = (new BlockQuartz()).setStepSound(soundTypePiston).setHardness(0.8F).setBlockName("quartzBlock").setBlockTextureName("quartz_block");
        blockRegistry.addObject(155, "quartz_block", var9);
        blockRegistry.addObject(156, "quartz_stairs", (new BlockStairs(var9, 0)).setBlockName("stairsQuartz"));
        blockRegistry.addObject(157, "activator_rail", (new BlockRailPowered()).setHardness(0.7F).setStepSound(soundTypeMetal).setBlockName("activatorRail").setBlockTextureName("rail_activator"));
        blockRegistry.addObject(158, "dropper", (new BlockDropper()).setHardness(3.5F).setStepSound(soundTypePiston).setBlockName("dropper").setBlockTextureName("dropper"));
        blockRegistry.addObject(159, "stained_hardened_clay", (new BlockColored(Material.rock)).setHardness(1.25F).setResistance(7.0F).setStepSound(soundTypePiston).setBlockName("clayHardenedStained").setBlockTextureName("hardened_clay_stained"));
        blockRegistry.addObject(160, "stained_glass_pane", (new BlockStainedGlassPane()).setHardness(0.3F).setStepSound(soundTypeGlass).setBlockName("thinStainedGlass").setBlockTextureName("glass"));
        blockRegistry.addObject(161, "leaves2", (new BlockNewLeaf()).setBlockName("leaves").setBlockTextureName("leaves"));
        blockRegistry.addObject(162, "log2", (new BlockNewLog()).setBlockName("log").setBlockTextureName("log"));
        blockRegistry.addObject(163, "acacia_stairs", (new BlockStairs(var1, 4)).setBlockName("stairsWoodAcacia"));
        blockRegistry.addObject(164, "dark_oak_stairs", (new BlockStairs(var1, 5)).setBlockName("stairsWoodDarkOak"));
        blockRegistry.addObject(170, "hay_block", (new BlockHay()).setHardness(0.5F).setStepSound(soundTypeGrass).setBlockName("hayBlock").setCreativeTab(CreativeTabs.tabBlock).setBlockTextureName("hay_block"));
        blockRegistry.addObject(171, "carpet", (new BlockCarpet()).setHardness(0.1F).setStepSound(soundTypeCloth).setBlockName("woolCarpet").setLightOpacity(0));
        blockRegistry.addObject(172, "hardened_clay", (new BlockHardenedClay()).setHardness(1.25F).setResistance(7.0F).setStepSound(soundTypePiston).setBlockName("clayHardened").setBlockTextureName("hardened_clay"));
        blockRegistry.addObject(173, "coal_block", (new Block(Material.rock)).setHardness(5.0F).setResistance(10.0F).setStepSound(soundTypePiston).setBlockName("blockCoal").setCreativeTab(CreativeTabs.tabBlock).setBlockTextureName("coal_block"));
        blockRegistry.addObject(174, "packed_ice", (new BlockPackedIce()).setHardness(0.5F).setStepSound(soundTypeGlass).setBlockName("icePacked").setBlockTextureName("ice_packed"));
        blockRegistry.addObject(175, "double_plant", new BlockDoublePlant());
        Iterator var10 = blockRegistry.iterator();

        while (var10.hasNext()) {
            Block var11 = (Block) var10.next();

            if (var11.blockMaterial == Material.air) {
                var11.field_149783_u = false;
            } else {
                boolean var12 = false;
                boolean var13 = var11.getRenderType() == 10;
                boolean var14 = var11 instanceof BlockSlab;
                boolean var15 = var11 == var4;
                boolean var16 = var11.canBlockGrass;
                boolean var17 = var11.lightOpacity == 0;

                if (var13 || var14 || var15 || var16 || var17) {
                    var12 = true;
                }

                var11.field_149783_u = var12;
            }
        }
    }

    protected Block(Material materialIn) {
        this.stepSound = soundTypeStone;
        this.blockParticleGravity = 1.0F;
        this.slipperiness = 0.6F;
        this.blockMaterial = materialIn;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.opaque = this.isOpaqueCube();
        this.lightOpacity = this.isOpaqueCube() ? 255 : 0;
        this.canBlockGrass = !materialIn.getCanBlockGrass();
    }

    public boolean isGlassCube() {
        return false;
    }

    /**
     * Sets the footstep sound for the block. Returns the object for convenience in constructing.
     */
    protected Block setStepSound(Block.SoundType sound) {
        this.stepSound = sound;
        return this;
    }

    /**
     * Sets how much light is blocked going through this block. Returns the object for convenience in constructing.
     */
    public Block setLightOpacity(int opacity) {
        this.lightOpacity = opacity;
        return this;
    }

    /**
     * Sets the light value that the block emits. Returns resulting block instance for constructing convenience. Args:
     * level
     */
    protected Block setLightLevel(float value) {
        this.lightValue = (int) (15.0F * value);
        return this;
    }

    /**
     * Sets the the blocks resistance to explosions. Returns the object for convenience in constructing.
     */
    protected Block setResistance(float resistance) {
        this.blockResistance = resistance * 3.0F;
        return this;
    }

    /**
     * Indicate if a material is a normal solid opaque cube
     */
    public boolean isBlockNormalCube() {
        return this.blockMaterial.blocksMovement() && this.renderAsNormalBlock();
    }

    public boolean isNormalCube() {
        return this.blockMaterial.isOpaque() && this.renderAsNormalBlock() && !this.canProvidePower();
    }

    public boolean renderAsNormalBlock() {
        return true;
    }

    public boolean getBlocksMovement(IBlockAccess worldIn, int x, int y, int z) {
        return !this.blockMaterial.blocksMovement();
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {
        return 0;
    }

    /**
     * Sets how many hits it takes to break a block.
     */
    protected Block setHardness(float hardness) {
        this.blockHardness = hardness;

        if (this.blockResistance < hardness * 5.0F) {
            this.blockResistance = hardness * 5.0F;
        }

        return this;
    }

    protected Block setBlockUnbreakable() {
        this.setHardness(-1.0F);
        return this;
    }

    public float getBlockHardness(World worldIn, int x, int y, int z) {
        return this.blockHardness;
    }

    /**
     * Sets whether this block type will receive random update ticks
     */
    protected Block setTickRandomly(boolean shouldTick) {
        this.needsRandomTick = shouldTick;
        return this;
    }

    /**
     * Returns whether or not this block is of a type that needs random ticking. Called for ref-counting purposes by
     * ExtendedBlockStorage in order to broadly cull a chunk from the random chunk update list for efficiency's sake.
     */
    public boolean getTickRandomly() {
        return this.needsRandomTick;
    }

    public boolean hasTileEntity() {
        return this.isBlockContainer;
    }

    protected final void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.field_149759_B = (double) minX;
        this.field_149760_C = (double) minY;
        this.field_149754_D = (double) minZ;
        this.field_149755_E = (double) maxX;
        this.field_149756_F = (double) maxY;
        this.maxZ = (double) maxZ;
    }

    public int getBlockBrightness(IBlockAccess worldIn, int x, int y, int z) {
        Block var5 = worldIn.getBlock(x, y, z);
        int var6 = worldIn.getLightBrightnessForSkyBlocks(x, y, z, var5.getLightValue());

        if (var6 == 0 && var5 instanceof BlockSlab) {
            --y;
            var5 = worldIn.getBlock(x, y, z);
            return worldIn.getLightBrightnessForSkyBlocks(x, y, z, var5.getLightValue());
        } else {
            return var6;
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side) {
        return side == 0 && this.field_149760_C > 0.0D || (side == 1 && this.field_149756_F < 1.0D || (side == 2 && this.field_149754_D > 0.0D || (side == 3 && this.maxZ < 1.0D || (side == 4 && this.field_149759_B > 0.0D || (side == 5 && this.field_149755_E < 1.0D || !worldIn.getBlock(x, y, z).isOpaqueCube())))));
    }

    public boolean isBlockSolid(IBlockAccess worldIn, int x, int y, int z, int side) {
        return worldIn.getBlock(x, y, z).getMaterial().isSolid();
    }

    public IIcon getIcon(IBlockAccess worldIn, int x, int y, int z, int side) {
        return this.getIcon(side, worldIn.getBlockMetadata(x, y, z));
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta) {
        return this.blockIcon;
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    public final IIcon getBlockTextureFromSide(int side) {
        return this.getIcon(side, 0);
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox((double) x + this.field_149759_B, (double) y + this.field_149760_C, (double) z + this.field_149754_D, (double) x + this.field_149755_E, (double) y + this.field_149756_F, (double) z + this.maxZ);
    }

    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list, Entity collider) {
        AxisAlignedBB var8 = this.getCollisionBoundingBoxFromPool(worldIn, x, y, z);

        if (var8 != null && mask.intersectsWith(var8)) {
            list.add(var8);
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox((double) x + this.field_149759_B, (double) y + this.field_149760_C, (double) z + this.field_149754_D, (double) x + this.field_149755_E, (double) y + this.field_149756_F, (double) z + this.maxZ);
    }

    public boolean isOpaqueCube() {
        return true;
    }

    /**
     * Returns whether this block is collideable based on the arguments passed in \n@param par1 block metaData \n@param
     * par2 whether the player right-clicked while holding a boat
     */
    public boolean canCollideCheck(int meta, boolean includeLiquid) {
        return this.isCollidable();
    }

    public boolean isCollidable() {
        return true;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World worldIn, int x, int y, int z, Random random) {
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random) {}

    public void onBlockDestroyedByPlayer(World worldIn, int x, int y, int z, int meta) {}

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor) {}

    public int tickRate(World worldIn) {
        return 10;
    }

    public void onBlockAdded(World worldIn, int x, int y, int z) {}

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta) {}

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random) {
        return 1;
    }

    public Item getItemDropped(int meta, Random random, int fortune) {
        return Item.getItemFromBlock(this);
    }

    public float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, int x, int y, int z) {
        float var6 = this.getBlockHardness(worldIn, x, y, z);
        return var6 < 0.0F ? 0.0F : (!player.canHarvestBlock(this) ? player.getCurrentPlayerStrVsBlock(this, false) / var6 / 100.0F : player.getCurrentPlayerStrVsBlock(this, true) / var6 / 30.0F);
    }

    /**
     * Drops the specified block items
     */
    public final void dropBlockAsItem(World worldIn, int x, int y, int z, int meta, int fortune) {
        this.dropBlockAsItemWithChance(worldIn, x, y, z, meta, 1.0F, fortune);
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune) {
        if (!worldIn.isClient) {
            int var8 = this.quantityDroppedWithBonus(fortune, worldIn.rand);

            for (int var9 = 0; var9 < var8; ++var9) {
                if (worldIn.rand.nextFloat() <= chance) {
                    Item var10 = this.getItemDropped(meta, worldIn.rand, fortune);

                    if (var10 != null) {
                        this.dropBlockAsItem_do(worldIn, x, y, z, new ItemStack(var10, 1, this.damageDropped(meta)));
                    }
                }
            }
        }
    }

    /**
     * Spawns EntityItem in the world for the given ItemStack if the world is not remote.
     */
    protected void dropBlockAsItem_do(World worldIn, int x, int y, int z, ItemStack itemIn) {
        if (!worldIn.isClient && worldIn.getGameRules().getGameRuleBooleanValue("doTileDrops")) {
            float var6 = 0.7F;
            double var7 = (double) (worldIn.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
            double var9 = (double) (worldIn.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
            double var11 = (double) (worldIn.rand.nextFloat() * var6) + (double) (1.0F - var6) * 0.5D;
            EntityItem var13 = new EntityItem(worldIn, (double) x + var7, (double) y + var9, (double) z + var11, itemIn);
            var13.delayBeforeCanPickup = 10;
            worldIn.spawnEntityInWorld(var13);
        }
    }

    protected void dropXpOnBlockBreak(World worldIn, int x, int y, int z, int amount) {
        if (!worldIn.isClient) {
            while (amount > 0) {
                int var6 = EntityXPOrb.getXPSplit(amount);
                amount -= var6;
                worldIn.spawnEntityInWorld(new EntityXPOrb(worldIn, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, var6));
            }
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta) {
        return 0;
    }

    /**
     * Returns how much this block can resist explosions from the passed in entity.
     */
    public float getExplosionResistance(Entity exploder) {
        return this.blockResistance / 5.0F;
    }

    public MovingObjectPosition collisionRayTrace(World worldIn, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        startVec = startVec.addVector((double) (-x), (double) (-y), (double) (-z));
        endVec = endVec.addVector((double) (-x), (double) (-y), (double) (-z));
        Vec3 var7 = startVec.getIntermediateWithXValue(endVec, this.field_149759_B);
        Vec3 var8 = startVec.getIntermediateWithXValue(endVec, this.field_149755_E);
        Vec3 var9 = startVec.getIntermediateWithYValue(endVec, this.field_149760_C);
        Vec3 var10 = startVec.getIntermediateWithYValue(endVec, this.field_149756_F);
        Vec3 var11 = startVec.getIntermediateWithZValue(endVec, this.field_149754_D);
        Vec3 var12 = startVec.getIntermediateWithZValue(endVec, this.maxZ);

        if (!this.isVecInsideYZBounds(var7)) {
            var7 = null;
        }

        if (!this.isVecInsideYZBounds(var8)) {
            var8 = null;
        }

        if (!this.isVecInsideXZBounds(var9)) {
            var9 = null;
        }

        if (!this.isVecInsideXZBounds(var10)) {
            var10 = null;
        }

        if (!this.isVecInsideXYBounds(var11)) {
            var11 = null;
        }

        if (!this.isVecInsideXYBounds(var12)) {
            var12 = null;
        }

        Vec3 var13 = null;

        if (var7 != null && (var13 == null || startVec.squareDistanceTo(var7) < startVec.squareDistanceTo(var13))) {
            var13 = var7;
        }

        if (var8 != null && (var13 == null || startVec.squareDistanceTo(var8) < startVec.squareDistanceTo(var13))) {
            var13 = var8;
        }

        if (var9 != null && (var13 == null || startVec.squareDistanceTo(var9) < startVec.squareDistanceTo(var13))) {
            var13 = var9;
        }

        if (var10 != null && (var13 == null || startVec.squareDistanceTo(var10) < startVec.squareDistanceTo(var13))) {
            var13 = var10;
        }

        if (var11 != null && (var13 == null || startVec.squareDistanceTo(var11) < startVec.squareDistanceTo(var13))) {
            var13 = var11;
        }

        if (var12 != null && (var13 == null || startVec.squareDistanceTo(var12) < startVec.squareDistanceTo(var13))) {
            var13 = var12;
        }

        if (var13 == null) {
            return null;
        } else {
            byte var14 = -1;

            if (var13 == var7) {
                var14 = 4;
            }

            if (var13 == var8) {
                var14 = 5;
            }

            if (var13 == var9) {
                var14 = 0;
            }

            if (var13 == var10) {
                var14 = 1;
            }

            if (var13 == var11) {
                var14 = 2;
            }

            if (var13 == var12) {
                var14 = 3;
            }

            return new MovingObjectPosition(x, y, z, var14, var13.addVector((double) x, (double) y, (double) z));
        }
    }

    /**
     * Checks if a vector is within the Y and Z bounds of the block.
     */
    private boolean isVecInsideYZBounds(Vec3 point) {
        return point != null && (point.yCoord >= this.field_149760_C && point.yCoord <= this.field_149756_F && point.zCoord >= this.field_149754_D && point.zCoord <= this.maxZ);
    }

    /**
     * Checks if a vector is within the X and Z bounds of the block.
     */
    private boolean isVecInsideXZBounds(Vec3 point) {
        return point != null && (point.xCoord >= this.field_149759_B && point.xCoord <= this.field_149755_E && point.zCoord >= this.field_149754_D && point.zCoord <= this.maxZ);
    }

    /**
     * Checks if a vector is within the X and Y bounds of the block.
     */
    private boolean isVecInsideXYBounds(Vec3 point) {
        return point != null && (point.xCoord >= this.field_149759_B && point.xCoord <= this.field_149755_E && point.yCoord >= this.field_149760_C && point.yCoord <= this.field_149756_F);
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    public void onBlockDestroyedByExplosion(World worldIn, int x, int y, int z, Explosion explosionIn) {
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass() {
        return 0;
    }

    public boolean canReplace(World worldIn, int x, int y, int z, int side, ItemStack itemIn) {
        return this.canPlaceBlockOnSide(worldIn, x, y, z, side);
    }

    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World worldIn, int x, int y, int z, int side) {
        return this.canPlaceBlockAt(worldIn, x, y, z);
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z) {
        return worldIn.getBlock(x, y, z).blockMaterial.isReplaceable();
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ) {
        return false;
    }

    public void onEntityWalking(World worldIn, int x, int y, int z, Entity entityIn) {
    }

    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta) {
        return meta;
    }

    /**
     * Called when a player hits the block. Args: world, x, y, z, player
     */
    public void onBlockClicked(World worldIn, int x, int y, int z, EntityPlayer player) {
    }

    public void velocityToAddToEntity(World worldIn, int x, int y, int z, Entity entityIn, Vec3 velocity) {
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z) {
    }

    /**
     * returns the block bounderies minX value
     */
    public final double getBlockBoundsMinX() {
        return this.field_149759_B;
    }

    /**
     * returns the block bounderies maxX value
     */
    public final double getBlockBoundsMaxX() {
        return this.field_149755_E;
    }

    /**
     * returns the block bounderies minY value
     */
    public final double getBlockBoundsMinY() {
        return this.field_149760_C;
    }

    /**
     * returns the block bounderies maxY value
     */
    public final double getBlockBoundsMaxY() {
        return this.field_149756_F;
    }

    /**
     * returns the block bounderies minZ value
     */
    public final double getBlockBoundsMinZ() {
        return this.field_149754_D;
    }

    /**
     * returns the block bounderies maxZ value
     */
    public final double getBlockBoundsMaxZ() {
        return this.maxZ;
    }

    public int getBlockColor() {
        return 16777215;
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int meta) {
        return 16777215;
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z) {
        return 16777215;
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, int x, int y, int z, int side) {
        return 0;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower() {
        return false;
    }

    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn) {
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, int x, int y, int z, int side) {
        return 0;
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender() {
    }

    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta) {
        player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest() && EnchantmentHelper.getSilkTouchModifier(player)) {
            ItemStack var8 = this.createStackedBlock(meta);

            if (var8 != null) {
                this.dropBlockAsItem_do(worldIn, x, y, z, var8);
            }
        } else {
            int var7 = EnchantmentHelper.getFortuneModifier(player);
            this.dropBlockAsItem(worldIn, x, y, z, meta, var7);
        }
    }

    protected boolean canSilkHarvest() {
        return this.renderAsNormalBlock() && !this.isBlockContainer;
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int meta) {
        int var2 = 0;
        Item var3 = Item.getItemFromBlock(this);

        if (var3 != null && var3.getHasSubtypes()) {
            var2 = meta;
        }

        return new ItemStack(var3, 1, var2);
    }

    /**
     * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
     */
    public int quantityDroppedWithBonus(int maxBonus, Random random) {
        return this.quantityDropped(random);
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World worldIn, int x, int y, int z) {
        return true;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
    }

    /**
     * Called after a block is placed
     */
    public void onPostBlockPlaced(World worldIn, int x, int y, int z, int meta) {
    }

    /**
     * Sets the mod-specific block name
     */
    public Block setBlockName(String name) {
        this.unlocalizedNameBlock = name;
        return this;
    }

    /**
     * Gets the localized name of this block. Used for the statistics page.
     */
    public String getLocalizedName() {
        return StatCollector.translateToLocal(this.getUnlocalizedName() + ".name");
    }

    /**
     * Returns the unlocalized name of the block with "tile." appended to the front.
     */
    public String getUnlocalizedName() {
        return "tile." + this.unlocalizedNameBlock;
    }

    public boolean onBlockEventReceived(World worldIn, int x, int y, int z, int eventId, int eventData) {
        return false;
    }

    /**
     * Return the state of blocks statistics flags - if the block is counted for mined and placed.
     */
    public boolean getEnableStats() {
        return this.enableStats;
    }

    protected Block disableStats() {
        this.enableStats = false;
        return this;
    }

    public int getMobilityFlag() {
        return this.blockMaterial.getMaterialMobility();
    }

    /**
     * Returns the default ambient occlusion value based on block opacity
     */
    public float getAmbientOcclusionLightValue() {
        return this.isBlockNormalCube() ? 0.2F : 1.0F;
    }

    /**
     * Block's chance to react to an entity falling on it.
     */
    public void onFallenUpon(World worldIn, int x, int y, int z, Entity entityIn, float fallDistance) {
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    public Item getItem(World worldIn, int x, int y, int z) {
        return Item.getItemFromBlock(this);
    }

    /**
     * Get the block's damage value (for use with pick block).
     */
    public int getDamageValue(World worldIn, int x, int y, int z) {
        return this.damageDropped(worldIn.getBlockMetadata(x, y, z));
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, 0));
    }

    /**
     * Returns the CreativeTab to display the given block on.
     */
    public CreativeTabs getCreativeTabToDisplayOn() {
        return this.displayOnCreativeTab;
    }

    public Block setCreativeTab(CreativeTabs tab) {
        this.displayOnCreativeTab = tab;
        return this;
    }

    /**
     * Called when the block is attempted to be harvested
     */
    public void onBlockHarvested(World worldIn, int x, int y, int z, int meta, EntityPlayer player) {
    }

    public void onBlockPreDestroy(World worldIn, int x, int y, int z, int meta) {
    }

    /**
     * currently only used by BlockCauldron to incrament meta-data during rain
     */
    public void fillWithRain(World worldIn, int x, int y, int z) {
    }

    /**
     * Returns true only if block is flowerPot
     */
    public boolean isFlowerPot() {
        return false;
    }

    public boolean requiresUpdates() {
        return true;
    }

    /**
     * Return whether this block can drop from an explosion.
     */
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return true;
    }

    public boolean isAssociatedBlock(Block other) {
        return this == other;
    }

    public static boolean isEqualTo(Block blockIn, Block other) {
        return (blockIn != null && other != null) && (blockIn == other || blockIn.isAssociatedBlock(other));
    }

    public boolean hasComparatorInputOverride() {
        return false;
    }

    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side) {
        return 0;
    }

    protected Block setBlockTextureName(String textureName) {
        this.textureName = textureName;
        return this;
    }

    protected String getTextureName() {
        return this.textureName == null ? "MISSING_ICON_BLOCK_" + getIdFromBlock(this) + "_" + this.unlocalizedNameBlock : this.textureName;
    }

    public IIcon getItemIcon(int side, int meta) {
        return this.getIcon(side, meta);
    }

    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon(this.getTextureName());
    }

    /**
     * Gets the icon name of the ItemBlock corresponding to this block. Used by hoppers.
     */
    public String getItemIconName() {
        return null;
    }

    public static class SoundType {

        public final String field_150501_a;
        public final float field_150499_b;
        public final float field_150500_c;

        public SoundType(String name, float volume, float frequency) {
            this.field_150501_a = name;
            this.field_150499_b = volume;
            this.field_150500_c = frequency;
        }

        public float func_150497_c() {
            return this.field_150499_b;
        }

        public float func_150494_d() {
            return this.field_150500_c;
        }

        public String func_150495_a() {
            return "dig." + this.field_150501_a;
        }

        public String func_150498_e() {
            return "step." + this.field_150501_a;
        }

        public String func_150496_b() {
            return this.func_150495_a();
        }
    }
}