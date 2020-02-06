package me.profiluefter.moodlePlugin.ui.moodleModules;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import me.profiluefter.moodlePlugin.moodle.modules.MoodleLabelModule;

import java.awt.*;

public class MoodleLabelViewer extends MoodleModuleViewer {
	public MoodleLabelViewer(MoodleLabelModule module) {
		super(module);
		panel.setLayout(new BorderLayout());
		panel.add(new JBScrollPane(new JBLabel("<html>" + module.getDescription() + "</html>"){{setVerticalAlignment(TOP);}}));
	}
}
