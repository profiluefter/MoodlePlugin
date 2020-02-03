package me.profiluefter.moodlePlugin.moodle;

import me.profiluefter.moodlePlugin.data.moodle.MoodleToken;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;

public class MoodleHostTest {
	private static Properties testSecrets = new Properties();

	@BeforeClass
	public static void loadSecrets() throws IOException {
		testSecrets.load(
				Objects.requireNonNull(
						MoodleHostTest.class
								.getClassLoader()
								.getResourceAsStream("secret.properties")));
	}

	@Test
	public void authenticate() throws URISyntaxException, IOException {
		MoodleHost moodleHost = new MoodleHost(testSecrets.getProperty("moodle.host"));

		MoodleToken token = moodleHost.authenticate(
				testSecrets.getProperty("moodle.username"),
				testSecrets.getProperty("moodle.password"));
		assertNotNull(token);
		assertFalse(token.getToken().isEmpty());
	}

	@Test
	public void connect() {
	}
}