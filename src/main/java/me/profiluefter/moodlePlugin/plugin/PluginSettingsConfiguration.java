package me.profiluefter.moodlePlugin.plugin;

import com.intellij.openapi.options.Configurable;
import me.profiluefter.moodlePlugin.ui.SettingsForm;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PluginSettingsConfiguration implements Configurable {
	private SettingsForm form;

	@Nullable
	@Override
	public JComponent createComponent() {
		form = new SettingsForm();
		return form.getRootPanel();
	}

	@Override
	public boolean isModified() {
		return form.hasCredentialsChanged() || !MoodleSettings.getInstance().equals(form.getData());
	}

	@Override
	public void apply() {
		MoodleSettings settings = MoodleSettings.getInstance();
		settings.loadState(form.getData());
	}

	@Override
	public void reset() {
		form.reset();
	}

	@Override
	public void disposeUIResources() {
		form = null;
	}

	@Nls(capitalization = Nls.Capitalization.Title)
	@Override
	public String getDisplayName() {
		return "Moodle";
	}
}
