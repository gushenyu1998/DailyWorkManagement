package ca.cmpt213.a4.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.GregorianCalendar;
import ca.cmpt213.a4.control.TaskControl;
import ca.cmpt213.a4.model.Task;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * The Dialog of Add task action of the task list. Apply the Box layout to build the box
 */
public class AddTaskGUI extends JDialog {
    Box titleField,noteField, dateField,buttonField;
    JTextField titleInput,noteInput;
    JButton generateButton,createButton,cancelButton;
    DateTimePicker dateTimePicker;
    public AddTaskGUI(JFrame taskGUI){
        super(taskGUI,"Add task",true);//freeze the main windows until this block closed
        this.setLocation(500,500);
        this.setMinimumSize(new Dimension(450,300));
        Container content = this.getContentPane();
        Box totalContent = Box.createVerticalBox();
        // the the Field of inputting the name of task
        titleField = Box.createHorizontalBox();
        titleField.add(new JLabel("Name:\t"));
        titleInput = new JTextField();
        titleField.add(titleInput);
        titleField.setMaximumSize(new Dimension(300,25));
        //set the field of inputting the note of task
        noteField = Box.createHorizontalBox();
        noteField.add(new JLabel("Note:\t"));
        noteInput  = new JTextField();
        noteField.add(noteInput);
        noteField.setMaximumSize(new Dimension(300,25));
        //Set the file of Date time picker, and set the default time to now
        dateTimePicker = new DateTimePicker();
        dateTimePicker.getDatePicker().setDateToToday();
        dateTimePicker.getTimePicker().setTimeToNow();
        dateField = Box.createHorizontalBox();
        dateField.add(new JLabel("Due Date of Task:"));
        dateField.add(Box.createHorizontalGlue());
        dateField.add(dateTimePicker);
        dateField.setMinimumSize(new Dimension(400,25));
        dateField.setMaximumSize(new Dimension(400,25));
        //Set the field for buttons in this Dialogs
        buttonField = Box.createHorizontalBox();
        generateButton = new JButton("Generate");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateTask();
            }
        });
        buttonField.add(generateButton);
        buttonField.add(Box.createHorizontalGlue());
        createButton = new JButton("Create");
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTask();
            }
        });
        buttonField.add(createButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonField.add(cancelButton);
        buttonField.setMinimumSize(new Dimension(400,50));
        buttonField.setMaximumSize(new Dimension(400,50));

        totalContent.add(Box.createVerticalStrut(30));
        totalContent.add(titleField);
        totalContent.add(Box.createVerticalStrut(30));
        totalContent.add(noteField);
        totalContent.add(Box.createVerticalStrut(30));
        totalContent.add(dateField);
        totalContent.add(Box.createVerticalStrut(30));
        totalContent.add(buttonField);
        content.add(totalContent,BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * Create Task and add that to task list and update the local file.
     * <p>If the name is null or the date time input in illegal way, there would be an window for remind re input</p>
     */
    private void createTask(){
        String nameInputSting = titleInput.getText();
        if(nameInputSting.isBlank()){
            JOptionPane.showMessageDialog(null,"Task name cannot be blank!!!!","Warning",JOptionPane.WARNING_MESSAGE);
            dateTimePicker.getDatePicker().setDateToToday();
            dateTimePicker.getTimePicker().setTimeToNow();
            return;
        }
        String noteInputString = noteInput.getText();
        LocalDate date;
        LocalTime time;
        date = dateTimePicker.datePicker.getDate();
        if(date==null){
            JOptionPane.showMessageDialog(null,"Please input the correct Date","Warning",JOptionPane.WARNING_MESSAGE);
            dateTimePicker.getDatePicker().setDateToToday();
            dateTimePicker.getTimePicker().setTimeToNow();
            return;
        }
        time = dateTimePicker.timePicker.getTime();
        if(time==null){
            JOptionPane.showMessageDialog(null,"Please input the correct time","Warning",JOptionPane.WARNING_MESSAGE);
            return;
        }
        GregorianCalendar dueDate = new GregorianCalendar();
        dueDate.set(date.getYear(),date.getMonthValue()-1,date.getDayOfMonth(),time.getHour(),time.getMinute());
        Task newTask = new Task(nameInputSting,noteInputString,dueDate);
        TaskControl.addTasks(newTask);
        try {
            TaskControl.updateFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dispose();
    }
    private void generateTask(){
        String command = "curl -X GET https://www.boredapi.com/api/activity";
        Process process;
        try {
            process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            builder.append(line);
            GentActivity gentActivity = new GentActivity();
            Gson gson = new Gson();
            java.lang.reflect.Type activityType = new TypeToken<GentActivity>(){}.getType();
            gentActivity = gson.fromJson(String.valueOf(builder),activityType);
            titleInput.setText(gentActivity.activity);
            noteInput.setText(gentActivity.toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Cannot generate the task, please input it in hand","Warning",JOptionPane.WARNING_MESSAGE);
        }

    }
}

/**
 * Class of resolve the activity gson
 */
class GentActivity{
    public String activity;
    public String type;
    public String participants;
    public String price;
    @Override
    public String toString() {
        return "type: " + type  +
                ", participants: " + participants +
                ", price: " + price;
    }
}