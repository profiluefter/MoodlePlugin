package me.profiluefter.moodlePlugin.moodle;

import me.profiluefter.moodlePlugin.moodle.data.MoodleToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static me.profiluefter.moodlePlugin.moodle.SecretTestProperties.secrets;

public class MoodleTest {
	private static Moodle moodleInstance;

	@BeforeClass
	public static void beforeClass() throws Exception {
		MoodleHost host = new MoodleHost(secrets.getProperty("moodle.host"));
		MoodleToken token = host.authenticate(secrets.getProperty("moodle.username"), secrets.getProperty("moodle.password"));
		MoodleTest.moodleInstance = host.connect(token);
	}
}