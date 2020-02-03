package me.profiluefter.moodlePlugin.moodle;

import me.profiluefter.moodlePlugin.data.moodle.MoodleToken;
import org.json.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		String response = new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining("\n"));

		String token = new JSONObject(response).getString("token");

		return new MoodleToken(token);
	}

	public Moodle connect(MoodleToken token) {
		return null;
	}
}
