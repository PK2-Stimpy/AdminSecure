package me.pk2.adminsecure.user.ip.object;

import org.bukkit.entity.Player;

public class IPObject {
    public final String address;
    public final int port;
    public IPObject(String address) {
        if(address.contains("/")) {
            String[] split = address.split("/");
            this.address = split[0];
            this.port = Integer.parseInt(split[1]);
            return;
        }

        this.address = address;
        this.port = 0;
    }
    public IPObject(Player player) { this(player.getAddress().getHostName()); }
}