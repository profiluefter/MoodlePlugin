package me.profiluefter.moodlePlugin.plugin;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import me.profiluefter.moodlePlugin.moodle.Moodle;
import me.profiluefter.moodlePlugin.moodle.MoodleHost;
import me.profiluefter.moodlePlugin.moodle.MoodleToken;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MoodleData {
	private Moodle moodleInstance = null;

	public static MoodleData getInstance() {
		return ServiceManager.getService(MoodleData.class);
	}

	public CompletableFuture<Void> refresh() {
		CompletableFuture<Void> future = new CompletableFuture<>();
		ProgressManager.getInstance().run(new Task.Backgroundable(null, "Loading Moodle Data", true) {
			@Override
			public void run(@NotNull ProgressIndicator progressIndicator) {
				progressIndicator.setIndeterminate(true);
				progressIndicator.checkCanceled();
				MoodleSettings settings = MoodleSettings.getInstance();

				if(moodleInstance == null) {
					if(settings.getHost() == null) {
						handleMissingData();
						return;
					}

					progressIndicator.setText2("Getting auth token");
					MoodleHost host = new MoodleHost(settings.getHost());

					PasswordSafe safe = PasswordSafe.getInstance();
					CredentialAttributes key = new CredentialAttributes(CredentialAttributesKt.generateServiceName("moodle", settings.getHost()));
					progressIndicator.checkCanceled();
					Credentials credentials = safe.get(key);
					if(credentials == null) {
						handleMissingData();
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
				progressIndicator.setText2("Executing callback");
				future.complete(null);
			}

			private void handleMissingData() {
				future.completeExceptionally(new IllegalStateException("Missing arguments"));
			}
		});
		return future;
	}


	public Moodle getData() {
		return moodleInstance;
	}
}
