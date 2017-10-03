package com.eakjb.MenuPlanner.GUI;
import java.awt.*;
import java.awt.event.*;

import com.eakjb.MenuPlanner.Objects.MenuItem;
import javax.swing.*;
public class MenuItemDisplayer { 
	private final MenuItem item;
	private final int closeOperation;
	JFrame frame = new JFrame();
	JButton closeButton = new JButton("Close");
	JButton[] ratingButtons;
	JTabbedPane pane = new JTabbedPane();
	JPanel mainPanel = new JPanel(new GridLayout(4, 1));
	JPanel infoPanel = new JPanel(new GridLayout(4, 1));
	JPanel ratingButtonsPanel = new JPanel();
	JPanel ratingPanel = new JPanel(new GridLayout(2, 1));
	JScrollPane mainScroller = new JScrollPane(mainPanel);
	JTextArea notes = new JTextArea(20, 10);
	JScrollPane notesScroller = new JScrollPane(notes);
	JTextArea instructions = new JTextArea(20, 10);
	JScrollPane instructionsScroller = new JScrollPane(instructions);
	JLabel title;
	JLabel description;
	JLabel rating;
	JLabel type;
	JLabel ratingButtonsLabel = new JLabel("Change Rating: ");
	Font titleFont;
	JPanel dishPanel = new JPanel(new GridLayout(3, 1));
	JScrollPane dishScroller = new JScrollPane(dishPanel);
	JPanel buttonHolder = new JPanel();
	JButton addButton;
	JLabel dishLabel;
	JList dishCompList = new JList();
	public MenuItemDisplayer(MenuItem item, int titleSize, int defaultCloseOperation) {
		this.item = item;
		this.closeOperation = defaultCloseOperation;
		this.ratingButtons = item.getRatingButtons();
		
		frame.setTitle(item.getTitle());
		frame.setDefaultCloseOperation(defaultCloseOperation);
		frame.setSize(500, 400);
		frame.setLocation(360, 280);
		
		mainScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		mainScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		notesScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		notesScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);	
		
		instructionsScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		instructionsScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		closeButton.addActionListener(new closeButtonListener());
		notes.addKeyListener(new notesListener());
		notes.addFocusListener(new notesFocusListener());
		instructions.addKeyListener(new instructionsListener());
		instructions.addFocusListener(new instructionsFocusListener());
		
		description = item.getDescriptionLabel();		
		title = item.getTitleLabel();
		rating = item.getRatingLabel();
		description = item.getDescriptionLabel();
		type = item.getTypeLabel();
		titleFont = new Font("Title-Font", Font.BOLD, titleSize);
		title.setFont(titleFont);
		notes.setText(item.getNotes());
		instructions.setText(item.getInstructions());
		
		for(int i = 0; i < ratingButtons.length; i++) {
			ratingButtonsPanel.add(ratingButtons[i]);
		}
		
		frame.add(BorderLayout.NORTH, title);
		frame.add(BorderLayout.CENTER, pane);
		frame.add(BorderLayout.SOUTH, closeButton);
		
		pane.add("Main", mainScroller);
		pane.add("Instructions", instructionsScroller);
		pane.add("Notes", notesScroller);
		
		infoPanel.add(description);
		infoPanel.add(rating);
		infoPanel.add(type);
		
		ratingPanel.add(ratingButtonsLabel);
		ratingPanel.add(ratingButtonsPanel);
		
		mainPanel.add(infoPanel);
		mainPanel.add(ratingPanel);
	}
	public MenuItemDisplayer(MenuItem item, int titleSize) {
		this(item, titleSize, JFrame.DISPOSE_ON_CLOSE);
	}
	public MenuItemDisplayer(MenuItem item) {
		this(item, 36);
	}
	public MenuItem getItem() {
		return item;
	}
	public void go() {
		frame.setVisible(true);
	}
	public void stop() {
		frame.setVisible(false);
	}
	public boolean toggle() {
		frame.setVisible(!frame.isVisible());
		return frame.isVisible();
	}
	private class notesListener implements KeyListener {

		public void keyPressed(KeyEvent arg0) {}

		public void keyReleased(KeyEvent arg0) {}

		public void keyTyped(KeyEvent arg0) {
			if (notes.getText() != null && !notes.getText().equals("")) {
				item.setNotes(notes.getText());
			}
		}
		
	}
	private class instructionsListener implements KeyListener {

		public void keyPressed(KeyEvent arg0) {}

		public void keyReleased(KeyEvent arg0) {}

		public void keyTyped(KeyEvent arg0) {
			if (instructions.getText() != null && !instructions.getText().equals("")) {
				item.setInstructions(instructions.getText());
			}
		}
		
	}
	private class notesFocusListener implements FocusListener {

		public void focusGained(FocusEvent arg0) {}

		public void focusLost(FocusEvent arg0) {
			if (notes.getText() == null || notes.getText().equals("")) {
				notes.setText("(none)");
			}
		}
		
	}
	private class instructionsFocusListener implements FocusListener {

		public void focusGained(FocusEvent arg0) {}

		public void focusLost(FocusEvent arg0) {
			if (instructions.getText() == null || instructions.getText().equals("")) {
				instructions.setText("(none)");
			}
		}
		
	}
	private class closeButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			stop();
			if (closeOperation == JFrame.EXIT_ON_CLOSE) {
				System.exit(0);
			}
		}
		
	}
 }
