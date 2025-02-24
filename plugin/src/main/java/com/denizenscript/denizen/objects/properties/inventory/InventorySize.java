package com.denizenscript.denizen.objects.properties.inventory;

import com.denizenscript.denizen.objects.InventoryTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;

public class InventorySize implements Property {

    public static boolean describes(ObjectTag inventory) {
        // All inventories should have a size
        return inventory instanceof InventoryTag;
    }

    public static InventorySize getFrom(ObjectTag inventory) {
        if (!describes(inventory)) {
            return null;
        }
        return new InventorySize((InventoryTag) inventory);
    }

    public static final String[] handledTags = new String[] {
            "size"
    };

    public static final String[] handledMechs = new String[] {
            "size"
    };

    ///////////////////
    // Instance Fields and Methods
    /////////////

    InventoryTag inventory;

    public InventorySize(InventoryTag inventory) {
        this.inventory = inventory;
    }

    public int getSize() {
        if (inventory.getInventory() == null) {
            return 0;
        }
        return inventory.getInventory().getSize();
    }

    public void setSize(int size) {
        inventory.setSize(size);
    }


    /////////
    // Property Methods
    ///////

    @Override
    public String getPropertyString() {
        if (getSize() > 0 && inventory.getIdType().equals("generic")
                && inventory.getIdHolder().equals("CHEST")) {
            return String.valueOf(getSize());
        }
        else {
            return null;
        }
    }

    @Override
    public String getPropertyId() {
        return "size";
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
        // @attribute <InventoryTag.size>
        // @returns ElementTag(Number)
        // @group properties
        // @mechanism InventoryTag.size
        // @description
        // Return the number of slots in the inventory.
        // -->
        if (attribute.startsWith("size")) {
            return new ElementTag(getSize())
                    .getAttribute(attribute.fulfill(1));
        }

        return null;

    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object InventoryTag
        // @name size
        // @input Element(Number)
        // @description
        // Sets the size of the inventory. (Only works for "generic" chest inventories.)
        // @tags
        // <InventoryTag.size>
        // -->
        if (mechanism.matches("size") && inventory.getIdType().equals("generic") && mechanism.requireInteger()) {
            setSize(mechanism.getValue().asInt());
        }

    }
}
