package me.pk2.adminsecure.user.ip;

import me.pk2.adminsecure.user.ip.object.IPObject;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class IPWhitelister {
    private final ArrayList<IPObject> verified;
    public IPWhitelister() {
        this.verified = new ArrayList<>();
    }

    public IPObject get(Player player) {
        IPObject object = new IPObject(player);
        for(IPObject ipObject : verified)
            if(ipObject.address.contentEquals(object.address))
                return ipObject;
        return null;
    }
    public void put(Player player) {
        if(get(player) != null)
            return;
        verified.add(new IPObject(player));
    }
    public void rem(Player player) {
        if(get(player) == null)
            return;
        IPObject object = new IPObject(player);
        verified.removeIf(ipObject -> ipObject.address.contentEquals(object.address));
    }
    public void shutdown() { verified.clear(); }
}