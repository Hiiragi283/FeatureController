package hiiragi283.feature_controller;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.MapGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, acceptedMinecraftVersions = "[1.12, 1.12.2]")
public class FeatureController {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    @Mod.EventHandler
    public void onConstruct(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.TERRAIN_GEN_BUS.register(this);
        MinecraftForge.ORE_GEN_BUS.register(this);
    }

    public static File configFile;

    public static FeatureJsonConfig configJson;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        configFile = event.getModConfigurationDirectory();
    }

    @Mod.EventHandler
    public void onComplete(FMLLoadCompleteEvent event) {
        FeatureJsonConfig.writeFile();
        FeatureJsonConfig.readFile();
    }

    //    Biome Decoration    //

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void removeBiomeDecoration(DecorateBiomeEvent.Decorate event) {
        if (!configJson.enabled()) return;
        if (configJson.removeDecoration().contains(event.getType())) {
            event.setResult(Event.Result.DENY);
        }
    }

    //    Chunk Feature    //

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void removeChunkFeatures(PopulateChunkEvent.Populate event) {
        if (!configJson.enabled()) return;
        if (configJson.removeFeature().contains(event.getType())) {
            event.setResult(Event.Result.DENY);
        }
    }

    //    Ore Generation    //

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void removeMinable(OreGenEvent.GenerateMinable event) {
        if (!configJson.enabled()) return;
        if (configJson.removeMinable().contains(event.getType())) {
            event.setResult(Event.Result.DENY);
        }
    }

    //    Structure Generation    //

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void removeMapGen(InitMapGenEvent event) {
        if (!configJson.enabled()) return;
        if (event.getType() == InitMapGenEvent.EventType.CAVE && configJson.removeCave()) {
            event.setNewGen(new MapGenBase());
        } else if (event.getType() == InitMapGenEvent.EventType.RAVINE && configJson.removeRavine()) {
            event.setNewGen(new MapGenBase());
        } else if (event.getType() == InitMapGenEvent.EventType.NETHER_CAVE && configJson.removeNetherCave()) {
            event.setNewGen(new MapGenBase());
        }
    }

    //    Entity Spawn    //

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void preventSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (!configJson.enabled()) return;
        EntityEntry entityEntry = EntityRegistry.getEntry(event.getEntityLiving().getClass());
        if (entityEntry == null) return;
        ResourceLocation registryName = entityEntry.getRegistryName();
        if (registryName == null) return;
        if (configJson.spawnBlacklist().contains(registryName.toString())) {
            if (event.isSpawner()) {
                if (configJson.preventFromSpawner()) {
                    event.setResult(Event.Result.DENY);
                }
            } else {
                event.setResult(Event.Result.DENY);
            }
        }
    }

}