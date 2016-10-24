package com.esllo.mcdetect;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JPanel;

public class MCDItemLayout extends JPanel {
	private static final long serialVersionUID = 20165126L;

	private int rheight;
	ArrayList<Component> list;

	public MCDItemLayout() {
		list = new ArrayList<Component>();
		setLayout(null);
		setSize(400, 330);
	}

	public int getHeight() {
		return rheight;
	}

	public void setRowHeight(int rheight) {
		this.rheight = rheight;
	}

	public void addItem(Component comp) {
		list.add(comp);
		// resize();
		comp.setLocation(0, list.size() * rheight);
		add(comp);
	}

	public void resize() {
		removeAll();
		// setSize(new Dimension(400, rheight * list.size()));
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setLocation(0, rheight * i);
			add(list.get(i));
		}
	}

}
