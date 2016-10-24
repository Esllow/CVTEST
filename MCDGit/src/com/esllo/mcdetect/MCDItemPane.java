package com.esllo.mcdetect;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Scalar;

public class MCDItemPane extends JPanel implements KeyListener, ChangeListener {
	private static final long serialVersionUID = 20165126;
	RangeSlider hSlide;
	RangeSlider sSlide;
	RangeSlider vSlide;
	JTextPane tagField;
	JCheckBox checked;
	JTextField[] values = new JTextField[6];
	MCDView mcd;

	public MCDItemPane(MCDView mcd) {
		this.mcd = mcd;
		this.initUI();
	}

	private void initUI() {
		this.setSize(400, 66);
		this.setMaximumSize(new Dimension(800, 66));
		this.setLayout(null);
		this.setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128), 1));
		this.tagField = new JTextPane();
		this.tagField.setSize(170, 30);
		this.tagField.setBackground(this.getBackground());
		this.tagField.setLocation(0, 0);
		this.tagField.setCaretColor(this.getBackground());
		this.checked = new JCheckBox("");
		this.checked.setSelected(true);
		this.checked.setSize(30, 30);
		this.checked.setLocation(170, 0);
		this.checked.addChangeListener((ChangeListener) new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				
			}
		});
		this.hSlide = new RangeSlider(0, 179);
		this.sSlide = new RangeSlider(0, 255);
		this.vSlide = new RangeSlider(0, 255);
		this.hSlide.setSize(200, 30);
		this.sSlide.setSize(200, 30);
		this.vSlide.setSize(200, 30);
		this.hSlide.setLocation(200, 0);
		this.sSlide.setLocation(0, 30);
		this.vSlide.setLocation(200, 30);
		this.hSlide.addChangeListener((ChangeListener) this);
		this.sSlide.addChangeListener((ChangeListener) this);
		this.vSlide.addChangeListener((ChangeListener) this);
		this.add(this.tagField);
		this.add(this.checked);
		this.add((Component) this.hSlide);
		this.add((Component) this.sSlide);
		this.add((Component) this.vSlide);
		int[] arrn = new int[2];
		arrn[0] = 200;
		int[] posX = arrn;
		int posY = 0;
		int sizeX = 40;
		int sizeY = 30;
		int i = 0;
		while (i < this.values.length) {
			this.values[i] = new JTextField();
			this.values[i].setSize(sizeX, sizeY);
			this.values[i].setLocation(2 + posX[i % 2] + i * (sizeX + 2), posY);
			this.values[i].addKeyListener(this);
			this.values[i].setInputVerifier((InputVerifier) new InputVerifier() {

				@Override
				public boolean verify(JComponent input) {
					
					return true;
				}
			});
			++i;
		}
	}

	public boolean isChecked() {
		return this.checked.isSelected();
	}

	public MCDItemPane setValues(String tag, double[] v, double[] v2) {
		this.tagField.setText(tag);
		this.hSlide.setValue((int) v[0]);
		this.hSlide.setUpperValue((int) v2[0]);
		this.sSlide.setValue((int) v[1]);
		this.sSlide.setUpperValue((int) v2[1]);
		this.vSlide.setValue((int) v[2]);
		this.vSlide.setUpperValue((int) v2[2]);
		return this;
	}

	public Scalar getLower() {
		return new Scalar((double) this.hSlide.getValue(), (double) this.sSlide.getValue(), (double) this.vSlide
				.getValue());
	}

	public Scalar getUpper() {
		return new Scalar((double) this.hSlide.getUpperValue(), (double) this.sSlide.getUpperValue(),
				(double) this.vSlide.getUpperValue());
	}

	public String getTag() {
		return this.tagField.getText();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void stateChanged(ChangeEvent e) {
	}
}