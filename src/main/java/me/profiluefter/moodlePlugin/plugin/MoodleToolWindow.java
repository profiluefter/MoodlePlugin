package me.profiluefter.moodlePlugin.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import me.profiluefter.moodlePlugin.ui.MoodleTaskOverview;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MoodleToolWindow implements ToolWindowFactory {
	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
		ContentManager contentManager = toolWindow.getContentManager();
		Content uiTab = contentManager.getFactory().createContent(new MoodleTaskOverview().getRootPanel(), "UI", true);
		contentManager.addContent(uiTab);
	}
}
