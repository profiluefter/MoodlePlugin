package me.profiluefter.moodlePlugin.moodle;

import me.profiluefter.moodlePlugin.moodle.data.MoodleToken;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import static me.profiluefter.moodlePlugin.moodle.SecretTestProperties.secrets;

public class MoodleHostTest {
	@Test
	public void authenticate() throws URISyntaxException, IOException {
		MoodleHost moodleHost = new MoodleHost(secrets.getProperty("moodle.host"));

		MoodleToken token = moodleHost.authenticate(
				secrets.getProperty("moodle.username"),
				secrets.getProperty("moodle.password"));
		assertNotNull(token);
		assertFalse(token.getToken().isEmpty());
	}

	@Test
	public void connect() throws URISyntaxException, IOException {
		MoodleHost host = new MoodleHost(secrets.getProperty("moodle.host"));

		MoodleToken token = host.authenticate(
				secrets.getProperty("moodle.username"),
				secrets.getProperty("moodle.password")
		);

		Moodle moodle = host.connect(token);

		assertNotNull(moodle);
	}
}