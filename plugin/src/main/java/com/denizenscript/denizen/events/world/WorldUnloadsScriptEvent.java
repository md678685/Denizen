package com.denizenscript.denizen.events.world;

import com.denizenscript.denizen.objects.WorldTag;
import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldUnloadsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // world unloads
    // <world> unloads
    //
    // @Regex ^on [^\s]+ unloads$
    //
    // @Group World
    //
    // @Cancellable true
    //
    // @Triggers when a world is unloaded.
    //
    // @Context
    // <context.world> returns the WorldTag that was unloaded.
    //
    // -->

    public WorldUnloadsScriptEvent() {
        instance = this;
    }

    public static WorldUnloadsScriptEvent instance;
    public WorldTag world;
    public WorldUnloadEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        if (path.eventArgLowerAt(0).equals("chunk")) {
            return false;
        }
        return path.eventArgLowerAt(1).equals("saves");
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (!runGenericCheck(path.eventArgAt(0), world.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "WorldUnloads";
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("world")) {
            return world;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onWorldUnloads(WorldUnloadEvent event) {
        world = new WorldTag(event.getWorld());
        this.event = event;
        fire(event);
    }
}
