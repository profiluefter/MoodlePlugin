package me.profiluefter.moodlePlugin.moodle.modules;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class MoodlePageModule extends MoodleModule {
	private final URL contentURL;
	private String content;

	public MoodlePageModule(JSONObject data) {
		super(data);
		JSONArray contents = data.getJSONArray("contents");
		assert contents.length() == 1;
		JSONObject pageData = contents.getJSONObject(0);
		assert pageData.getString("type").equals("file");
		try {
			this.contentURL = new URL(pageData.getString("fileurl"));
		} catch(MalformedURLException e) {
			throw new RuntimeException("Error while parsing content URL", e);
		}
	}

	public URL getContentURL() {
		return contentURL;
	}

	public CompletableFuture<String> getContentAsync() {
		if(content != null) return CompletableFuture.completedFuture(content);
		CompletableFuture<String> future = BackgroundLoader.loadResource(BackgroundLoader.appendToken(contentURL), "Loading Page Content");
		future.whenComplete((s, throwable) -> {
			if(throwable != null) throw new RuntimeException("Error while loading content", throwable);
			this.content = s;
		});
		return future;
	}
}
