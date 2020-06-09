package me.profiluefter.moodlePlugin.ui.moodleModules;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;
import com.intellij.ui.jcef.JBCefClient;
import me.profiluefter.moodlePlugin.moodle.modules.MoodleAssignModule;
import org.cef.CefApp;

import javax.swing.*;
import java.awt.*;

public class MoodleAssignViewer extends MoodleModuleViewer {
	private JPanel guiBuilderPanel;
	private JLabel taskDescription;

	@SuppressWarnings("UnstableApiUsage")
	public MoodleAssignViewer(MoodleAssignModule module) {
		super(module);

		if(!JBCefApp.isSupported())
			throw new RuntimeException("No CEF!");

		CefApp.startup(new String[0]);
		JBCefClient client = JBCefApp.getInstance().createClient();
		JBCefBrowser browser = new JBCefBrowser(client, null);
		browser.loadHTML(module.getDescription());

		panel.setLayout(new BorderLayout());
		panel.add(browser.getComponent());

		browser.openDevtools();
	}

	private void createUIComponents() {
		taskDescription = new JBLabel();
		((JBLabel) taskDescription).setAllowAutoWrapping(true);
	}
}
