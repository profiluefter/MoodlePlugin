package me.profiluefter.moodlePlugin.moodle;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.stream.Collectors;

public class MoodleHost {
	private static final String MOODLE_SERVICE = "moodle_mobile_app";
	private final URI moodlePath;

	public MoodleHost(String moodlePath) {
		try {
			this.moodlePath = new URI(moodlePath);
		} catch(URISyntaxException e) {
			throw new RuntimeException("Error while parsing URL",e);
		}
	}

	public MoodleToken authenticate(String username, String password) {
		URI loginEndpoint;
		try {
			loginEndpoint = moodlePath.resolve(
					String.format(
							"login/token.php?username=%s&password=%s&service=%s",
							URLEncoder.encode(username, "UTF-8"),
							URLEncoder.encode(password, "UTF-8"),
							URLEncoder.encode(MOODLE_SERVICE, "UTF-8")
					));
		} catch(UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 Encoding not supported?", e);
		}

		String response;

		try {
			URLConnection connection = loginEndpoint.toURL().openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			response = reader.lines().collect(Collectors.joining("\n"));
			reader.close();
		} catch(IOException e) {
			throw new RuntimeException("Error while connecting to authentication endpoint", e);
		}

		JSONObject jsonResponse = new JSONObject(response);
		assert jsonResponse.has("token") || jsonResponse.has("error");
		if(jsonResponse.has("error"))
			throw new IllegalArgumentException(jsonResponse.getString("error"));
		String token = jsonResponse.getString("token");

		return new MoodleToken(token);
	}

	public Moodle connect(MoodleToken token) {
		return new Moodle(this, token);
	}

	URI getMoodlePath() {
		return moodlePath;
	}
}
