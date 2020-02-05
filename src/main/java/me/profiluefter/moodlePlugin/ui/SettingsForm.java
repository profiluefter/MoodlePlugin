package me.profiluefter.moodlePlugin.ui;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import me.profiluefter.moodlePlugin.plugin.MoodleSettings;

import javax.swing.*;

public class SettingsForm {
	private JPanel rootPanel;
	private JTextField usernameInput;
	private JPasswordField passwordInput;
	private JSpinner courseIDInput;
	private JTextField hostInput;

	public JPanel getRootPanel() {
		return rootPanel;
	}

	public String getUsername() {
		return usernameInput.getText();
	}

	public char[] getPassword() {
		return passwordInput.getPassword();
	}

	public int getCourseID() {
		return (int) courseIDInput.getValue();
	}

	public String getHost() {
		return hostInput.getText();
	}

	public MoodleSettings getData() {
		MoodleSettings settings = new MoodleSettings();
		settings.setHost(getHost());
		settings.setCourseID(getCourseID());

		CredentialAttributes key = new CredentialAttributes(CredentialAttributesKt.generateServiceName("moodle", settings.getHost()));
		PasswordSafe.getInstance().set(key, new Credentials(getUsername(), getPassword()));

		return settings;
	}

	public void reset() {
		MoodleSettings settings = MoodleSettings.getInstance();
		this.hostInput.setText(settings.getHost());
		this.courseIDInput.setValue(settings.getCourseID());
		CredentialAttributes key = new CredentialAttributes(CredentialAttributesKt.generateServiceName("moodle", settings.getHost()));
		Credentials credentials = PasswordSafe.getInstance().get(key);
		if(credentials != null) {
			this.usernameInput.setText(credentials.getUserName());
			this.passwordInput.setText(credentials.getPasswordAsString());
		}
	}
}
