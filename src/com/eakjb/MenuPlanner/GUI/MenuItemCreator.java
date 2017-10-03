package com.eakjb.MenuPlanner.GUI;
import java.awt.*;
import java.awt.event.*;

import com.eakjb.ExceptionHandeler.ExceptionHandeler;
import com.eakjb.MenuPlanner.utill;
import com.eakjb.MenuPlanner.Objects.*;
import com.eakjb.MenuPlanner.Objects.MenuItem;
import javax.swing.*;
import javax.swing.text.JTextComponent;
public class MenuItemCreator {
	private boolean currentWindowClosed = false;
	private Thread updater = new updater();
	public static FoodType[] foodTypes = FoodType.values();
	JFrame frame = new JFrame();
	JPanel mainPanel = new JPanel(new GridLayout(7 ,2));
	JPanel southPanel = new JPanel(new GridLayout(1, 2));
	JPanel checkBoxPanel = new JPanel();
	JButton submit = new JButton("Submit");
	JButton close = new JButton("Cancel");
	JLabel ratingsLabel = new JLabel("Rating: ");
	JSpinner ratingsBox = new JSpinner(new SpinnerNumberModel(0, 0, MenuItem.highestRating, 1));
	JLabel diffucultysBoxLabel = new JLabel("Difficulty To Cook: ");
	JSpinner diffacultysBox = new JSpinner(new SpinnerNumberModel(0, 0, MenuItem.highestHardness, 1));
	JLabel foodTypesBoxLabel = new JLabel("Food Type");
	JComboBox foodTypesBox = new JComboBox(foodTypes);
	JLabel nameFieldLabel = new JLabel("Title: ");
	JTextField nameField = new JTextField(20);
	JLabel descriptionField1Label = new JLabel("Description Line One: ");
	JTextField descriptionField1 = new JTextField(20);
	JLabel descriptionField2Label = new JLabel("Description Line Two: ");
	JTextField descriptionField2 = new JTextField(20);
	JLabel safeWithAllLabel = new JLabel("Safe with all: ");
	JCheckBox safeWithAll = new JCheckBox();
	JPanel leaveInMenuHolder = new JPanel();
	submitListener listener = new submitListener();
	private MenuItem item;
	public MenuItemCreator() {
		
		frame.setSize(400, 220);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocation(360, 260);
		frame.setTitle("Untitled");
		
		foodTypesBox.setMaximumRowCount(FoodType.values().length);
		
		frame.add(BorderLayout.CENTER, mainPanel);
		frame.add(BorderLayout.SOUTH, southPanel);
		mainPanel.add(nameFieldLabel);
		mainPanel.add(nameField);
		mainPanel.add(descriptionField1Label);
		mainPanel.add(descriptionField1);
		mainPanel.add(descriptionField2Label);
		mainPanel.add(descriptionField2);
		mainPanel.add(ratingsLabel);
		mainPanel.add(ratingsBox);
		mainPanel.add(diffucultysBoxLabel);
		mainPanel.add(diffacultysBox);
		mainPanel.add(foodTypesBoxLabel);
		mainPanel.add(foodTypesBox);
		mainPanel.add(safeWithAllLabel);
		mainPanel.add(checkBoxPanel);
		
		southPanel.add(submit);
		southPanel.add(close);
		
		checkBoxPanel.add(safeWithAll);
		
		submit.addActionListener(listener);
		close.addActionListener(new closeButtonListener());
		foodTypesBox.addActionListener(new typeBoxListener(foodTypesBox, safeWithAll));
		frame.addWindowListener(new closeListener());
		nameField.addKeyListener(new textFieldListener());
		descriptionField1.addKeyListener(new textFieldListener());
		descriptionField2.addKeyListener(new textFieldListener());
		nameField.addFocusListener(new textFieldFocusListener(nameField));
		descriptionField1.addFocusListener(new textFieldFocusListener(descriptionField1));
		descriptionField2.addFocusListener(new textFieldFocusListener(descriptionField2));
	}
	public MenuItem go() {	
		updater.start();
		frame.setVisible(true);
		MenuItem toReturn = getMenuItem();
		frame.setVisible(false);
		return toReturn;
	}
	private void submit() {
		if (!nameField.getText().equals("")) {
			String name = nameField.getText();
			String description = descriptionField1.getText() + "\n " + descriptionField2.getText();
			FoodType type = (FoodType) foodTypesBox.getSelectedItem();
			Integer hardnessObj = (Integer) diffacultysBox.getValue();
			Integer ratingObj = (Integer) ratingsBox.getValue();
			int hardness = hardnessObj.intValue();
			int rating = ratingObj.intValue();
			boolean safeWithAllBool = safeWithAll.isSelected();
			item = new MenuItem(name, description, hardness, type, rating, safeWithAllBool);
			
		} else {
			utill.AWTToolKit.beep();
		}
	}
	private synchronized MenuItem getMenuItem() {
		while(item == null && !currentWindowClosed) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				ExceptionHandeler.HandelException(e, false);
			}
		}
		return item;
	}
	private class updater extends Thread {
		public void run() {
			while(true) {
				String lastTitle = nameField.getText();
				while(lastTitle == nameField.getText()) {
					try {
						Thread.sleep(1000);
					} catch(InterruptedException ex) {
						ExceptionHandeler.HandelException(ex, false);
					}
				}
				if(nameField.getText().equals("")) {
					frame.setTitle("Untitled");
				} else {
					frame.setTitle(nameField.getText());
				}
			}
		}
	}
	private class textFieldListener implements KeyListener {

		public void keyPressed(KeyEvent arg0) {}

		public void keyReleased(KeyEvent arg0) {}

		public void keyTyped(KeyEvent arg0) {
			if (arg0.getKeyChar() == '^' || arg0.getKeyChar() == '/' || arg0.getKeyChar() == '*' || arg0.getKeyChar() == '(' || arg0.getKeyChar() == ')' ) {
				utill.AWTToolKit.beep();
				utill.robot.keyPress(KeyEvent.VK_BACK_SPACE);
				utill.robot.keyRelease(KeyEvent.VK_BACK_SPACE);
			}
		}
		
	}
	private class textFieldFocusListener implements FocusListener {
		private final JTextComponent comp;
		textFieldFocusListener(JTextComponent comp) {
			this.comp = comp;
		}
		public void focusGained(FocusEvent arg0) {}

		public void focusLost(FocusEvent arg0) {
			comp.setCaretPosition(0);
		}
		
	}
	private class typeBoxListener implements ActionListener {
		private final JComboBox target;
		private final JCheckBox safeWithAll;
		typeBoxListener(JComboBox target, JCheckBox safeWithAll) {
			this.target = target;
			this.safeWithAll = safeWithAll;
		}
		public void actionPerformed(ActionEvent arg0) {
			FoodType selectedItem = (FoodType) target.getSelectedItem();
			safeWithAll.setSelected(selectedItem.isSafeWithAll());
		}
		
	}
	private class closeButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			currentWindowClosed = true;
		}
		
	}
	private class closeListener implements WindowListener {
		public void windowActivated(WindowEvent arg0) {}
		public void windowClosed(WindowEvent arg0) {}
		public void windowClosing(WindowEvent arg0) {
			currentWindowClosed = true;			
		}
		public void windowDeactivated(WindowEvent arg0) {}
		public void windowDeiconified(WindowEvent arg0) {}
		public void windowIconified(WindowEvent arg0) {}
		public void windowOpened(WindowEvent arg0) {}
	}
	private class submitListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			submit();
		}
	}
}
