package me.profiluefter.moodlePlugin.ui.moodleModules;

import com.intellij.ui.components.JBLabel;
import me.profiluefter.moodlePlugin.moodle.modules.MoodleLabelModule;

public class MoodleLabelViewer extends MoodleModuleViewer {
	public MoodleLabelViewer(MoodleLabelModule module) {
		super(module);
		panel.add(new JBLabel("<html>"+module.getDescription()+"</html>"));
	}
}
