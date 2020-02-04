package me.profiluefter.moodlePlugin.moodle;

import me.profiluefter.moodlePlugin.moodle.modules.MoodleModule;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoodleSection {
	private int id;
	private String name;
	private List<MoodleModule> modules;

	public MoodleSection(JSONObject data) {
		this.id = data.getInt("id");
		this.name = data.getString("name");
		JSONArray modulesRaw = data.getJSONArray("modules");
		this.modules = new ArrayList<>(modulesRaw.length());
		for(Object moduleRaw : modulesRaw) {
			assert moduleRaw instanceof JSONObject;
			JSONObject module = (JSONObject) moduleRaw;
			modules.add(MoodleModule.parseModule(module));
		}
	}
}
