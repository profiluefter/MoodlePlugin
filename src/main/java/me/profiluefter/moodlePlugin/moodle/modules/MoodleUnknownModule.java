package me.profiluefter.moodlePlugin.moodle.modules;

import org.json.JSONObject;

public class MoodleUnknownModule extends MoodleModule {
	private String moduleName;

	public MoodleUnknownModule(JSONObject data) {
		super(data);
		this.moduleName = data.getString("modname");
	}

	public String getModuleName() {
		return moduleName;
	}

	@Override
	public String toString() {
		return String.format("%s\nModule Name: %s", super.toString(), getModuleName());
	}
}
