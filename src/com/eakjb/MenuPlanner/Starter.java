package com.eakjb.MenuPlanner;
import java.io.FileReader;
import javax.swing.JOptionPane;
import com.eakjb.ExceptionHandeler.ExceptionHandeler;
import com.eakjb.MenuPlanner.GUI.*;
import com.eakjb.MenuPlanner.Objects.MenuItem;
public class Starter {

	public static void main(String[] args) {
		if (args.length > 0) {
			try {
				FileReader reader = new FileReader(args[0]);
				MenuItem.readObjects(reader);
			} catch (Exception e) {
				ExceptionHandeler.HandelException(e);
			}
		} else if (utill.backUpFile.exists()) {
			System.out.println("Reading Back Up...");
			try {
				utill.readBackUp();
				utill.backUpFile.delete();
				JOptionPane.showMessageDialog(null, "Recovered Last Session");
			} catch (Exception e) {}
			System.out.println("Reading Complete");
			System.out.println();
		}
		utill.printStats();
		System.out.println("Starting GUI...");
		MainGUI gui = new MainGUI();
		gui.go();
		System.out.println("GUI running...");
	}
}
