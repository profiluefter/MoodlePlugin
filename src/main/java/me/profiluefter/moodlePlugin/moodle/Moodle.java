package me.profiluefter.moodlePlugin.moodle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Moodle {
	private final Map<Integer, MoodleCourse> courses;

	private final MoodleHost host;
	private final MoodleToken token;

	Moodle(MoodleHost host, MoodleToken token) {
		this.courses = new HashMap<>();

		this.host = host;
		this.token = token;
	}

	public MoodleCourse getCourseById(int id) {
		return getCourseById(id,false);
	}

	public MoodleCourse getCourseById(int id, boolean forceRefresh) {
		if(forceRefresh || !courses.containsKey(id)) {
			courses.put(id, loadMoodleCourse(id));
		}
		return courses.get(id);
	}

	private MoodleCourse loadMoodleCourse(int id) {
		JSONArray courseResponse = new JSONArray(callMoodleFunction("core_course_get_contents", "courseid="+id));
		JSONObject assignResponse = new JSONObject(callMoodleFunction("mod_assign_get_assignments", "courseids[0]="+id));

		return new MoodleCourse(id, courseResponse, assignResponse);
	}

	public String callMoodleFunction(String functionName, String payload) {
		try {
			URI endpoint = host.getMoodlePath().resolve(
					String.format(
							"webservice/rest/server.php?wstoken=%s&wsfunction=%s&moodlewsrestformat=json&%s",
							URLEncoder.encode(this.token.getToken(), StandardCharsets.UTF_8),
							URLEncoder.encode(functionName, StandardCharsets.UTF_8),
							payload
					)
			);
			HttpURLConnection connection = ((HttpURLConnection) endpoint.toURL().openConnection());
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
			String response = reader.lines().collect(Collectors.joining("\n"));
			reader.close();
			return response;
		} catch(IOException e) {
			throw new RuntimeException("Error while calling moodle function",e);
		}
	}

	public MoodleToken getToken() {
		return token;
	}
}
