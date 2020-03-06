package me.profiluefter.moodlePlugin.ui.moodleModules;

import me.profiluefter.moodlePlugin.moodle.modules.MoodleURLModule;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class MoodleURLViewer extends MoodleModuleViewer{
	public MoodleURLViewer(MoodleURLModule module) {
		super(module);
		panel.add(new JButton(module.getName()){{addActionListener((e)->{
			try {
				Desktop.getDesktop().browse(module.getUrl().toURI());
			} catch(IOException | URISyntaxException ex) {
				throw new RuntimeException("Error while opening link", ex);
			}
		});}});
	}
}
