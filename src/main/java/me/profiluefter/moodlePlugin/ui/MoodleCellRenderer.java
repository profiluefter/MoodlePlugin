package me.profiluefter.moodlePlugin.ui;

import com.intellij.icons.AllIcons;
import me.profiluefter.moodlePlugin.moodle.MoodleCourse;
import me.profiluefter.moodlePlugin.moodle.MoodleSection;
import me.profiluefter.moodlePlugin.moodle.modules.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

class MoodleCellRenderer extends DefaultTreeCellRenderer {
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
}
