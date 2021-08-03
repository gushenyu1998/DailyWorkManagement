package ca.cmpt213.a4.view;

import ca.cmpt213.a4.control.TaskControl;
import ca.cmpt213.a4.model.Task;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SingleClass extends JPanel {
    private Task task;
    public SingleClass(Task task, TaskGUI mainWindow, int number){
        this.task = task;
        JTextArea dueDate = new JTextArea(task.toString());
        dueDate.setEditable(false);
        dueDate.setLineWrap(true);
        dueDate.setWrapStyleWord(true);
        dueDate.setOpaque(false);
        Box buttonBox = Box.createHorizontalBox();
        JCheckBox accomplished = new JCheckBox("Complish");
        if (task.getComplete()){accomplished.setSelected(true);}
        accomplished.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(accomplished.isSelected()){
                    task.setComplete(true);
                }else {
                    task.setComplete(false);
                }
                try {
                    TaskControl.updateFile();
                    mainWindow.flushScrollPane();
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null,"Up Date File Error!!!Program exit","Error",JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            }
        });

        JButton remove = new JButton("Remove");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskControl.removeTask(task);
                try {
                    TaskControl.updateFile();
                    mainWindow.flushScrollPane();
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null,"Up Date File Error!!!Program exit","Error",JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            }
        });
        buttonBox.add(accomplished);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(remove);
        Box formatBox = Box.createVerticalBox();
        formatBox.add(dueDate);
        formatBox.add(buttonBox);
        formatBox.setBorder(new TitledBorder(new EtchedBorder(),"Task #"+number));
        this.add(formatBox);
        this.setLayout(new GridLayout(1,1));
        formatBox.setBackground(Color.BLACK);
    }
}
