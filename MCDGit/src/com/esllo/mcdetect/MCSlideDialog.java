package com.esllo.mcdetect;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextField;

public class MCSlideDialog extends JDialog implements KeyListener {
	private static final long serialVersionUID = 20165126L;
	RangeSlider hSlide;
	RangeSlider sSlide;
	RangeSlider vSlide;
	JTextField[] values = new JTextField[6];
	MCDView mcd;

	public MCSlideDialog(MCDView mcd) {
		super(mcd, "MCD 슬라이더", Dialog.ModalityType.DOCUMENT_MODAL);
		this.mcd = mcd;
		initUI();
	}

	private void initUI() {
		setLayout(new FlowLayout());
		hSlide = new RangeSlider(0, 255);
		sSlide = new RangeSlider(0, 255);
		vSlide = new RangeSlider(0, 255);
		int[] posX = { 0, 0 };
		int[] posY = { 0, 0, 0 };
		int sizeX = 0, sizeY = 0;
		for (int i = 0; i < values.length; i++) {
			values[i] = new JTextField();
			values[i].setSize(sizeX, sizeY);
			values[i].setLocation(posX[i % 2], posY[i / 2]);
			values[i].addKeyListener(this);
			values[i].setInputVerifier(new InputVerifier() {

				@Override
				public boolean verify(JComponent input) {
					String t = ((JTextField) input).getText();
					t.replaceAll("[0-9]", "");
					return t.length() > 0 && t.length() < 3;
				}
			});
		}
		setVisible(true);
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
}
