package me.profiluefter.moodlePlugin.plugin;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.ActivityTracker;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import me.profiluefter.moodlePlugin.moodle.Moodle;
import me.profiluefter.moodlePlugin.moodle.MoodleHost;
import me.profiluefter.moodlePlugin.moodle.MoodleToken;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class MoodleData {
	private Moodle moodleInstance = null;
	private final AtomicBoolean currentlyRefreshing = new AtomicBoolean(false);

	public static MoodleData getInstance() {
		return ApplicationManager.getApplication().getService(MoodleData.class);
	}

	public CompletableFuture<Void> refresh() {
		CompletableFuture<Void> future = new CompletableFuture<>();
		new Task.Backgroundable(null, "Loading moodle data", true) {
			@Override
			public void run(@NotNull ProgressIndicator progressIndicator) {
				progressIndicator.setIndeterminate(true);
				currentlyRefreshing.set(true);
				ActivityTracker.getInstance().inc();
				MoodleSettings settings = MoodleSettings.getInstance();
				progressIndicator.checkCanceled();

				if(moodleInstance == null) {
					if(settings.getHost() == null || settings.getHost().isEmpty()) {
						future.completeExceptionally(new IllegalStateException("Missing arguments"));
						return;
					}

					progressIndicator.setText2("Getting auth token");
					MoodleHost host = new MoodleHost(settings.getHost());

					PasswordSafe safe = PasswordSafe.getInstance();
					CredentialAttributes key = new CredentialAttributes(CredentialAttributesKt.generateServiceName("moodle", settings.getHost()));
					progressIndicator.checkCanceled();
					Credentials credentials = safe.get(key);
					if(credentials == null
							|| credentials.getUserName() == null
							|| credentials.getPasswordAsString() == null
							|| credentials.getPasswordAsString().isEmpty()
							|| credentials.getUserName().isEmpty()) {
						future.completeExceptionally(new IllegalStateException("Missing arguments"));
						return;
					}

					progressIndicator.checkCanceled();
					try {
						MoodleToken token = host.authenticate(credentials.getUserName(), credentials.getPasswordAsString());
						moodleInstance = host.connect(token);
					} catch(IllegalArgumentException exception) {
						future.completeExceptionally(new IllegalStateException("Login not successful", exception));
					}
				}

				progressIndicator.setText2("Getting course data");
				progressIndicator.checkCanceled();
				if(settings.getCourseID() == -1) {
					future.completeExceptionally(new IllegalStateException("Missing arguments"));
					return;
				}
				moodleInstance.getCourseById(settings.getCourseID(), true);
				progressIndicator.checkCanceled();
				future.complete(null);
			}

			@Override
			public void onCancel() {
				setFinished();
			}

			@Override
			public void onSuccess() {
				setFinished();
			}

			@Override
			public void onThrowable(@NotNull Throwable error) {
				setFinished();
			}

			private void setFinished() {
				currentlyRefreshing.set(false);
				ActivityTracker.getInstance().inc();
			}
		}.queue();
		return future;
	}

	public boolean isRefreshing() {
		return currentlyRefreshing.get();
	}

	public Moodle getData() {
		return moodleInstance;
	}
}
