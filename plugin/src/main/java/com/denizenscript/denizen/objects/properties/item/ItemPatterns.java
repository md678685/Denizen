package com.denizenscript.denizen.objects.properties.item;

import com.denizenscript.denizen.utilities.MaterialCompat;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizen.objects.ItemTag;
import com.denizenscript.denizencore.objects.Mechanism;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.properties.Property;
import com.denizenscript.denizencore.tags.Attribute;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemPatterns implements Property {

    public static boolean describes(ObjectTag item) {
        if (item instanceof ItemTag) {
            Material material = ((ItemTag) item).getItemStack().getType();
            return MaterialCompat.isBannerOrShield(material);
        }
        return false;
    }

    public static ItemPatterns getFrom(ObjectTag item) {
        if (!describes(item)) {
            return null;
        }
        else {
            return new ItemPatterns((ItemTag) item);
        }
    }

    public static final String[] handledTags = new String[] {
            "patterns"
    };

    public static final String[] handledMechs = new String[] {
            "patterns"
    };


    private ItemPatterns(ItemTag item) {
        this.item = item;
    }

    ItemTag item;

    private ListTag listPatterns() {
        ListTag list = new ListTag();
        for (Pattern pattern : getPatterns()) {
            list.add(pattern.getColor().name() + "/" + pattern.getPattern().name());
        }
        return list;
    }

    private List<Pattern> getPatterns() {
        ItemMeta itemMeta = item.getItemStack().getItemMeta();
        if (itemMeta instanceof BannerMeta) {
            return ((BannerMeta) itemMeta).getPatterns();
        }
        else if (itemMeta instanceof BlockStateMeta) {
            return ((Banner) ((BlockStateMeta) itemMeta).getBlockState()).getPatterns();
        }
        else {
            // ...???
            return new ArrayList<>();
        }
    }

    private void setPatterns(List<Pattern> patterns) {
        ItemStack itemStack = item.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta instanceof BannerMeta) {
            ((BannerMeta) itemMeta).setPatterns(patterns);
        }
        else if (itemMeta instanceof BlockStateMeta) {
            try {
                Banner banner = (Banner) ((BlockStateMeta) itemMeta).getBlockState();
                banner.setPatterns(patterns);
                banner.update();
                ((BlockStateMeta) itemMeta).setBlockState(banner);
            }
            catch (Exception ex) {
                Debug.echoError("Banner setPatterns failed!");
                Debug.echoError(ex);
            }
        }
        else {
            // ...???
        }
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public String getAttribute(Attribute attribute) {

        if (attribute == null) {
            return null;
        }

        // <--[tag]
        // @attribute <ItemTag.patterns>
        // @returns ListTag
        // @group properties
        // @mechanism ItemTag.patterns
        // @description
        // Lists a banner's patterns in the form "li@COLOR/PATTERN|COLOR/PATTERN" etc.
        // TODO: Local meta for these links
        // For the list of possible colors, see <@link url http://bit.ly/1dydq12>.
        // For the list of possible patterns, see <@link url http://bit.ly/1MqRn7T>.
        // -->
        if (attribute.startsWith("patterns")) {
            return listPatterns().getAttribute(attribute.fulfill(1));
        }

        return null;
    }


    @Override
    public String getPropertyString() {
        ListTag list = listPatterns();
        if (list.isEmpty()) {
            return null;
        }
        return list.identify();
    }

    @Override
    public String getPropertyId() {
        return "patterns";
    }

    @Override
    public void adjust(Mechanism mechanism) {

        // <--[mechanism]
        // @object ItemTag
        // @name patterns
        // @input ListTag
        // @description
        // Changes the patterns of a banner. Input must be in the form
        // "li@COLOR/PATTERN|COLOR/PATTERN" etc.
        // For the list of possible colors, see <@link url http://bit.ly/1dydq12>.
        // For the list of possible patterns, see <@link url http://bit.ly/1MqRn7T>.
        // @tags
        // <ItemTag.patterns>
        // <server.list_patterns>
        // -->

        if (mechanism.matches("patterns")) {
            List<Pattern> patterns = new ArrayList<>();
            ListTag list = mechanism.valueAsType(ListTag.class);
            List<String> split;
            for (String string : list) {
                try {
                    split = CoreUtilities.split(string, '/', 2);
                    patterns.add(new Pattern(DyeColor.valueOf(split.get(0).toUpperCase()),
                            PatternType.valueOf(split.get(1).toUpperCase())));
                }
                catch (Exception e) {
                    Debug.echoError("Could not apply pattern to banner: " + string);
                }
            }
            setPatterns(patterns);
        }
    }
}
