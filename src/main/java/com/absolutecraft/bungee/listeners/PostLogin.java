package com.absolutecraft.bungee.listeners;

import com.absolutecraft.bungee.APIClient;
import com.absolutecraft.bungee.AbsoluteBungee;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.json.JSONObject;

public class PostLogin implements Listener {

    final private AbsoluteBungee plugin;
    private APIClient client;

    public PostLogin(AbsoluteBungee plugin) {
        this.plugin = plugin;
        this.client = new APIClient(plugin);
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        JSONObject body = new JSONObject();
        body.put("uuid", player.getUniqueId());
        body.put("username", player.getName());
        
        this.client.post("/player/join", body, new Callback<JsonNode>() {
            public void cancelled() {}
            public void failed(UnirestException e) {
                System.out.println("API player/join failed");
            }

            public void completed(HttpResponse<JsonNode> response) {
                JSONObject data = response.getBody().getObject();

                System.out.println("Player " + data.get("id") + " - " + data.get("username") + " joined!");
            }
        });
    }
	
}
