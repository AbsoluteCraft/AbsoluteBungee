package com.absolutecraft.bungee;

import java.util.concurrent.Future;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import net.md_5.bungee.api.plugin.Plugin;

import org.json.JSONObject;

public class APIClient {
	
	private Plugin plugin;
	
	private String baseUrl = "https://api.absolutecraft.co.uk";
	private String apiKey = null;

	public APIClient(AbsoluteBungee plugin) {
		this.plugin = plugin;
		
		this.apiKey = plugin.getConfig().getString("api_key");
        if(this.apiKey == null) {
            this.plugin.onDisable();
        }

        String baseUrl = plugin.getConfig().getString("api_baseurl");
        if(baseUrl != null) {
            this.baseUrl = baseUrl;
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
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
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
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
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

    private String buildUrl(String url) {
        if(url.charAt(0) != '/') {
            url = "/" + url;
        }

        return this.baseUrl + url;
    }
}
