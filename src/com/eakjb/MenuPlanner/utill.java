package com.eakjb.MenuPlanner;
import com.eakjb.ExceptionHandeler.ExceptionHandeler;
import com.eakjb.MenuPlanner.Objects.MenuItem;
import java.awt.*;
import java.io.*;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
public class utill {
	private utill() {}
	public static List<MenuItem> menuItems = MenuItem.allItems;
	public static boolean saved = true;
	public static final String toolKitVersion;
	public static final JFileChooser chooser = new JFileChooser();
	public static final Toolkit AWTToolKit = Toolkit.getDefaultToolkit();
	public static final File backUpFile = new File("BackUp.menu2");
	public static final int[] versionInts = {0, 2};
	public static final int[] toolKitVersionInts = {0 , 1};
	public static final String versionPrefix = "Beta";
	public static final String version;
	public static final Robot robot;
	public static final String toolKitVersionPrefix = "Beta";
	public static File currentDirectory = new File(System.getProperty("user.home"));
	static {
		chooser.addChoosableFileFilter(new chooserFilter());
		chooser.setCurrentDirectory(currentDirectory);
		
		StringBuffer versionBuff = new StringBuffer();
		versionBuff.append(versionPrefix + " ");
		for (int i = 0; i < versionInts.length; i++) {
			versionBuff.append(versionInts[i]);
			if (i < versionInts.length - 1) {
				versionBuff.append(".");
			}
		}
		version = versionBuff.toString();
		StringBuffer toolKitVersionBuff = new StringBuffer();
		toolKitVersionBuff.append(toolKitVersionPrefix + " ");
		for (int i = 0; i < toolKitVersionInts.length; i++) {
			toolKitVersionBuff.append(toolKitVersionInts[i]);
			if (i < toolKitVersionInts.length - 1) {
				toolKitVersionBuff.append(".");
			}
		}
		toolKitVersion = toolKitVersionBuff.toString();
		Robot newRobot = null;
		try {
			newRobot = new Robot();
		} catch (AWTException ex) {
			ExceptionHandeler.HandelException(ex, true);
		}
		robot = newRobot;
		Runtime.getRuntime().addShutdownHook(new Thread(new shutDownHook()));
	}
	//Write to the back up file
	public static void writeBackUp() throws IOException {
		FileWriter writer = new FileWriter(backUpFile);
		for(MenuItem toSave : menuItems) {
			toSave.writeObject(writer);
		}
		writer.close();
		saved = true;
	}
	//Read from the backup file
	public static void readBackUp() throws IOException, ClassNotFoundException {
		FileReader reader = new FileReader(backUpFile);
		MenuItem.readObjects(reader);		
	}
	//Print misc stats
	public static void printStats() {
		Runtime runtime = Runtime.getRuntime();
		System.out.println("Menu Planner: " + version);
		System.out.println();
		System.out.println("You have " + runtime.freeMemory() / 1000 / 1000 + " MB memory free out of " + runtime.totalMemory() / 1000 / 1000 + " MB avalable to this program.");
		System.out.println();
		System.out.println("You have " + runtime.availableProcessors() + " processers.");
		System.out.println();
		File[] roots = File.listRoots();
		System.out.print("\nYou have " + roots.length + " file system root");
		if (roots.length > 1) {
			System.out.print("s:\n");
		} else {
			System.out.print(":\n");
		}
		for (File toPrint : roots) {
			System.out.print(toPrint + "\n");
		}
		System.out.println();
		System.out.println("Current directory: " + System.getProperty("user.dir"));
		System.out.println();
		System.out.println("You screen is " + utill.AWTToolKit.getScreenSize().width + " x " + utill.AWTToolKit.getScreenSize().height);
		System.out.println(); 
		if(menuItems.size() > 0) {
			System.out.println("Menu Items: ");
			for(MenuItem item : menuItems) {
				System.out.println(item.toString());
			}
		} else {
			System.out.println("No Menu Items");
		}
		System.out.println();
	}
	//Save to file.
	public static int save() {
		int returnVal = chooser.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			FileWriter out = null;
			try {
				File file = chooser.getSelectedFile();
				file.delete();
				file.createNewFile();
				if (!file.getName().contains(".")) {
					file = new File(file.getPath() + ".menu2");
				}
				out = new FileWriter(file);
				
				for (MenuItem toWrite : menuItems) {
					toWrite.writeObject(out);
				}
				out.write("safety");
			} catch (Exception ex) {
				ExceptionHandeler.HandelException(ex, false);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException ex) {
						ExceptionHandeler.HandelException(ex, true);
					}
				}
			}
			saved = true;
		}
		currentDirectory = chooser.getCurrentDirectory();
		return returnVal;
	}
	//Open a file
	public static int open() {
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			FileReader in = null;
			try {
				in = new FileReader(chooser.getSelectedFile());
				menuItems.clear();
				MenuItem.readObjects(in);
			} catch (Exception ex) {
				ExceptionHandeler.HandelException(ex, false);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException ex) {
						ExceptionHandeler.HandelException(ex, true);
					}
				}
			}
		}
		currentDirectory = chooser.getCurrentDirectory();
		return returnVal;
	}
	private static class chooserFilter extends FileFilter {

		public boolean accept(File arg0) {
			String fileName = arg0.getName();
			boolean toReturn = true;
			if (!arg0.isDirectory() && !fileName.endsWith(".menu2")) {
				toReturn = false;
			}
			return toReturn;
		}

		public String getDescription() {
			return "*.menu2 (menu2)";
		}

	}
	private static class shutDownHook implements Runnable {

		public void run() {
			if(menuItems.size() <= 0 && !saved) {
				try {
					writeBackUp();
				} catch (IOException e) {}
			}
		}
		
	}
}
