package me.profiluefter.moodlePlugin.moodle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class MoodleCourse {
	private final Map<Integer, MoodleSection> sections;

	MoodleCourse(JSONArray data) {
		sections = new TreeMap<>();
		for(Object sectionRaw : data) {
			assert sectionRaw instanceof JSONObject;
			JSONObject sectionObject = (JSONObject) sectionRaw;
			sections.put(sectionObject.getInt("section"), new MoodleSection(sectionObject));
		}
	}

	public Map<Integer, MoodleSection> getSections() {
		return sections;
	}
}
