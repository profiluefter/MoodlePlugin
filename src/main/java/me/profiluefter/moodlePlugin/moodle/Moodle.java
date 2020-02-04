package me.profiluefter.moodlePlugin.moodle;

import me.profiluefter.moodlePlugin.moodle.data.Course;
import me.profiluefter.moodlePlugin.moodle.data.MoodleToken;
import org.json.JSONArray;

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
	private final Map<Integer, Course> courses;

	private final MoodleHost host;
	private final MoodleToken token;

	Moodle(MoodleHost host, MoodleToken token) {
		this.courses = new HashMap<>();

		this.host = host;
		this.token = token;
	}

	public Course getCourseByID(int id) {
		return getCourseByID(id,false);
	}

	public Course getCourseByID(int id, boolean forceRefresh) {
		if(forceRefresh || !courses.containsKey(id)) {
			System.out.println(callMoodleFunction("core_course_get_contents", "courseid="+id));
		}
		return courses.get(id);
	}

	public JSONArray callMoodleFunction(String functionName, String payload) {
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response = reader.lines().collect(Collectors.joining("\n"));
			reader.close();
			System.out.println(response);
			return new JSONArray(response);
		} catch(IOException e) {
			throw new RuntimeException("Error while calling moodle function",e);
		}
	}
}
