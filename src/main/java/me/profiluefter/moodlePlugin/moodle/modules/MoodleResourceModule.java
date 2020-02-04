package me.profiluefter.moodlePlugin.moodle.modules;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MoodleResourceModule extends MoodleModule {
	private final List<Attachment> contents;

	public MoodleResourceModule(JSONObject data) {
		super(data);
		JSONArray rawContents = data.getJSONArray("contents");
		this.contents = new ArrayList<>(rawContents.length());
		for(Object rawContent : rawContents) {
			assert rawContent instanceof JSONObject;
			this.contents.add(new Attachment((JSONObject) rawContent));
		}
	}

	public List<Attachment> getContents() {
		return contents;
	}

	public static class Attachment {
		private final String fileName;
		private final URL fileURL;
		//TODO: parse more

		private Attachment(JSONObject data) {
			assert data.getString("type").equals("file"); //Only one that's known to me

			this.fileName = data.getString("filename");
			try {
				this.fileURL = new URL(data.getString("fileurl"));
			} catch(MalformedURLException e) {
				throw new RuntimeException("Error while parsing file URL", e);
			}
		}

		public String getFileName() {
			return fileName;
		}

		public URL getFileURL() {
			return fileURL;
		}
	}
}
