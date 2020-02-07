package me.profiluefter.moodlePlugin.moodle;

import me.profiluefter.moodlePlugin.moodle.modules.MoodleAssignModule;
import me.profiluefter.moodlePlugin.moodle.modules.MoodleModule;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

public class MoodleCourse {
	private final int id;
	private final String shortName;
	private final String fullName;
	private final Map<Integer, MoodleSection> sections;

	MoodleCourse(int id, JSONArray courseData, JSONObject assignData) {
		this.id = id;
		this.sections = new TreeMap<>();
		for(Object sectionRaw : courseData) {
			assert sectionRaw instanceof JSONObject;
			JSONObject sectionObject = (JSONObject) sectionRaw;
			sections.put(sectionObject.getInt("section"), new MoodleSection(sectionObject));
		}

		JSONArray courses = assignData.getJSONArray("courses");
		JSONObject courseAssignData = courses.getJSONObject(0);

		this.shortName = courseAssignData.getString("shortname");
		this.fullName = courseAssignData.getString("fullname");

		JSONArray assignments = courseAssignData.getJSONArray("assignments");

		for(Object rawAssignment : assignments) {
			JSONObject assignmentData = ((JSONObject) rawAssignment);

			MoodleModule module = getModuleByID(assignmentData.getInt("cmid"));
			assert module instanceof MoodleAssignModule;
			MoodleAssignModule assignmentModule = ((MoodleAssignModule) module);

			assignmentModule.setAssignmentData(assignmentData);
		}
	}

	private MoodleModule getModuleByID(int id) {
		return sections.values().stream()
				.flatMap(section -> section.getModules().stream())
				.filter(module -> module.getId() == id)
				.findAny().orElseThrow();
	}

	public Map<Integer, MoodleSection> getSections() {
		return sections;
	}

	public String getShortName() {
		return shortName;
	}

	public String getFullName() {
		return fullName;
	}
}
