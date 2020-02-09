package me.profiluefter.moodlePlugin.moodle.modules;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MoodleAssignModule extends MoodleModuleWithURL {
	private AttemptReopenMethod attemptReopenMethod;
	/**
	 * The format of the key is "plugin.subtype.name"
	 */
	private Map<String, String> configs;
	private Map<String, MoodleFile> assets;
	private Map<String, MoodleFile> attachments;
	private int maxAttempts;
	private String content;
	private int assignmentID;
	private Date dueDate;
	private Integer grade;
	private boolean submissions;
	private boolean submissionDraft;

	public MoodleAssignModule(JSONObject data) {
		super(data);
	}

	public void setAssignmentData(JSONObject data) {
		{
			switch(data.getString("attemptreopenmethod")) {
				case "none":
					this.attemptReopenMethod = AttemptReopenMethod.None;
					break;
				case "untilpass":
					this.attemptReopenMethod = AttemptReopenMethod.UntilPass;
					break;
			}
		}

		{
			this.configs = new HashMap<>();
			for(Object rawConfig : data.getJSONArray("configs")) {
				JSONObject config = ((JSONObject) rawConfig);
				this.configs.put(
						String.format("%s.%s.%s", config.getString("plugin"), config.getString("subtype"), config.getString("name")),
						config.getString("value")
				);
			}
		}

		{
			assets = new HashMap<>();
			for(Object rawFile : data.getJSONArray("introfiles")) {
				JSONObject file = ((JSONObject) rawFile);
				try {
					MoodleFile moodleFile = new MoodleFile(file.getString("filename"), file.getString("filepath"), new URL(file.getString("fileurl")), file.getString("mimetype"));
					this.assets.put(moodleFile.getAbsolutePath(), moodleFile);
				} catch(MalformedURLException e) {
					throw new RuntimeException("Error while parsing file url", e);
				}
			}
		}

		{
			attachments = new HashMap<>();
			for(Object rawFile : data.getJSONArray("introattachments")) {
				JSONObject file = ((JSONObject) rawFile);
				try {
					MoodleFile moodleFile = new MoodleFile(file.getString("filename"), file.getString("filepath"), new URL(file.getString("fileurl")), file.getString("mimetype"));
					this.attachments.put(moodleFile.getAbsolutePath(), moodleFile);
				} catch(MalformedURLException e) {
					throw new RuntimeException("Error while parsing file url", e);
				}
			}
		}

		{
			this.dueDate = data.getInt("duedate") == 0 ? null : new Date(data.getInt("duedate"));
			if(this.dueDate == null)
				this.dueDate = data.getInt("cutoffdate") == 0 ? null : new Date(data.getInt("cutoffdate"));

			//TODO: find out purpose of "cutoffdate"
		}

		switch(data.getInt("nosubmissions")) {
			case 0:
			case 1:
				submissions = data.getInt("nosubmissions") == 0;
				break;
			default:
				throw new IllegalStateException();
		}

		assert data.getInt("submissiondrafts") == 0 || data.getInt("submissiondrafts") == 1;
		this.submissionDraft = data.getInt("submissiondrafts") == 1;
		this.maxAttempts = data.getInt("maxattempts");
		this.content = data.getString("intro");
		this.assignmentID = data.getInt("id");
		this.grade = data.getInt("grade") == -34 ? null : data.getInt("grade");

		//Assertions on data that never changed
		assert data.getInt("sendstudentnotifications") == 1;
		assert data.getInt("requireallteammemberssubmit") == 0;
		assert data.getInt("completionsubmit") == 0;
		assert data.getInt("sendlatenotifications") == 0;
		assert data.getInt("markingallocation") == 0;
		assert data.getInt("preventsubmissionnotingroup") == 0;
		assert data.getInt("teamsubmissiongroupingid") == 0;
		assert data.getInt("revealidentities") == 0;
		assert data.getInt("introformat") == 1;
		assert data.getInt("markingworkflow") == 0;
		assert data.getInt("sendnotifications") == 0;
		assert data.getInt("teamsubmission") == 0;
		assert data.getInt("blindmarking") == 0;
		assert data.getInt("requiresubmissionstatement") == 0;
	}

	//TODO: methods to interact

	private enum AttemptReopenMethod {
		None, UntilPass
	}

	private static class MoodleFile {
		private final String fileName;
		private final String filePath;
		private final URL fileURL;
		private final String mimeType;

		public MoodleFile(String fileName, String filePath, URL fileURL, String mimeType) {
			this.fileName = fileName;
			this.filePath = filePath;
			this.fileURL = fileURL;
			this.mimeType = mimeType;
		}

		public String getAbsolutePath() {
			return filePath + fileName;
		}
	}
}
