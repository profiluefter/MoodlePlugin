package me.profiluefter.moodlePlugin.plugin;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;

public class MoodleNotification {
	private static final NotificationGroup group = new NotificationGroup("", NotificationDisplayType.BALLOON, false);

	public static String getGroupID() {
		return group.getDisplayId();
	}
}
