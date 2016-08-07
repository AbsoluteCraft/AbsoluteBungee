package com.absolutecraft.bungee;

import java.util.Map;
import java.util.logging.Level;
import java.util.concurrent.Future;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.md_5.bungee.api.plugin.Plugin;

import org.json.JSONObject;

public class APIClient {
	
	private Plugin plugin;
	
	private String baseUrl = "http://local.ac/api";
	private String apiKey = null;

	public APIClient(AbsoluteBungee plugin) {
		this.plugin = plugin;
		
		this.apiKey = plugin.getConfig().getString("api_key");
		
		if(this.apiKey == null) {
			this.plugin.onDisable();
		}
	}

    /**
     * Make an async get request
     *
     * @param url
     * @param body
     * @param callback
     * @return
     */
    public Future<HttpResponse<JsonNode>> get(String url, JSONObject body, Callback<JsonNode> callback) {
        return Unirest.post(this.buildUrl(url))
                .body(body)
                .asJsonAsync(callback);
    }

    /**
     * Make an async post request
     *
     * @param url
     * @param body
     * @param callback
     * @return
     */
    public Future<HttpResponse<JsonNode>> post(String url, JSONObject body, Callback<JsonNode> callback) {
        return Unirest.post(this.buildUrl(url))
                .body(body)
                .asJsonAsync(callback);
    }

    /**
     * Make an async post request where we don't care about the response
     *
     * @param url
     * @param body
     * @return
     */
    public Future<HttpResponse<JsonNode>> post(String url, JSONObject body) {
        return Unirest.post(this.buildUrl(url))
                .body(body)
                .asJsonAsync(new Callback<JsonNode>() {
                    public void completed(HttpResponse<JsonNode> httpResponse) {}
                    public void failed(UnirestException e) {}
                    public void cancelled() {}
                });
    }

    @Deprecated
    public JSONObject getSync(String url, Map<String, Object> parameters) {
        try {
            HttpResponse<JsonNode> response = Unirest.get(this.buildUrl(url))
                    .queryString(parameters)
                    .asJson();

            if(response.getStatus() == 200) {
                return response.getBody().getObject();
            } else {
                this.plugin.getLogger().log(Level.WARNING, "API GET request bad response", response.getBody().getObject());
            }
        } catch(UnirestException e) {
            this.plugin.getLogger().log(Level.WARNING, "API GET request error", e);
        }

        return null;
    }

    @Deprecated
	public JSONObject postSync(String url, JSONObject body) {
        try {
            HttpResponse<JsonNode> response = Unirest.post(this.buildUrl(url))
                    .body(body)
                    .asJson();

            if(response.getStatus() == 200) {
                return response.getBody().getObject();
            } else {
                this.plugin.getLogger().log(Level.WARNING, "API POST request bad response", response.getBody().getObject());
            }
        } catch(UnirestException e) {
            this.plugin.getLogger().log(Level.WARNING, "API POST request error", e);
        }

        return null;
    }

    private String buildUrl(String url) {
        if(baseUrl.charAt(0) != '/') {
            url = "/" + url;
        }

        return this.baseUrl + url;
    }
}
