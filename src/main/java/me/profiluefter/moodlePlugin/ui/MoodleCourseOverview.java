package me.profiluefter.moodlePlugin.ui;

import com.intellij.ui.treeStructure.Tree;
import me.profiluefter.moodlePlugin.moodle.Moodle;
import me.profiluefter.moodlePlugin.moodle.MoodleCourse;
import me.profiluefter.moodlePlugin.moodle.MoodleSection;
import me.profiluefter.moodlePlugin.moodle.modules.MoodleModule;
import me.profiluefter.moodlePlugin.plugin.MoodleData;
import me.profiluefter.moodlePlugin.plugin.MoodleSettings;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;

public class MoodleCourseOverview {
	private JPanel rootPanel;
	private Tree tree;
	private JButton reloadButton;

	public MoodleCourseOverview() {
		reloadButton.addActionListener(e -> {
			reloadData();
		});
	}

	public JPanel getRootPanel() {
		return rootPanel;
	}

	private void reloadData() {
		MoodleData.getInstance().refresh(() -> tree.setModel(new DefaultTreeModel(moodleDataToTreeNode())));
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
					if(nodeValue instanceof MoodleSection) {
						MoodleSection data = (MoodleSection) nodeValue;
						super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
					} else if(nodeValue instanceof MoodleModule) {
						MoodleModule data = (MoodleModule) nodeValue;
						super.getTreeCellRendererComponent(tree, data.getName(), sel, expanded, leaf, row, hasFocus);
						setIcon(new ImageIcon(data.getIconURL()));
					} else
						super.getTreeCellRendererComponent(tree, nodeValue, sel, expanded, leaf, row, hasFocus);
				} else
					super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				return this;
			}
		});
	}
}
