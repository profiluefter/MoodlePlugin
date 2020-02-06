package me.profiluefter.moodlePlugin.moodle.modules;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import me.profiluefter.moodlePlugin.plugin.MoodleData;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class BackgroundLoader {
	static URL appendToken(URL oldUrl) {
		try {
			URI uri = oldUrl.toURI();
			String query = uri.getQuery();
			if(query == null) {
				query = "token=" + MoodleData.getInstance().getData().getToken().getToken();
			} else {
				query += "&token=" + MoodleData.getInstance().getData().getToken().getToken();
			}
			return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), query, uri.getFragment()).toURL();
		} catch(URISyntaxException | MalformedURLException e) {
			throw new RuntimeException("Error while appending query to url", e);
		}
	}

	static CompletableFuture<String> loadResource(URL target, String message) {
		CompletableFuture<String> future = new CompletableFuture<>();
		ProgressManager.getInstance().run(new Task.Backgroundable(null, message, true) {
			@Override
			public void run(@NotNull ProgressIndicator progressIndicator) {
				try {
					progressIndicator.setIndeterminate(true);
					URLConnection connection = target.openConnection();
					progressIndicator.checkCanceled();
					connection.connect();
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
					progressIndicator.checkCanceled();
					String response = reader.lines().collect(Collectors.joining("\n"));
					reader.close();
					future.complete(response);
				} catch(IOException e) {
					future.completeExceptionally(e);
				}
			}
		});
		return future;
	}
}
