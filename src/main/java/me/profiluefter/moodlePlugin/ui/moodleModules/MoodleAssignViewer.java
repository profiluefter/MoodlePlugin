package me.profiluefter.moodlePlugin.ui.moodleModules;

import com.intellij.openapi.roots.ui.componentsList.components.ScrollablePanel;
import com.intellij.ui.components.JBLabel;
import me.profiluefter.moodlePlugin.moodle.modules.MoodleAssignModule;

import javax.swing.*;
import java.awt.*;

public class MoodleAssignViewer extends MoodleModuleViewer {
	private JPanel guiBuilderPanel;
	private JLabel name;

	public MoodleAssignViewer(MoodleAssignModule module) {
		super(module);
		panel.setLayout(new BorderLayout());
		ScrollablePanel scrollablePanel = new ScrollablePanel(new BorderLayout());
		scrollablePanel.add(guiBuilderPanel);
		panel.add(new JScrollPane(scrollablePanel));

		name.setText("<html>"+module.getDescription()+"</html>");

	}

	private void createUIComponents() {
		name = new JBLabel();
		((JBLabel) name).setAllowAutoWrapping(true);
	}
}
