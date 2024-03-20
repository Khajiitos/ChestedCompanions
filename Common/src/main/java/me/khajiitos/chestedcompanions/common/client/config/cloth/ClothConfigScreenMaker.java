package me.khajiitos.chestedcompanions.common.client.config.cloth;

import me.khajiitos.chestedcompanions.common.ChestedCompanions;
import me.khajiitos.chestedcompanions.common.config.CCConfig;
import me.khajiitos.chestedcompanions.common.config.CCConfigValues;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.lang.reflect.Field;
import java.util.Locale;

public class ClothConfigScreenMaker {

    public static Screen create(Minecraft minecraft, Screen parent) {
        return create(parent);
    }

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TranslatableComponent("chestedcompanions.config.header"))
                .setSavingRunnable(CCConfig::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        for (Field field : CCConfig.class.getDeclaredFields()) {
            addEntryForField(field, builder, entryBuilder);
        }

        return builder.build();
    }

    public static void addEntryForField(Field field, ConfigBuilder configBuilder, ConfigEntryBuilder entryBuilder) {
        CCConfig.Entry annotation = field.getAnnotation(CCConfig.Entry.class);

        if (annotation == null) {
            return;
        }

        ConfigCategory category = configBuilder.getOrCreateCategory(new TranslatableComponent("chestedcompanions.config.category." + annotation.category()));

        try {
            String fieldName = field.getName();
            Component name = new TranslatableComponent(String.format("chestedcompanions.config.%s.name", fieldName));
            Component description = new TranslatableComponent(String.format("chestedcompanions.config.%s.description", fieldName));

            if (field.get(null) instanceof CCConfigValues.BooleanValue booleanValue) {
                category.addEntry(entryBuilder.startBooleanToggle(name, booleanValue.get())
                        .setTooltip(description)
                        .setDefaultValue(booleanValue.getDefault())
                        .setSaveConsumer(booleanValue::set)
                        .build());
            } else if (field.get(null) instanceof CCConfigValues.EnumValue<? extends Enum<?>> enumValue) {
                category.addEntry(entryBuilder.startEnumSelector(name, (Class<Enum<?>>)enumValue.get().getClass(), enumValue.get())
                        .setEnumNameProvider(anEnum -> new TranslatableComponent(String.format("chestedcompanions.config.%s.value.%s", fieldName, anEnum.toString().toLowerCase(Locale.ROOT))))
                        .setTooltip(description)
                        .setDefaultValue(enumValue.getDefault())
                        .setSaveConsumer(enumValue::setUnchecked)
                        .build());
            }
        } catch (IllegalAccessException e) {
            ChestedCompanions.LOGGER.error("Failed to access a field", e);
        }
    }
}
