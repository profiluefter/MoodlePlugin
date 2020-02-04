package me.profiluefter.moodlePlugin.moodle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoodleCourse {
	List<MoodleSection> sections;

	MoodleCourse(JSONArray data) {
		sections = new ArrayList<>(data.length());
		for(Object sectionRaw : data) {
			assert sectionRaw instanceof JSONObject;
			JSONObject sectionObject = (JSONObject) sectionRaw;
			sections.set(sectionObject.getInt("section"), new MoodleSection(sectionObject));
		}
	}
}
