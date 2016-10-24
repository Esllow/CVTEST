package com.esllo.mcdetect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import com.xuggle.mediatool.IMediaListener;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAddStreamEvent;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.ICloseCoderEvent;
import com.xuggle.mediatool.event.ICloseEvent;
import com.xuggle.mediatool.event.IFlushEvent;
import com.xuggle.mediatool.event.IOpenCoderEvent;
import com.xuggle.mediatool.event.IOpenEvent;
import com.xuggle.mediatool.event.IReadPacketEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.mediatool.event.IWriteHeaderEvent;
import com.xuggle.mediatool.event.IWritePacketEvent;
import com.xuggle.mediatool.event.IWriteTrailerEvent;
import com.xuggle.xuggler.IError;

public class MCDView extends JFrame implements ActionListener, IMediaListener {
	private static final long serialVersionUID = 20165126L;
	// URL input for RTSP
	JTextField urlField;
	// flag for drawing
	public boolean running = false;

	// image/frame draw panel
	private JDraw draw;
	JPanel layout;

	public MCDView() {
		super("MCD");
		initUI();
	}

	// GUI
	private void initUI() {
		try {
			// Windows Default UI
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel wrap = new JPanel();
		wrap.setPreferredSize(new Dimension(800, 330));
		wrap.setLayout(null);
		draw = new JDraw();
		draw.setSize(new Dimension(400, 300));
		draw.setLocation(0, 0);
		JPanel pn1 = new JPanel();
		pn1.setLayout(null);
		pn1.setSize(new Dimension(400, 30));
		pn1.setLocation(0, 300);
		urlField = new JTextField();
		urlField.setSize(new Dimension(218, 24));
		urlField.setLocation(3, 3);
		JButton conBtn = new JButton("연결");
		JButton locBtn = new JButton("파일열기");
		conBtn.setSize(new Dimension(86, 26));
		locBtn.setSize(new Dimension(86, 26));
		conBtn.setLocation(224, 2);
		locBtn.setLocation(312, 2);
		conBtn.addActionListener(this);
		locBtn.addActionListener(this);
		conBtn.setActionCommand("oplink");
		locBtn.setActionCommand("opfile");
		pn1.add(urlField);
		pn1.add(conBtn);
		pn1.add(locBtn);
		JPanel pn2 = new JPanel();
		pn2.setSize(new Dimension(400, 30));
		layout = new JPanel();
		layout.setLayout(new BoxLayout(layout, BoxLayout.Y_AXIS));
		layout.add(new MCDItemPane(this).setValues("BLUE", CV.ColorValue.blueLow.val, CV.ColorValue.blueHigh.val));
		layout.add(new MCDItemPane(this).setValues("GREEN", CV.ColorValue.greenLow.val, CV.ColorValue.greenHigh.val));
		layout.add(new MCDItemPane(this).setValues("YELLOW", CV.ColorValue.yellowLow.val,
				CV.ColorValue.yellowHigh.val));
		layout.add(new MCDItemPane(this).setValues("RED", CV.ColorValue.redLow.val, CV.ColorValue.redHigh.val));
		layout.add(new MCDItemPane(this).setValues("WHITE", CV.ColorValue.whiteLow.val, CV.ColorValue.whiteHigh.val));
		JScrollPane scrollPane = new JScrollPane(layout, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setSize(400, 330);
		scrollPane.setLocation(400, 0);
		scrollPane.getViewport().setMinimumSize(new Dimension(400, 330));

		wrap.add(draw);
		wrap.add(pn1);
		wrap.add(scrollPane);
		setLayout(new BorderLayout());
		add(wrap);
		setResizable(false);
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "oplink": // RTSP
			running = false;
			String url = urlField.getText().toString().trim();
			String pat = "^(rtsp://)[a-zA-Z0-9]*:[a-zA-Z0-9]*@[\\d]{1,3}[\\.][\\d]{1,3}[\\.][\\d]{1,3}[\\.][\\d]{1,3}";
			Pattern p = Pattern.compile(pat);
			Matcher m = p.matcher(url);
			if (m.find())
				runRtspThread(url);
			break;
		case "opfile": // File (img/video) - video 개발중
			running = false;
			JFileChooser fc = new JFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(new FileNameExtensionFilter("이미지", "png", "jpeg", "jpg", "gif", "bmp"));
			fc.addChoosableFileFilter(new FileNameExtensionFilter("비디오", "mp4"));
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				if (fc.getFileFilter().getDescription().equals("이미지")) {
					BufferedImage img;
					try {
						img = ImageIO.read(file);
						if (img != null) {
							runImageThread(img);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} else
					runRtspThread(file.getAbsolutePath());
			} else {
			}
			break;
		}
	}

	
	// run Thread with image
	public void runImageThread(final BufferedImage bi) {
		new Thread(new Runnable() {
			public void run() {
				running = true;
				while (running) {
					updateImage(bi);
				}
			}
		}).start();
	}

	// rtsp://****:****@***.***.***.***
	// run Thread with video
	public void runRtspThread(final String url) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				IMediaReader read = ToolFactory.makeReader(url);
				read.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
				read.setQueryMetaData(false);
				read.addListener(MCDView.this);
				running = true;
				while (running) {
					IError err = null;
					if (read != null)
						err = read.readPacket();
					if (err != null) {
						System.out.println("err" + err);
						break;
					}
				}
			}
		}).start();
	}

	@Override
	public void onAddStream(IAddStreamEvent arg0) {
	}

	@Override
	public void onAudioSamples(IAudioSamplesEvent arg0) {
	}

	@Override
	public void onClose(ICloseEvent arg0) {
	}

	@Override
	public void onCloseCoder(ICloseCoderEvent arg0) {
	}

	@Override
	public void onFlush(IFlushEvent arg0) {
	}

	@Override
	public void onOpen(IOpenEvent arg0) {
	}

	@Override
	public void onOpenCoder(IOpenCoderEvent arg0) {
	}

	@Override
	public void onReadPacket(IReadPacketEvent arg0) {
	}

	@Override
	public void onVideoPicture(IVideoPictureEvent arg0) {
		try {
			BufferedImage bi = arg0.getImage(); // get BufferedImage
			if (bi != null && running)
				update(bi); // update Image/Frame
		} catch (Exception e) {

		}
	}

	public void updateImage(BufferedImage bi) {
		Mat origin = CV.toMat(bi);
		// check tag available
		for (int i = 0; i < 5; i++) {
			if (((MCDItemPane) layout.getComponent(i)).isChecked()) {
				Scalar lower = ((MCDItemPane) layout.getComponent(i)).getLower();
				Scalar upper = ((MCDItemPane) layout.getComponent(i)).getUpper();
				Mat mat = CV.hsvIn(bi, lower, upper);
				CV.findContour(mat, origin, ((MCDItemPane) layout.getComponent(i)).getTag());
			}
		}
		bi = CV.toBufferedImage(origin);
		// draw to panel
		draw.update(bi);
	}

	public void update(BufferedImage bi) {
		if (running)
			updateImage(bi);
	}

	@Override
	public void onWriteHeader(IWriteHeaderEvent arg0) {
	}

	@Override
	public void onWritePacket(IWritePacketEvent arg0) {
	}

	@Override
	public void onWriteTrailer(IWriteTrailerEvent arg0) {
	}

}

class JDraw extends JPanel {
	private static final long serialVersionUID = 20165126L;
	BufferedImage image = null;

	public JDraw() {
		image = new BufferedImage(400, 300, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		g.setPaint(new Color(0, 0, 0));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
	}

	public void update(BufferedImage image) {
		this.image = image;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null)
			g.drawImage(image, 0, 0, 400, 300, null);
	}
}