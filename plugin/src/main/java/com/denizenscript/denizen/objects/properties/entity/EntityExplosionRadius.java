package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Explosive;

public class EntityExplosionRadius implements Property {

    public static boolean describes(ObjectTag entity) {
        return entity instanceof EntityTag
                && (((EntityTag) entity).getBukkitEntity() instanceof Explosive
                || ((EntityTag) entity).getBukkitEntity() instanceof Creeper);
    }

    public static EntityExplosionRadius getFrom(ObjectTag entity) {
        return describes(entity) ? new EntityExplosionRadius((EntityTag) entity) : null;
    }

    public static final String[] handledTags = new String[] {
            "explosion_radius"
    };

    public static final String[] handledMechs = new String[] {
            "explosion_radius"
    };

    public float getExplosionRadius() {
        if (entity.getBukkitEntity() instanceof Creeper) {
            return ((Creeper) entity.getBukkitEntity()).getExplosionRadius();
        }
        return ((Explosive) entity.getBukkitEntity()).getYield();
    }

    ///////////////////
    // Instance Fields and Methods
    /////////////

    private EntityExplosionRadius(EntityTag ent) {
        entity = ent;
    }

    EntityTag entity;

    /////////
    // Property Methods
    ///////

    @Override
    public String getPropertyString() {
        return String.valueOf(getExplosionRadius());
    }

    @Override
    public String getPropertyId() {
        return "explosion_radius";
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
        // @attribute <EntityTag.explosion_radius>
        // @returns ElementTag(Decimal)
        // @mechanism EntityTag.explosion_radius
        // @group properties
        // @description
        // If this entity can explode, returns its explosion radius.
        // -->
        if (attribute.startsWith("explosion_radius")) {
            return new ElementTag(getExplosionRadius())
                    .getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object EntityTag
        // @name explosion_radius
        // @input Element(Decimal)
        // @description
        // If this entity can explode, sets its explosion radius.
        // @tags
        // <EntityTag.explosion_radius>
        // -->
        if (mechanism.matches("explosion_radius") && mechanism.requireFloat()) {
            if (entity.getBukkitEntity() instanceof Creeper) {
                ((Creeper) entity.getBukkitEntity()).setExplosionRadius(mechanism.getValue().asInt());
            }
            else {
                ((Explosive) entity.getBukkitEntity()).setYield(mechanism.getValue().asFloat());
            }
        }
    }
}
