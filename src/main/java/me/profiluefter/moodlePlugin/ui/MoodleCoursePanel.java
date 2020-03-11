package me.profiluefter.moodlePlugin.ui;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBEmptyBorder;
import me.profiluefter.moodlePlugin.moodle.Moodle;
import me.profiluefter.moodlePlugin.moodle.MoodleCourse;
import me.profiluefter.moodlePlugin.moodle.MoodleSection;
import me.profiluefter.moodlePlugin.moodle.modules.*;
import me.profiluefter.moodlePlugin.plugin.MoodleData;
import me.profiluefter.moodlePlugin.plugin.MoodleSettings;
import me.profiluefter.moodlePlugin.ui.moodleModules.MoodleModuleViewer;

import javax.swing.*;
import javax.swing.tree.*;

public class MoodleCoursePanel {
	private JPanel rootPanel;
	private Tree tree;
	private JComponent toolbar;
	private JScrollPane scrollPane;
	private final Project project;

	public MoodleCoursePanel(Project project) {
		this.project = project;

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(event -> {
			Object lastSelectedPathComponent = tree.getLastSelectedPathComponent();
			if(lastSelectedPathComponent instanceof DefaultMutableTreeNode) {
				Object node = ((DefaultMutableTreeNode) lastSelectedPathComponent).getUserObject();
				if(node instanceof MoodleModule) {
					ContentManager contentManager = ToolWindowManager.getInstance(project).getToolWindow("Moodle").getContentManager();
					Content content = contentManager.getFactory().createContent(
							MoodleModuleViewer.createModuleView((MoodleModule) node),
							((MoodleModule) node).getName(), true);
					contentManager.addContent(content);
					contentManager.setSelectedContent(content);
				}
			}
		});
	}

	public JPanel getRootPanel() {
		return rootPanel;
	}

	public void refreshData() {
		tree.setPaintBusy(true);
		MoodleData.getInstance().refresh().whenComplete((value, error) -> SwingUtilities.invokeLater(() -> {
			tree.setModel(new DefaultTreeModel(moodleDataToTreeNode()));
			tree.setPaintBusy(false);
		}));
	}

	private TreeNode moodleDataToTreeNode() {
		Moodle data = MoodleData.getInstance().getData();
		if(data == null) return new DefaultMutableTreeNode("Course");

		MoodleCourse course = data.getCourseById(MoodleSettings.getInstance().getCourseID());

		//Set tab title
		//noinspection ConstantConditions
		ContentManager contentManager = ToolWindowManager.getInstance(project).getToolWindow("Moodle").getContentManager();
		Content content = contentManager.getContent(rootPanel);
		String shortName = course.getShortName();
		content.setDisplayName(shortName);

		DefaultMutableTreeNode courseRoot = new DefaultMutableTreeNode(course);

		for(MoodleSection section : course.getSections().values()) {
			DefaultMutableTreeNode sectionRoot = new DefaultMutableTreeNode(section);

			for(MoodleModule module : section.getModules()) {
				DefaultMutableTreeNode moduleRoot = new DefaultMutableTreeNode(module);
				sectionRoot.add(moduleRoot);
			}

			courseRoot.add(sectionRoot);
		}

		return courseRoot;
	}

	private void createUIComponents() {
		scrollPane = new JBScrollPane();

		ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("Moodle Actions", (ActionGroup) ActionManager.getInstance().getAction("me.profiluefter.moodlePlugin"), true);
		actionToolbar.setTargetComponent(rootPanel);
		toolbar = actionToolbar.getComponent();
		toolbar.setBorder(new JBEmptyBorder(2));

		tree = new Tree(moodleDataToTreeNode());
		ToolTipManager.sharedInstance().registerComponent(tree);
		tree.setCellRenderer(new MoodleCellRenderer());
	}

}
