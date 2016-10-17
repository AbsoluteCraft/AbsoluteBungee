package com.absolutecraft.bungee.listeners;

import com.absolutecraft.bungee.APIClient;
import com.absolutecraft.bungee.AbsoluteBungee;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.json.JSONObject;

import java.util.logging.Level;

public class PlayerDisconnect implements Listener {

    final private AbsoluteBungee plugin;
    private APIClient client;

    public PlayerDisconnect(AbsoluteBungee plugin) {
        this.plugin = plugin;
        this.client = new APIClient(plugin);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        JSONObject body = new JSONObject();
        body.put("uuid", player.getUniqueId());

        this.client.post("/player/leave", body, new Callback<JsonNode>() {
            public void cancelled() {}
            public void failed(UnirestException e) {
            	plugin.getLogger().log(Level.SEVERE, "API player/leave failed");
            }

            public void completed(HttpResponse<JsonNode> response) {
                JSONObject data = response.getBody().getObject();

                plugin.getLogger().log(Level.INFO, "Player " + data.get("id") + " - " + data.get("username") + " left!");
            }
        });
    }

}
