package me.profiluefter.moodlePlugin.moodle.modules;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class MoodleModule {
	private final int id;
	private final String name;
	private final URL iconURL;

	MoodleModule(JSONObject data) {
		URL iconURL;
		try {
			iconURL = new URL(data.getString("modicon"));
		} catch(MalformedURLException e) {
			throw new RuntimeException("Error while parsing icon url", e);
		}

		this.id = data.optInt("id");
		this.name = data.getString("name");
		this.iconURL = iconURL;
	}

	public static MoodleModule parseModule(JSONObject moduleRaw) {
		MoodleModule moodleModule;

		String moduleName = moduleRaw.getString("modname");
		switch(moduleName) {
			case "forum":
				moodleModule = new MoodleForumModule(moduleRaw);
				break;
			case "page":
				moodleModule = new MoodlePageModule(moduleRaw);
				break;
			case "label":
				moodleModule = new MoodleLabelModule(moduleRaw);
				break;
			case "url":
				moodleModule = new MoodleURLModule(moduleRaw);
				break;
			case "resource":
				moodleModule = new MoodleResourceModule(moduleRaw);
				break;
			case "assign":
				moodleModule = new MoodleAssignModule(moduleRaw);
				break;
			case "book":
				moodleModule = new MoodleBookModule(moduleRaw);
				break;
			default:
				throw new IllegalStateException("Unknown moodle module \n" + moduleName + "\n");
		}

		return moodleModule;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public URL getIconURL() {
		return iconURL;
	}
}
