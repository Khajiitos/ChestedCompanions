package me.khajiitos.chestedcompanions.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.khajiitos.chestedcompanions.common.ChestedCompanions;
import me.khajiitos.chestedcompanions.common.util.InventoryCapacity;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

public class CCConfig {
    private static final File file = new File("config/chestedcompanions.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Entry
    public static final CCConfigValues.BooleanValue invertShiftToOpen = new CCConfigValues.BooleanValue(false);

    @Entry
    public static final CCConfigValues.BooleanValue publicChest = new CCConfigValues.BooleanValue(false);

    @Entry
    public static final CCConfigValues.EnumValue<InventoryCapacity> catInventoryCapacity = new CCConfigValues.EnumValue<>(InventoryCapacity.ONE_ROW);

    @Entry
    public static final CCConfigValues.EnumValue<InventoryCapacity> wolfInventoryCapacity = new CCConfigValues.EnumValue<>(InventoryCapacity.ONE_ROW);

    @Entry(clientOnly = true)
    public static final CCConfigValues.BooleanValue hideCatChest = new CCConfigValues.BooleanValue(false);

    @Entry(clientOnly = true)
    public static final CCConfigValues.BooleanValue hideWolfChest = new CCConfigValues.BooleanValue(false);

    @Entry
    public static final CCConfigValues.BooleanValue allowChestOnBabyCat = new CCConfigValues.BooleanValue(false);

    @Entry
    public static final CCConfigValues.BooleanValue allowChestOnBabyWolf = new CCConfigValues.BooleanValue(false);

    /*
    @Entry(clientOnly = true)
    public static final CCConfigValues.BooleanValue showChestIconOnCats = new CCConfigValues.BooleanValue(false);

    @Entry(clientOnly = true)
    public static final CCConfigValues.BooleanValue showChestIconOnWolves = new CCConfigValues.BooleanValue(false);
*/
    public static void init() {
        if (!file.exists()) {
            save();
        } else {
            load();
        }
    }

    public static void save() {
        if (!file.getParentFile().isDirectory() && !file.getParentFile().mkdirs()) {
            ChestedCompanions.LOGGER.error("Failed to create config directory");
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            JsonObject jsonObject = new JsonObject();

            for (Field field : CCConfig.class.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Entry.class)) {
                    continue;
                }

                Object object = field.get(null);

                if (!(object instanceof CCConfigValues.Value<?> configValue)) {
                    continue;
                }

                jsonObject.add(field.getName(), configValue.write());
            }

            GSON.toJson(jsonObject, fileWriter);
        } catch (IOException e) {
            ChestedCompanions.LOGGER.error("Failed to save the Chested Companions config", e);
        } catch (IllegalAccessException e) {
            ChestedCompanions.LOGGER.error("Error while saving the Chested Companions config", e);
        }
    }

    public static void load() {
        if (!file.exists()) {
            return;
        }

        try (FileReader fileReader = new FileReader(file)) {
            JsonObject jsonObject = GSON.fromJson(fileReader, JsonObject.class);

            for (Field field : CCConfig.class.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Entry.class)) {
                    continue;
                }

                String fieldName = field.getName();

                if (!jsonObject.has(fieldName)) {
                    continue;
                }

                Object object = field.get(null);

                if (!(object instanceof CCConfigValues.Value<?> configValue)) {
                    continue;
                }

                JsonElement jsonElement = jsonObject.get(fieldName);
                configValue.setUnchecked(configValue.read(jsonElement));
            }
        } catch (IOException e) {
            ChestedCompanions.LOGGER.error("Failed to read the Chested Companions config", e);
        } catch (IllegalAccessException e) {
            ChestedCompanions.LOGGER.error("Error while reading the Chested Companions config", e);
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Entry {
        String category() default "general";
        boolean clientOnly() default false;
    }
}
