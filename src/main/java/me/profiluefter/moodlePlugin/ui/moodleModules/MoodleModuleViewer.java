package me.profiluefter.moodlePlugin.ui.moodleModules;

import me.profiluefter.moodlePlugin.moodle.modules.*;

import javax.swing.*;

public abstract class MoodleModuleViewer {
	protected MoodleModule module;
	protected JPanel panel;

	protected MoodleModuleViewer(MoodleModule module) {
		this.module = module;
		this.panel = new JPanel();
	}

	JComponent getComponent() {
		return panel;
	}

	public static JComponent createModuleView(MoodleModule module) {
		if(module instanceof MoodleAssignModule) {
			return new MoodleAssignViewer(((MoodleAssignModule) module)).getComponent();
		} else if(module instanceof MoodleBookModule) {
			return new MoodleBookViewer(((MoodleBookModule) module)).getComponent();
		} else if(module instanceof MoodleForumModule) {
			return new MoodleForumViewer(((MoodleForumModule) module)).getComponent();
		} else if(module instanceof MoodleLabelModule) {
			return new MoodleLabelViewer(((MoodleLabelModule) module)).getComponent();
		} else if(module instanceof MoodlePageModule) {
			return new MoodlePageViewer(((MoodlePageModule) module)).getComponent();
		} else if(module instanceof MoodleResourceModule) {
			return new MoodleResourceViewer(((MoodleResourceModule) module)).getComponent();
		} else if(module instanceof MoodleURLModule) {
			return new MoodleURLViewer(((MoodleURLModule) module)).getComponent();
		} else {
			throw new IllegalStateException("Unknown moodle module " + module.getClass().getName());
		}
	}
}
