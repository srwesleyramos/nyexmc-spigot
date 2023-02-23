package br.com.nyexgaming.mc.spigot.storage.events;

import br.com.nyexgaming.mc.spigot.storage.Storage;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CitizensEvents implements Listener {

    private final Storage storage;

    public CitizensEvents(Storage storage) {
        this.storage = storage;
    }

    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent e) {
        if (!storage.service.isNpcsActivated()) return;

        if (storage.npcs.containsKey(e.getNPC().getId())) {
            storage.createViewer(e.getClicker(), e.getClicker().getName());
        }
    }
}
