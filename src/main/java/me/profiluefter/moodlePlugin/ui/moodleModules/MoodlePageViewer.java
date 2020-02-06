package me.profiluefter.moodlePlugin.ui.moodleModules;

import com.intellij.openapi.roots.ui.componentsList.components.ScrollablePanel;
import com.intellij.ui.components.JBLabel;
import me.profiluefter.moodlePlugin.moodle.modules.MoodlePageModule;

import javax.swing.*;
import java.awt.*;

public class MoodlePageViewer extends MoodleModuleViewer {
	public MoodlePageViewer(MoodlePageModule module) {
		super(module);
		panel.setLayout(new BorderLayout());
		module.getContentAsync().whenComplete((s, throwable) -> {
			if(throwable != null) throw new RuntimeException("Error while loading moodle page", throwable);

			ScrollablePanel scrollablePanel = new ScrollablePanel(new BorderLayout());
			scrollablePanel.add(new JBLabel("<html>" + s + "</html>") {{
				setVerticalAlignment(TOP);
				setAllowAutoWrapping(true);
			}});

			panel.add(new JScrollPane(scrollablePanel));
		});
	}
}
