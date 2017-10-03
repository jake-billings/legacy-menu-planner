package com.eakjb.MenuPlanner.GUI;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.eakjb.MenuPlanner.Objects.MenuItem;
import com.eakjb.ExceptionHandeler.ExceptionHandeler;
import com.eakjb.MenuPlanner.*;
public class MainGUI {
	updater updaterTarget = new updater();
	Thread updaterThread = new Thread(updaterTarget, "Updater-Thread");
	List<MenuItem> menuItems = utill.menuItems;
	public final JFrame frame = new JFrame("Menu Planner " + utill.version);
	JPanel mainPanel = new JPanel(new GridLayout(1, 2));
	JPanel openPanel = new JPanel();
	JPanel openButtonsPanel = new JPanel();
	JPanel outputPanel = new JPanel();
	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();
	JSplitPane itemsOpenSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	JButton openItemButton = new JButton("Open");
	JComboBox menuItemsBox = new JComboBox(menuItems.toArray());
	JList menuItemsList = new JList();
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	JMenu viewMenu = new JMenu("View");
	JMenuItem[] fileMenuItems = {new JMenuItem("New..."), new JMenuItem("Open..."), new JMenuItem("Save..."), new JMenuItem("Save and Quit..."),  new JMenuItem("Quit")};
	JComponent[] viewMenuItems = {new JCheckBoxMenuItem("Show hidden files"), new JSeparator(), new JMenuItem("Clear Items"), new JSeparator(), new JMenuItem("Reset Slider")};
	public MainGUI() {
		frame.setSize(400, 380);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(utill.AWTToolKit.getScreenSize().width / 2 - 220, utill.AWTToolKit.getScreenSize().height / 2 - 220);
		
		itemsOpenSplit.setDividerLocation(frame.getWidth() / 3);
		
		rightPanel.setMinimumSize(new Dimension(frame.getWidth() / 3, frame.getHeight() / 3));
		leftPanel.setMinimumSize(new Dimension(frame.getWidth() / 3, frame.getHeight() / 3));	
		
		menuItemsList.setListData(menuItems.toArray());
		
		menuItemsList.addListSelectionListener(new listListener());
		
		fileMenuItems[4].addActionListener(new quitListener());
		fileMenuItems[3].addActionListener(new saveAndQuitListener());
		fileMenuItems[2].addActionListener(new saveFileListener());
		fileMenuItems[1].addActionListener(new openFileListener());
		fileMenuItems[0].addActionListener(new newItemListener());
		
		((AbstractButton) viewMenuItems[0]).addItemListener(new showFilesListener());
		((AbstractButton) viewMenuItems[2]).addActionListener(new clearListener());
		((AbstractButton) viewMenuItems[4]).addActionListener(new resetSliderListener());
		
		openItemButton.addActionListener(new openItemListener());
		
		frame.addComponentListener(new resizeListener());

		for(JMenuItem toAdd : fileMenuItems) {
			fileMenu.add(toAdd);
		}
		for(JComponent toAdd : viewMenuItems) {
			viewMenu.add(toAdd);
		}
		menuBar.add(fileMenu);
		menuBar.add(viewMenu);
		frame.setJMenuBar(menuBar);
		
		outputPanel.add(menuItemsList);
		
		openButtonsPanel.add(openItemButton);

		openPanel.add(menuItemsBox);
		openPanel.add(openButtonsPanel);

		itemsOpenSplit.setLeftComponent(leftPanel);
		itemsOpenSplit.setRightComponent(rightPanel);
		
		leftPanel.add(outputPanel);
		
		rightPanel.add(openPanel);
		
		mainPanel.add(itemsOpenSplit);
		
		frame.add(BorderLayout.CENTER, mainPanel);
	}
	public void go() {
		frame.setVisible(true);
		updaterTarget.go();
		if (!updaterThread.getState().equals(Thread.State.RUNNABLE)) {
			updaterThread = new Thread(updaterTarget, "Updater-Thread");
		}
		updaterThread.start();
		frame.requestFocus();
	}
	public void stop() {
		frame.setVisible(false);
		updaterTarget.stop();
	}
	private class resetSliderListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			itemsOpenSplit.setDividerLocation(frame.getWidth() / 3);			
		}
		
	}
	private class resizeListener implements ComponentListener {

		public void componentHidden(ComponentEvent arg0) {}

		public void componentMoved(ComponentEvent arg0) {}

		public void componentResized(ComponentEvent arg0) {
			rightPanel.setMinimumSize(new Dimension(frame.getWidth() / 4, frame.getHeight() / 4));
			leftPanel.setMinimumSize(new Dimension(frame.getWidth() / 4, frame.getHeight() / 4));		
		}

		public void componentShown(ComponentEvent arg0) {}
		
	}
	private class showFilesListener implements ItemListener {

		public void itemStateChanged(ItemEvent arg0) {
			utill.chooser.setFileHidingEnabled(((AbstractButton) viewMenuItems[1]).isSelected());		
		}
		
	}
	private class clearListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			menuItems.clear();
			menuItemsBox.removeAllItems();
		}
		
	}
	private class saveAndQuitListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			int returnVal = utill.save();
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.exit(0);
			}
		}
		
	}
	private class saveFileListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			utill.save();                       
		}
		
	}
	private class openFileListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			utill.open();			
		}
		
	}
	private class openItemListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			MenuItem toDisplay = (MenuItem) menuItemsBox.getSelectedItem();
			if (menuItemsBox.getSelectedItem() != null) {
				MenuItemDisplayer displayer = new MenuItemDisplayer(toDisplay);
				displayer.go();
			} else {
				utill.AWTToolKit.beep();
			}
		}

	}
	private class newItemListener implements ActionListener {
		private class invokerThread extends Thread {
			invokerThread() {
				super("Invoker (Preventing Deadlock)");
			}
			public void run() {
				MenuItemCreator creator = new MenuItemCreator();
				creator.go();
			}
		}
		public void actionPerformed(ActionEvent arg0) {
			new invokerThread().start();
		}
	}
	private class quitListener implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);			
		}

	}
	private class updater implements Runnable {
		private boolean stopped = false;
		public void run() {
			List<MenuItem> lastMenuItems = new ArrayList<MenuItem>();
			lastMenuItems.clear();
			lastMenuItems.addAll(menuItems);
			while(!stopped) {
				while(lastMenuItems.equals(menuItems) && !stopped) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						ExceptionHandeler.HandelException(e, true);
					}
				}
				if (!stopped) {
					if(!(lastMenuItems == menuItems)) {
						menuItemsList.setListData(menuItems.toArray());
						for(MenuItem item : menuItems) {
							menuItemsBox.removeItem(item);
							menuItemsBox.addItem(item);							
						}
						lastMenuItems.clear();
						lastMenuItems.addAll(menuItems);
						System.out.println("Menu Items Are Now: ");
						for(MenuItem item : menuItems) {
							System.out.println(item.toString());
						}
					}
				}
			}
		}
		public void stop() {
			stopped = true;
		}
		public void go() {
			stopped = false;
		}
	}
	private class listListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent arg0) {
			menuItemsBox.setSelectedIndex(((JList) arg0.getSource()).getSelectedIndex());
		}
		
	}
}
