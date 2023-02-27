package br.com.nyexgaming.mc.spigot.database.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class CitizenModel {

    private final long id, createdAt;
    private final Location location;

    public CitizenModel(long id, long createdAt, Location location) {
        this.id = id;
        this.createdAt = createdAt;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public Location getLocation() {
        if (Bukkit.getPluginManager().isPluginEnabled("Citizens")) {
            net.citizensnpcs.api.npc.NPC npc = net.citizensnpcs.api.CitizensAPI.getNPCRegistry().getById((int) this.id);

            if (npc != null) {
                return npc.getStoredLocation();
            }
        }

        return location;
    }
}
