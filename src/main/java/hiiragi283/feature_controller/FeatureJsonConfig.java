package hiiragi283.feature_controller;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Desugar
public record FeatureJsonConfig(
        boolean enabled,
        Collection<DecorateBiomeEvent.Decorate.EventType> removeDecoration,
        Collection<PopulateChunkEvent.Populate.EventType> removeFeature,
        Collection<OreGenEvent.GenerateMinable.EventType> removeMinable,
        boolean removeCave,
        boolean removeRavine,
        boolean removeNetherCave,
        Collection<String> spawnBlacklist,
        boolean preventFromSpawner
) {

    public static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void writeFile() {

        Objects.requireNonNull(FeatureController.configFile);

        var file = new File(FeatureController.configFile, "/" + Tags.MOD_ID + ".json");
        if (file.exists()) return;

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (
                var fos = new FileOutputStream(file.getPath());
                var osw = new OutputStreamWriter(fos);
                var jw = new JsonWriter(osw)
        ) {
            jw.setIndent("  ");
            gson.toJson(new FeatureJsonConfig(
                    false,
                    Arrays.asList(DecorateBiomeEvent.Decorate.EventType.values()),
                    Arrays.asList(PopulateChunkEvent.Populate.EventType.values()),
                    Arrays.asList(OreGenEvent.GenerateMinable.EventType.values()),
                    true,
                    true,
                    true,
                    ForgeRegistries.ENTITIES.getValuesCollection().stream()
                            .map(IForgeRegistryEntry::getRegistryName)
                            .filter(Objects::nonNull)
                            .map(ResourceLocation::toString)
                            .collect(Collectors.toList()),
                    false
            ), FeatureJsonConfig.class, jw);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void readFile() {

        Objects.requireNonNull(FeatureController.configFile);

        var file = new File(FeatureController.configFile, "/" + Tags.MOD_ID + ".json");

        try (
                var fis = new FileInputStream(file.getPath());
                var isr = new InputStreamReader(fis);
                var js = new JsonReader((isr))
        ) {
            if (file.exists() && file.canRead()) {
                FeatureController.configJson = gson.fromJson(js, FeatureJsonConfig.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}