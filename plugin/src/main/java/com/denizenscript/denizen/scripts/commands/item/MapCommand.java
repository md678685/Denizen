package com.denizenscript.denizen.scripts.commands.item;

import com.denizenscript.denizen.scripts.containers.core.MapScriptContainer;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizen.utilities.maps.DenizenMapManager;
import com.denizenscript.denizen.utilities.maps.DenizenMapRenderer;
import com.denizenscript.denizen.utilities.maps.MapAnimatedImage;
import com.denizenscript.denizen.utilities.maps.MapImage;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.objects.core.ScriptTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;
import com.denizenscript.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.List;

public class MapCommand extends AbstractCommand {

    // <--[command]
    // @Name Map
    // @Syntax map [<#>/new:<world>] [reset:<location>/image:<file> (resize)/script:<script>] (x:<#>) (y:<#>)
    // @Required 2
    // @Short Modifies a new or existing map by adding images or text.
    // @Group item
    //
    // @Description
    // This command modifies an existing map, or creates a new one. Using this will override existing
    // non-Denizen map renderers with Denizen's custom map renderer.
    // You can reset this at any time by using the 'reset:<location>' argument, which will remove all
    // images and texts on the map and show the default world map at the specified location.
    // Note that all maps have a size of 128x128.
    // The file path is relative to the 'plugins/Denizen/images/' folder.
    // Use escaping to let the image and text arguments have tags based on the player viewing the map.
    // Custom maps will persist over restarts using the 'maps.yml' save file in the Denizen plugins folder.
    //
    // @Tags
    // <entry[saveName].created_map> returns the map created by the 'new:' argument if used.
    //
    // @Usage
    // Use to add an auto-resized background image to map 3
    // - map 3 image:my_map_images/my_background.png resize
    //
    // @Usage
    // Use to add an image with the top-left corner at the center of a new map
    // - map new:WorldTag image:my_map_images/my_center_image.png x:64 y:64
    //
    // @Usage
    // Reset map to have the center at the player's location
    // - map 3 reset:<player.location>
    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        for (Argument arg : scriptEntry.getProcessedArgs()) {

            if (!scriptEntry.hasObject("new")
                    && arg.matchesPrefix("new")
                    && arg.matchesArgumentType(WorldTag.class)) {
                scriptEntry.addObject("new", arg.asType(WorldTag.class));
            }
            else if (!scriptEntry.hasObject("reset-loc")
                    && arg.matchesPrefix("r", "reset")
                    && arg.matchesArgumentType(LocationTag.class)) {
                scriptEntry.addObject("reset-loc", arg.asType(LocationTag.class));
                scriptEntry.addObject("reset", new ElementTag(true));
            }
            else if (!scriptEntry.hasObject("reset")
                    && arg.matches("reset")) {
                scriptEntry.addObject("reset", new ElementTag(true));
            }
            else if (!scriptEntry.hasObject("image")
                    && arg.matchesPrefix("i", "img", "image")) {
                scriptEntry.addObject("image", arg.asElement());
            }
            else if (!scriptEntry.hasObject("resize")
                    && arg.matches("resize")) {
                scriptEntry.addObject("resize", new ElementTag(true));
            }
            else if (!scriptEntry.hasObject("width")
                    && arg.matchesPrefix("width")
                    && arg.matchesPrimitive(ArgumentHelper.PrimitiveType.Integer)) {
                scriptEntry.addObject("width", arg.asElement());
            }
            else if (!scriptEntry.hasObject("height")
                    && arg.matchesPrefix("height")
                    && arg.matchesPrimitive(ArgumentHelper.PrimitiveType.Integer)) {
                scriptEntry.addObject("height", arg.asElement());
            }
            else if (!scriptEntry.hasObject("script")
                    && arg.matchesPrefix("s", "script")
                    && arg.matchesArgumentType(ScriptTag.class)) {
                scriptEntry.addObject("script", arg.asType(ScriptTag.class));
            }
            else if (!scriptEntry.hasObject("x-value")
                    && arg.matchesPrefix("x")
                    && arg.matchesPrimitive(ArgumentHelper.PrimitiveType.Double)) {
                scriptEntry.addObject("x-value", arg.asElement());
            }
            else if (!scriptEntry.hasObject("y-value")
                    && arg.matchesPrefix("y")
                    && arg.matchesPrimitive(ArgumentHelper.PrimitiveType.Double)) {
                scriptEntry.addObject("y-value", arg.asElement());
            }
            else if (!scriptEntry.hasObject("map-id")
                    && arg.matchesPrimitive(ArgumentHelper.PrimitiveType.Integer)) {
                scriptEntry.addObject("map-id", arg.asElement());
            }

        }

