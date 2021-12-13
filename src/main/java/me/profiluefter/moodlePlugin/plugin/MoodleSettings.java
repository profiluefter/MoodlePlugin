package me.profiluefter.moodlePlugin.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@State(name = "moodleSettings", storages = {
		@Storage("moodle.xml")
})
public class MoodleSettings implements PersistentStateComponent<MoodleSettings> {
	public static MoodleSettings getInstance() {
		return ApplicationManager.getApplication().getService(MoodleSettings.class);
	}
	private String host;
	private int courseID;

	@Nullable
	@Override
	public MoodleSettings getState() {
		return this;
	}

	@Override
	public void loadState(@NotNull MoodleSettings moodleSettings) {
		XmlSerializerUtil.copyBean(moodleSettings, this);
	}

	public MoodleSettings() {
		this.host = null;
		this.courseID = -1;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getCourseID() {
		return courseID;
	}

	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		MoodleSettings that = (MoodleSettings) o;
		return courseID == that.courseID &&
				Objects.equals(host, that.host);
	}

	@Override
	public int hashCode() {
		return Objects.hash(host, courseID);
	}
}
