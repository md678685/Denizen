package com.denizenscript.denizen.scripts.commands.entity;

import com.denizenscript.denizen.utilities.Conversion;
import com.denizenscript.denizen.utilities.Utilities;
import com.denizenscript.denizen.utilities.debugging.Debug;
import com.denizenscript.denizen.utilities.entity.Position;
import com.denizenscript.denizen.objects.EntityTag;
import com.denizenscript.denizen.objects.LocationTag;
import com.denizenscript.denizencore.exceptions.InvalidArgumentsException;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ArgumentHelper;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.denizencore.scripts.ScriptEntry;
import com.denizenscript.denizencore.scripts.commands.AbstractCommand;

import java.util.List;

public class MountCommand extends AbstractCommand {

    // <--[command]
    // @Name Mount
    // @Syntax mount (cancel) [<entity>|...] (<location>)
    // @Required 0
    // @Short Mounts one entity onto another.
    // @Group entity
    //
    // @Description
    // Mounts an entity onto another as though in a vehicle. Can be used to force a player into a vehicle or to
    // mount an entity onto another entity. e.g. a player onto an npc. If the entity(s) don't exist they will be
    // spawned. Accepts a location, which the entities will be teleported to on mounting.
    //
    // @Tags
    // <EntityTag.vehicle>
    // <EntityTag.inside_vehicle>
    // <entry[saveName].mounted_entities> returns a list of entities that were mounted.
    //
    // @Usage
    // Use to mount an NPC on top of a player.
    // - mount <npc>|<player>
    //
    // @Usage
    // Use to spawn a mutant pile of mobs.
    // - mount cow|pig|sheep|chicken
    //
    // @Usage
    // Use to place a diamond block above a player's head.
    // - mount falling_block,diamond_block|<player>
    //
    // @Usage
    // Use to force an entity in a vehicle.
    // - mount <player>|boat
    // -->

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

        // Initialize necessary fields

        List<EntityTag> entities = null;

        for (Argument arg : scriptEntry.getProcessedArgs()) {

            if (!scriptEntry.hasObject("cancel")
                    && arg.matches("cancel")) {

                scriptEntry.addObject("cancel", "");
            }
            else if (!scriptEntry.hasObject("location")
                    && arg.matchesArgumentType(LocationTag.class)) {
                // Location arg
                scriptEntry.addObject("location", arg.asType(LocationTag.class));
            }
            else if (!scriptEntry.hasObject("entities")
                    && arg.matchesArgumentList(EntityTag.class)) {
                // Entity arg
                entities = arg.asType(ListTag.class).filter(EntityTag.class, scriptEntry);
                scriptEntry.addObject("entities", entities);
            }
            else {
                arg.reportUnhandled();
            }
        }

        if (!scriptEntry.hasObject("entities")) {
            throw new InvalidArgumentsException("Must specify entity/entities!");
        }

        if (!scriptEntry.hasObject("location")) {
            if (entities != null) {
                for (int i = entities.size() - 1; i >= 0; i--) {
                    if (entities.get(i).isSpawned()) {
                        scriptEntry.defaultObject("location", entities.get(i).getLocation());
                        break;
                    }
                }
            }
            scriptEntry.defaultObject("location",
                    Utilities.entryHasPlayer(scriptEntry) ? Utilities.getEntryPlayer(scriptEntry).getLocation() : null,
                    Utilities.entryHasNPC(scriptEntry) ? Utilities.getEntryNPC(scriptEntry).getLocation() : null);
        }

        if (!scriptEntry.hasObject("location")) {
            throw new InvalidArgumentsException("Must specify a location!");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute(final ScriptEntry scriptEntry) {
        // Get objects

        LocationTag location = (LocationTag) scriptEntry.getObject("location");
        List<EntityTag> entities = (List<EntityTag>) scriptEntry.getObject("entities");
        boolean cancel = scriptEntry.hasObject("cancel");

        // Report to dB
        if (scriptEntry.dbCallShouldDebug()) {
            Debug.report(scriptEntry, getName(), (cancel ? ArgumentHelper.debugObj("cancel", cancel) : "") +
                    ArgumentHelper.debugObj("location", location) +
                    ArgumentHelper.debugObj("entities", entities.toString()));
        }

        // Mount or dismount all of the entities
        if (!cancel) {

            // Go through all the entities, spawning/teleporting them
            for (EntityTag entity : entities) {
                entity.spawnAt(location);
            }

            Position.mount(Conversion.convertEntities(entities));
        }
        else {
            Position.dismount(Conversion.convertEntities(entities));
        }

        ListTag entityList = new ListTag();
        entityList.addObjects((List) entities);
        scriptEntry.addObject("mounted_entities", entityList);
    }
}
