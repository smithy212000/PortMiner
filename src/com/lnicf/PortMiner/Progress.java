package com.lnicf.PortMiner;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

public class Progress {
	// Create JFrame, container, progress bar and border.
	public static JFrame frame = new JFrame("PortMiner");
	private static Container cont = frame.getContentPane();
	private static JProgressBar pBar = new JProgressBar();
	private static Border border = BorderFactory.createTitledBorder("Starting PortMiner...");

	public static void setupFrame() {
		// Setup the frame, size, what to do on close, and setup the progress
		// bar and border.
		Progress.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		Progress.pBar.setValue(0);
		Progress.pBar.setStringPainted(true);
		Progress.pBar.setBorder(Progress.border);
		Progress.cont.add(Progress.pBar, BorderLayout.NORTH);
		Progress.frame.setSize(300, 100);
		Progress.frame.setVisible(true);
	}

	public static void closeFrame() {
		// Close the frame.
		frame.setVisible(false);
		frame.setEnabled(false);
	}

	public static void setProgress(String a, int percentage) {
		// Set the value of the progress bar to the second argument
		pBar.setValue(percentage);
		// Create a border with the title set by the first argument
		border = BorderFactory.createTitledBorder(a);
		// Apply the border to the progress bar
		pBar.setBorder(border);
		// Add the progress bar to the container
		cont.add(pBar, BorderLayout.NORTH);
		// Revalidate the container
		cont.revalidate();
		// Repaint container to show the new title for the border
		cont.repaint();
	}

}
