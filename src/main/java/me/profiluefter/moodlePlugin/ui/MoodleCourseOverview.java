package me.profiluefter.moodlePlugin.ui;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.treeStructure.Tree;
import me.profiluefter.moodlePlugin.moodle.Moodle;
import me.profiluefter.moodlePlugin.moodle.MoodleCourse;
import me.profiluefter.moodlePlugin.moodle.MoodleSection;
import me.profiluefter.moodlePlugin.moodle.modules.MoodleLabelModule;
import me.profiluefter.moodlePlugin.moodle.modules.MoodleModule;
import me.profiluefter.moodlePlugin.moodle.modules.MoodlePageModule;
import me.profiluefter.moodlePlugin.moodle.modules.MoodleResourceModule;
import me.profiluefter.moodlePlugin.plugin.MoodleData;
import me.profiluefter.moodlePlugin.plugin.MoodleSettings;
import me.profiluefter.moodlePlugin.ui.moodleModules.MoodleModuleViewer;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

public class MoodleCourseOverview {
	private JPanel rootPanel;
	private Tree tree;
	private JButton reloadButton;

	public MoodleCourseOverview(Project project) {
		reloadButton.addActionListener(e -> reloadData());
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

	private void reloadData() {
		tree.setPaintBusy(true);
		MoodleData.getInstance().refresh().whenComplete((value, error) -> {
			tree.setModel(new DefaultTreeModel(moodleDataToTreeNode()));
			tree.setPaintBusy(false);
		});
	}

	private TreeNode moodleDataToTreeNode() {
		DefaultMutableTreeNode courseRoot = new DefaultMutableTreeNode("Course");

		Moodle data = MoodleData.getInstance().getData();
		if(data == null) return courseRoot;

		MoodleCourse course = data.getCourseById(MoodleSettings.getInstance().getCourseID(), false);

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
		tree = new Tree(moodleDataToTreeNode());
		tree.setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				if(value instanceof DefaultMutableTreeNode) {
					Object nodeValue = ((DefaultMutableTreeNode) value).getUserObject();
					if(nodeValue instanceof String && nodeValue.equals("Course")) {
						super.getTreeCellRendererComponent(tree, nodeValue, sel, expanded, leaf, row, hasFocus);
						setIcon(AllIcons.Nodes.Module);
					} else if(nodeValue instanceof MoodleSection) {
						MoodleSection data = (MoodleSection) nodeValue;
						super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
						setIcon(AllIcons.Nodes.Folder);
					} else if(nodeValue instanceof MoodleModule) {
						MoodleModule data = (MoodleModule) nodeValue;
						if(data instanceof MoodlePageModule) {
							super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
							setIcon(AllIcons.FileTypes.Text);
						} else if(data instanceof MoodleResourceModule) {
							super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
							setIcon(AllIcons.FileTypes.Custom);
						} else if(data instanceof MoodleLabelModule) {
							super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
							setIcon(null);
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