        if (!scriptEntry.hasObject("map-id") && !scriptEntry.hasObject("new")) {
            throw new InvalidArgumentsException("Must specify a map ID or create a new map!");
        }

        if (!scriptEntry.hasObject("reset")
                && !scriptEntry.hasObject("reset-loc")
                && !scriptEntry.hasObject("image")
                && !scriptEntry.hasObject("script")) {
            throw new InvalidArgumentsException("Must specify a valid action to perform!");
        }

        scriptEntry.defaultObject("reset", new ElementTag(false)).defaultObject("resize", new ElementTag(false))
                .defaultObject("x-value", new ElementTag(0)).defaultObject("y-value", new ElementTag(0));

    }

    @Override
    public void execute(ScriptEntry scriptEntry) {

        ElementTag id = scriptEntry.getElement("map-id");
        WorldTag create = scriptEntry.getObjectTag("new");
        ElementTag reset = scriptEntry.getElement("reset");
        LocationTag resetLoc = scriptEntry.getObjectTag("reset-loc");
        ElementTag image = scriptEntry.getElement("image");
        ScriptTag script = scriptEntry.getObjectTag("script");
        ElementTag resize = scriptEntry.getElement("resize");
        ElementTag width = scriptEntry.getElement("width");
        ElementTag height = scriptEntry.getElement("height");
        ElementTag x = scriptEntry.getElement("x-value");
        ElementTag y = scriptEntry.getElement("y-value");

        if (scriptEntry.dbCallShouldDebug()) {

            Debug.report(scriptEntry, getName(), (id != null ? id.debug() : "") + (create != null ? create.debug() : "")
                    + reset.debug() + (resetLoc != null ? resetLoc.debug() : "") + (image != null ? image.debug() : "")
                    + (script != null ? script.debug() : "") + resize.debug() + (width != null ? width.debug() : "")
                    + (height != null ? height.debug() : "") + x.debug() + y.debug());

        }

        MapView map = null;
        if (create != null) {
            map = Bukkit.getServer().createMap(create.getWorld());
            scriptEntry.addObject("created_map", new ElementTag(map.getId()));
        }
        else if (id != null) {
            map = Bukkit.getServer().getMap((short) id.asInt());
            if (map == null) {
                Debug.echoError("No map found for ID '" + id.asInt() + "'!");
                return;
            }
        }
        else {
            Debug.echoError("The map command failed somehow! Report this to a developer!");
            return;
        }

        if (reset.asBoolean()) {
            List<MapRenderer> oldRenderers = DenizenMapManager.removeDenizenRenderers(map);
            for (MapRenderer renderer : oldRenderers) {
                map.addRenderer(renderer);
            }
            if (resetLoc != null) {
                map.setCenterX(resetLoc.getBlockX());
                map.setCenterZ(resetLoc.getBlockZ());
                map.setWorld(resetLoc.getWorld());
            }
        }
        else if (script != null) {
            DenizenMapManager.removeDenizenRenderers(map);
            ((MapScriptContainer) script.getContainer()).applyTo(map);
        }
        else {
            DenizenMapRenderer dmr = DenizenMapManager.getDenizenRenderer(map);
            if (image != null) {
                int wide = width != null ? width.asInt() : resize.asBoolean() ? 128 : 0;
                int high = height != null ? height.asInt() : resize.asBoolean() ? 128 : 0;
                if (CoreUtilities.toLowerCase(image.asString()).endsWith(".gif")) {
                    dmr.autoUpdate = true;
                    dmr.addObject(new MapAnimatedImage(x.asString(), y.asString(), "true", false, image.asString(),
                            wide, high));
                }
                else {
                    dmr.addObject(new MapImage(x.asString(), y.asString(), "true", false, image.asString(),
                            wide, high));
                }
            }
        }

    }
}
