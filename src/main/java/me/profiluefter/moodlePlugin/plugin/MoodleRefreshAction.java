package me.profiluefter.moodlePlugin.plugin;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class MoodleRefreshAction extends AnAction {
	@Override
	public void update(@NotNull AnActionEvent e) {
		e.getPresentation().setIcon(AllIcons.Actions.Refresh); //TODO: Find out how to do this in plugin.xml
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {
		MoodleToolWindow.getOverviewByProject(event.getProject()).refreshData();
	}
}
