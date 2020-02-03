package me.profiluefter.moodlePlugin.moodle;

import me.profiluefter.moodlePlugin.moodle.data.Course;
import me.profiluefter.moodlePlugin.moodle.data.MoodleToken;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Moodle {
	private List<Course> courses;

	private MoodleHost host;
	private MoodleToken token;

	Moodle(MoodleHost host, MoodleToken token) {
		this.courses = new ArrayList<>();

		this.host = host;
		this.token = token;
	}

	public List<Course> getCourses() {
		return getCourses(false);
	}

	public List<Course> getCourses(boolean forceRefresh) {
		throw new IllegalStateException("TODO");
	}

	public JSONObject callMoodleFunction(String functionName, JSONObject parameters) throws IOException {
		URI endpoint = host.getMoodlePath().resolve(
				String.format(
						"lib/ajax/service.php?info=%s",
						functionName
				)
		);

		JSONArray payload = new JSONArray().put(0, new JSONObject().put("args", parameters).put("index", 0).put("methodname", functionName));

		HttpURLConnection connection = ((HttpURLConnection) endpoint.toURL().openConnection());
		connection.setRequestMethod("POST");
		connection.connect();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
		writer.write(payload.toString());
		writer.close();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String response = reader.lines().collect(Collectors.joining("\n"));
		System.out.println(response);
		return null;
	}
}
