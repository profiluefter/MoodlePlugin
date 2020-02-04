package me.profiluefter.moodlePlugin.moodle;

import org.junit.BeforeClass;
import org.junit.Test;

import static me.profiluefter.moodlePlugin.moodle.SecretTestProperties.secrets;

public class MoodleTest {
	private static Moodle moodleInstance;

	@BeforeClass
	public static void beforeClass() throws Exception {
		MoodleHost host = new MoodleHost(secrets.getProperty("moodle.host"));
		MoodleToken token = host.authenticate(secrets.getProperty("moodle.username"), secrets.getProperty("moodle.password"));
		MoodleTest.moodleInstance = host.connect(token);
	}

	@Test
	public void getCourses() {
		MoodleCourse course = moodleInstance.getCourseById(Integer.parseInt(secrets.getProperty("moodle.course.id")), true);
	}
}