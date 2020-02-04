package me.profiluefter.moodlePlugin.moodle.modules;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

abstract class MoodleModuleWithURL extends MoodleModule {
	private final URL url;

	MoodleModuleWithURL(JSONObject data) {
		super(data);
		try {
			this.url = new URL(data.getString("url"));
		} catch(MalformedURLException e) {
			throw new RuntimeException("Error while parsing URL", e);
		}
	}

	public URL getUrl() {
		return url;
	}
}
