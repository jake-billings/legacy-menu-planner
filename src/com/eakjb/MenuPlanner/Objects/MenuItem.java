package com.eakjb.MenuPlanner.Objects;
import com.eakjb.MenuPlanner.utill;
import com.eakjb.MenuPlanner.GUI.*;

import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
public class MenuItem {
	public static List<MenuItem> allItems = new ArrayList<MenuItem>();
	public static int highestRating = 5;
	public static int highestHardness = 10;
	private final JButton[] ratingButtons = new JButton[5];
	private final String title;
	private final String description;
	private final int hardnessToCook;
	private final boolean safeWithAll;
	private final FoodType type;
	private final JLabel titleLabel;
	private final JLabel descriptionLabel;
	private final JLabel ratingLabel;
	private final JLabel typeLabel;
	private int rating;
	private String notes = "(none)";
	private String instructions = "(none)";
	public MenuItem(String title, String description, int hardnessToCook, FoodType type, int rating, boolean safeWithAll) {
		this.title = title;
		this.description = description;
		this.hardnessToCook = hardnessToCook;
		this.type = type;
		this.safeWithAll = safeWithAll;
		this.rating = rating;
		this.titleLabel = new JLabel(title);
		this.descriptionLabel = new JLabel(getTitle() + " is " + description);
		this.ratingLabel = new JLabel(getTitle() + " is rated " + String.valueOf(rating));
		this.typeLabel = new JLabel(getTitle() + " is a " + type.toString());
		for(int i = 0; i < ratingButtons.length; i++) {
			ratingButtons[i] = new JButton(String.valueOf(i+1));
			ratingButtons[i].addActionListener(new ratingButtonListener(i+1));
		}
		allItems.add(this);	
		utill.saved = false;
	}
	public MenuItem(String title, String description, FoodType type) {
		this(title, description, 5, type);
	}
	public MenuItem(String title, String description, int hardnessToCook, FoodType type) {
		this(title, description, hardnessToCook, type, 3, type.isSafeWithAll());
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	public int getHardnessToCook() {
		return hardnessToCook;
	}
	public boolean isSafeWithAll() {
		return safeWithAll;
	}
	public FoodType getType() {
		return type;
	}
	public JLabel getTitleLabel() {
		return titleLabel;
	}
	public void writeObject(Writer stream, char fieldSeperator, char objSeperator) throws IOException {
		if (stream == null) {
			throw new IllegalArgumentException(new NullPointerException("Stream must not be null!"));
		}
		StringBuffer buff = new StringBuffer();
		buff.append(title);
		buff.append(fieldSeperator);
		buff.append(description);
		buff.append(fieldSeperator);
		buff.append(rating);
		buff.append(fieldSeperator);
		buff.append(type.name());
		buff.append(fieldSeperator);
		buff.append(hardnessToCook);
		buff.append(fieldSeperator);
		buff.append(safeWithAll);
		buff.append(fieldSeperator);
		buff.append(notes);
		buff.append(fieldSeperator);
		buff.append(instructions);
		buff.append(objSeperator);
		stream.write(buff.toString() + "\n");
	}
	public void writeObject(Writer stream) throws IOException {
		writeObject(stream, '/', '^');
	}
	public static MenuItem[] readObjects(Reader stream, char fieldSeperator, char objSeperator) throws IOException, ClassNotFoundException {
		if (stream == null) {
			throw new IllegalArgumentException(new NullPointerException("Stream must not be null!"));
		}
		BufferedReader reader = new BufferedReader(stream);
		ArrayList<String> lines = new ArrayList<String>();
		while (reader.ready()) {
			lines.add(reader.readLine());
		}
		StringBuffer buff = new StringBuffer();
		for(String str : lines) {
			buff.append(str + "\n");
		}
		String fileContents = buff.toString();
		String[] objectStrings = fileContents.split(String.valueOf(objSeperator));
		ArrayList<MenuItem> items = new ArrayList<MenuItem>();
		for (String objectString : objectStrings) {
			String[] details = objectString.split(String.valueOf(fieldSeperator));
			if (details.length < 8) {
				break;
			}
			boolean isSafeWithAll;
			if (details.length > 8) {
				JOptionPane.showMessageDialog(null, "Some data may be lost.");
			}
			isSafeWithAll = Boolean.parseBoolean(details[5]);
			MenuItem item = null;
			item = new MenuItem(details[0], details[1], Integer.parseInt(details[4]), FoodType.valueOf(details[3]), Integer.parseInt(details[2]), isSafeWithAll);
			item.setNotes(details[6]);
			item.setInstructions(details[7]);
			if(item != null) {
				items.add(item);
			}
			
		}
		MenuItem[] toReturn = new MenuItem[items.size()];
		for (int i = 0; i < items.size(); i++) {
			MenuItem toAdd = items.get(i);
			toReturn[i] = toAdd;
		}
		return toReturn;
	}
	public static MenuItem[] readObjects(Reader stream) throws IOException, ClassNotFoundException {
		return readObjects(stream, '/', '^');
	}
	public JButton getNewLinkedButton(String buttonTitle) {
		JButton toReturn = new JButton(buttonTitle);
		toReturn.addActionListener(new linkedButtonListener(this));
		return toReturn;
	}
	public JButton getNewLinkedButton() {
		return getNewLinkedButton(title);
	}
	public JLabel getDescriptionLabel() {
		return descriptionLabel;
	}
	public void setRating(int rating) {
		this.rating = rating;
		this.ratingLabel.setText(getTitle() + " is rated " + String.valueOf(rating));
	}
	public int getRating() {
		return rating;
	}
	public JLabel getRatingLabel() {
		return ratingLabel;
	}
	public JButton[] getRatingButtons() {
		return ratingButtons;
	}
	public String toString() {
		String toReturn;
		StringBuffer buff  = new StringBuffer();
		buff.append(title);
		if (safeWithAll) {
			buff.append(" (Safe)");
		} else {
			buff.append(" (Unsafe)");
		}
		toReturn = buff.toString();
		return toReturn;
	}
	public JLabel getTypeLabel() {
		return typeLabel;
	}
	public void setNotes(String notes) {
		if (notes != null && !notes.equals("")) {
			this.notes = notes;
		}
	}
	public String getNotes() {
		return notes;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	public String getInstructions() {
		return instructions;
	}
	private class linkedButtonListener implements ActionListener {
		private final MenuItemDisplayer displayer;
		linkedButtonListener(MenuItem item) {
			displayer = new MenuItemDisplayer(item);
		}
		public void actionPerformed(ActionEvent arg0) {
			displayer.go();
		}
		
	}
	private class ratingButtonListener implements ActionListener {
		private final int number;
		ratingButtonListener(int number) {
			this.number = number; 
		}
		public void actionPerformed(ActionEvent arg0) {
			setRating(number);	
		}
		
	}
}
