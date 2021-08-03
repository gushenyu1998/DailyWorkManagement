package ca.cmpt213.a4;
import ca.cmpt213.a4.control.TaskControl;
import ca.cmpt213.a4.view.*;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            TaskControl.readTaskArray();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Cannot Find the task data, a new form of task data would be create","Warning",JOptionPane.WARNING_MESSAGE);
            try {
                TaskControl.newTaskArray();
                TaskControl.updateFile();
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null,"Cannot form the new task data, Program crush","Error",JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TaskGUI();
                try {
                    TaskControl.updateFile();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,"Update File Error!!!Program exit","Error",JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            }
        });

    }
}