package com.absolutecraft.bungee;

import com.google.common.collect.ListMultimap;

public class Response {
	
	private ListMultimap<String, String> data;
	
	public Response(ListMultimap<String, String> data) {
		this.data = data;
	}
	
	public ListMultimap<String, String> get() {
		return this.data;
	}
	
}
