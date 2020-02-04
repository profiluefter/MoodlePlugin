package me.profiluefter.moodlePlugin.moodle.modules;

import org.json.JSONObject;

public class MoodleLabelModule extends MoodleModule {
	private final String description;

	public MoodleLabelModule(JSONObject data) {
		super(data);
		this.description = data.getString("description");
	}

	public String getDescription() {
		return description;
	}
}
