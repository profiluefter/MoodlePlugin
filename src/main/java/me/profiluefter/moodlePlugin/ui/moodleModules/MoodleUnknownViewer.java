package me.profiluefter.moodlePlugin.ui.moodleModules;

import com.intellij.ui.components.JBLabel;
import me.profiluefter.moodlePlugin.moodle.modules.MoodleUnknownModule;

import javax.swing.*;
import java.awt.*;

public class MoodleUnknownViewer extends MoodleModuleViewer {
	public MoodleUnknownViewer(MoodleUnknownModule module) {
		super(module);
		panel.setLayout(new BorderLayout());
		JBLabel label = new JBLabel();
		label.setAllowAutoWrapping(true);
		String rawText = "No Viewer for Module:\n\n" + module.toString();
		rawText = "<html>"+rawText.replace("\n","<br>")+"</html>";
		label.setText(rawText);
		label.setVerticalAlignment(SwingConstants.TOP);
		panel.add(label);
	}
}
