package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.utilities.MaterialCompat;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class EntityFirework implements Property {

    public static boolean describes(ObjectTag entity) {
        return entity instanceof EntityTag && ((EntityTag) entity).getBukkitEntityType() == EntityType.FIREWORK;
    }

    public static EntityFirework getFrom(ObjectTag entity) {
        if (!describes(entity)) {
            return null;
        }
        else {
            return new EntityFirework((EntityTag) entity);
        }
    }

    public static final String[] handledTags = new String[] {
            "firework_item"
    };

    public static final String[] handledMechs = new String[] {
            "firework_item", "detonate"
    };


    ///////////////////
    // Instance Fields and Methods
    /////////////

    private EntityFirework(EntityTag entity) {
        firework = entity;
    }

    EntityTag firework;

    /////////
    // Property Methods
    ///////

    @Override
    public String getPropertyString() {
        ItemStack item = new ItemStack(MaterialCompat.FIREWORK_ROCKET);
        item.setItemMeta(((Firework) firework.getBukkitEntity()).getFireworkMeta());
        return new ItemTag(item).identify();
    }

    @Override
    public String getPropertyId() {
        return "firework_item";
    }

    ///////////
    // ObjectTag Attributes
    ////////

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <EntityTag.firework_item>
        // @returns ItemTag
        // @mechanism EntityTag.firework_item
        // @group properties
        // @description
        // If the entity is a firework, returns the firework item used to launch it.
        // -->
        if (attribute.startsWith("firework_item")) {
            ItemStack item = new ItemStack(MaterialCompat.FIREWORK_ROCKET);
            item.setItemMeta(((Firework) firework.getBukkitEntity()).getFireworkMeta());
            return new ItemTag(item).getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object EntityTag
        // @name firework_item
        // @input ItemTag
        // @description
        // Changes the firework effect on this entity, using a firework item.
        // @tags
        // <EntityTag.firework_item>
        // -->
        if (mechanism.matches("firework_item") && mechanism.requireObject(ItemTag.class)) {
            ItemTag item = mechanism.valueAsType(ItemTag.class);
            if (item != null && item.getItemStack().getItemMeta() instanceof FireworkMeta) {
                ((Firework) firework.getBukkitEntity()).setFireworkMeta((FireworkMeta) item.getItemStack().getItemMeta());
            }
            else {
                Debug.echoError("'" + mechanism.getValue().asString() + "' is not a valid firework item.");
            }
        }

        // <--[mechanism]
        // @object EntityTag
        // @name detonate
        // @input None
        // @description
        // If the entity is a firework, detonates it.
        // @tags
        // <EntityTag.firework_item>
        // -->
        if (mechanism.matches("detonate")) {
            ((Firework) firework.getBukkitEntity()).detonate();
        }
    }
}
