package me.profiluefter.moodlePlugin.plugin;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.ActivityTracker;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.components.ServiceManager;
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
		return ServiceManager.getService(MoodleData.class);
	}

	public CompletableFuture<Void> refresh() {
		CompletableFuture<Void> future = new CompletableFuture<>();
		new Task.Backgroundable(null, "Loading Moodle Data", true) {
			@Override
			public void run(@NotNull ProgressIndicator progressIndicator) {
				progressIndicator.setIndeterminate(true);
				currentlyRefreshing.set(true);
				ActivityTracker.getInstance().inc();
				MoodleSettings settings = MoodleSettings.getInstance();
				progressIndicator.checkCanceled();

				if(moodleInstance == null) {
					if(settings.getHost() == null) {
						future.completeExceptionally(new IllegalStateException("Missing arguments"));
						return;
					}

					progressIndicator.setText2("Getting auth token");
					MoodleHost host = new MoodleHost(settings.getHost());

					PasswordSafe safe = PasswordSafe.getInstance();
					CredentialAttributes key = new CredentialAttributes(CredentialAttributesKt.generateServiceName("moodle", settings.getHost()));
					progressIndicator.checkCanceled();
					Credentials credentials = safe.get(key);
					if(credentials == null) {
						future.completeExceptionally(new IllegalStateException("Missing arguments"));
						return;
					}

					progressIndicator.checkCanceled();
					MoodleToken token = host.authenticate(credentials.getUserName(), credentials.getPasswordAsString());
					moodleInstance = host.connect(token);
				}

				progressIndicator.setText2("Getting course data");
				progressIndicator.checkCanceled();
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
