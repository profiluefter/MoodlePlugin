package me.profiluefter.moodlePlugin.ui;

import com.intellij.icons.AllIcons;
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
import java.awt.*;

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
		tree.setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				if(value instanceof DefaultMutableTreeNode) {
					Object nodeValue = ((DefaultMutableTreeNode) value).getUserObject();
					if(nodeValue instanceof MoodleCourse) {
						MoodleCourse data = (MoodleCourse) nodeValue;
						super.getTreeCellRendererComponent(tree, data.getFullName(), sel, expanded, leaf, row, hasFocus);
						setIcon(AllIcons.Nodes.ModuleGroup);
					} else if(nodeValue instanceof MoodleSection) {
						MoodleSection data = (MoodleSection) nodeValue;
						super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
						setIcon(AllIcons.Nodes.Folder);
						setToolTipText(data.getSummary());
					} else if(nodeValue instanceof MoodleModule) {
						MoodleModule data = (MoodleModule) nodeValue;
						if(data instanceof MoodlePageModule) {
							super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
							setIcon(AllIcons.FileTypes.Text);
						} else if(data instanceof MoodleResourceModule) {
							super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
							setIcon(AllIcons.Actions.Download);
						} else if(data instanceof MoodleLabelModule) {
							super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
							setIcon(null);
						} else if(data instanceof MoodleAssignModule) {
							super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
							setIcon(AllIcons.Modules.SourceRoot);
						} else if(data instanceof MoodleURLModule) {
							super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
							setIcon(AllIcons.Actions.OpenNewTab);
						} else if(data instanceof MoodleUnknownModule) {
							super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
							setIcon(AllIcons.FileTypes.Unknown);
							setToolTipText("Unknown Module ID: "+((MoodleUnknownModule) data).getModuleName());
						} else {
							super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
							setIcon(AllIcons.FileTypes.Unknown);
						}
					} else
						super.getTreeCellRendererComponent(tree, nodeValue, sel, expanded, leaf, row, hasFocus);
				} else
					super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				return this;
			}
		});
	}
}
