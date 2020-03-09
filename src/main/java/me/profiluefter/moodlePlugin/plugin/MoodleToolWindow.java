package me.profiluefter.moodlePlugin.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import me.profiluefter.moodlePlugin.ui.MoodleCoursePanel;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MoodleToolWindow implements ToolWindowFactory {

	private static final Map<Project, MoodleCoursePanel> contents = new HashMap<>();

	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		ContentManager contentManager = toolWindow.getContentManager();
		MoodleCoursePanel overview = new MoodleCoursePanel(project);
		Content courseOverview = contentManager.getFactory().createContent(
				overview.getRootPanel(),
				"Course", true);
		courseOverview.setCloseable(false);
		contentManager.addContent(courseOverview);
		contents.put(project, overview);
	}

	public static MoodleCoursePanel getOverviewByProject(Project project) {
		return contents.get(project);
	}
}
