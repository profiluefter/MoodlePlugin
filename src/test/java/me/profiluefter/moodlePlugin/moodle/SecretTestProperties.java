package me.profiluefter.moodlePlugin.moodle;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class SecretTestProperties {
	public static final Properties secrets = new Properties();

	static {
		try {
			secrets.load(
					Objects.requireNonNull(
							MoodleHostTest.class
									.getClassLoader()
									.getResourceAsStream("secret.properties")));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
