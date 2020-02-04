package me.profiluefter.moodlePlugin.moodle;

import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class MoodleHost {
	private static final String MOODLE_SERVICE = "moodle_mobile_app";
	private final URI moodlePath;

	public MoodleHost(String moodlePath) throws URISyntaxException {
		this.moodlePath = new URI(moodlePath);
	}

	public MoodleToken authenticate(String username, String password) throws IOException {
		URI loginEndpoint = moodlePath.resolve(
				String.format(
						"login/token.php?username=%s&password=%s&service=%s",
						URLEncoder.encode(username, StandardCharsets.UTF_8),
						URLEncoder.encode(password, StandardCharsets.UTF_8),
						URLEncoder.encode(MOODLE_SERVICE, StandardCharsets.UTF_8)
				));

		URLConnection connection = loginEndpoint.toURL().openConnection();
		connection.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String response = reader.lines().collect(Collectors.joining("\n"));
		reader.close();

		String token = new JSONObject(response).getString("token");

		return new MoodleToken(token);
	}

	public Moodle connect(MoodleToken token) {
		return new Moodle(this, token);
	}

	URI getMoodlePath() {
		return moodlePath;
	}
}
