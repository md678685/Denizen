package com.denizenscript.denizen.objects.properties.entity;

import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;

public class EntityInvulnerable implements Property {

    public static boolean describes(ObjectTag entity) {
        return entity instanceof EntityTag;
    }

    public static EntityInvulnerable getFrom(ObjectTag entity) {
        if (!describes(entity)) {
            return null;
        }
        else {
            return new EntityInvulnerable((EntityTag) entity);
        }
    }

    public static final String[] handledTags = new String[] {
            "invulnerable"
    };

    public static final String[] handledMechs = new String[] {
            "invulnerable"
    };


    ///////////////////
    // Instance Fields and Methods
    /////////////

    private EntityInvulnerable(EntityTag entity) {
        dentity = entity;
    }

    EntityTag dentity;

    /////////
    // Property Methods
    ///////

    @Override
    public String getPropertyString() {
        if (!dentity.getBukkitEntity().isInvulnerable()) {
            return null;
        }
        else {
            return "true";
        }
    }

    @Override
    public String getPropertyId() {
        return "invulnerable";
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
        // @attribute <EntityTag.invulnerable>
        // @returns ElementTag(Boolean)
        // @mechanism EntityTag.invulnerable
        // @group properties
        // @description
        // Returns whether the entity is invulnerable (cannot be damaged).
        // -->
        if (attribute.startsWith("invulnerable")) {
            return new ElementTag(dentity.getBukkitEntity().isInvulnerable())
                    .getAttribute(attribute.fulfill(1));
        }

        return null;
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object EntityTag
        // @name invulnerable
        // @input Element(Boolean)
        // @description
        // Sets whether the entity is invulnerable (cannot be damaged).
        // @tags
        // <EntityTag.invulnerable>
        // -->

        if (mechanism.matches("invulnerable") && mechanism.requireBoolean()) {
            dentity.getBukkitEntity().setInvulnerable(mechanism.getValue().asBoolean());
        }
    }
}
