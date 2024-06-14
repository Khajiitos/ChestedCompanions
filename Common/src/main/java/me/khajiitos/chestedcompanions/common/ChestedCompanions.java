package me.khajiitos.chestedcompanions.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChestedCompanions {
    public static final String MOD_ID = "chestedcompanions";
    public static final Logger LOGGER = LoggerFactory.getLogger("Chested Companions");
    public static final TagKey<Item> PET_CHEST_ITEM = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ChestedCompanions.MOD_ID, "pet_chest_item"));
}
