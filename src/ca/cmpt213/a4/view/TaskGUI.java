package ca.cmpt213.a4.view;

import ca.cmpt213.a4.control.TaskControl;
import ca.cmpt213.a4.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This GUI panel contains the main page of the panel. And the filter of showing task. The list would show in Scroll panel. And and button to awake and Dialog to add task
 */
public class TaskGUI{
    JFrame windows;
    private int showingswitch=0;
    private final JScrollPane[] scollPane;
    public TaskGUI(){
        //basic property of the windows
        windows = new JFrame("My todo list");
        windows.setSize(400,600);
        windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windows.setResizable(false);
        //set the filter by using JToggleButton. and layout them by using a center flow JPanel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,20));
        buttonPanel.setSize(600,200);
        buttonPanel.setLocation(20,30);
        JToggleButton allList = new JToggleButton("ALL");
        JToggleButton overdue = new JToggleButton("Overdue");
        JToggleButton upcoming = new JToggleButton("Upcoming");
        //Set the action of the filters, and set the default list showing state of the filter
        allList.setSelected(true);
        scollPane = new JScrollPane[]{new JScrollPane(readList(showingswitch), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)};
        allList.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {showingswitch=0;showListSetting(allList,overdue,upcoming,showingswitch);}});
        overdue.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {showingswitch=1;showListSetting(allList,overdue,upcoming,showingswitch);}});
        upcoming.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {showingswitch=2;showListSetting(allList,overdue,upcoming,showingswitch);}});
        buttonPanel.add(allList);
        buttonPanel.add(overdue);
        buttonPanel.add(upcoming);
        //Use empty JPanel to control the layout of main window
        JPanel west = new JPanel();
        west.setPreferredSize(new Dimension(30,200));
        JPanel east = new JPanel();
        east.setPreferredSize(new Dimension(30,200));
        //Set the position of the add task button
        JPanel addTaskPanle = new JPanel(new FlowLayout(FlowLayout.CENTER,0,20));
        JButton addTask  = new JButton("Add Task");
        //set the action of the add task button
        addTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddTaskGUI(windows);
                flushScrollPane();
            }
        });
        addTaskPanle.add(addTask);
        //load every thing into the main window
        windows.add(scollPane[0],BorderLayout.CENTER);
        windows.add(buttonPanel,BorderLayout.NORTH);
        windows.add(west,BorderLayout.WEST);
        windows.add(east,BorderLayout.EAST);
        windows.add(addTaskPanle,BorderLayout.SOUTH);
        windows.setVisible(true);
    }

    /**
     * flush the list of tasks showing in the windows
     */
    public void flushScrollPane(){
        windows.remove(scollPane[0]);
        Box tasklist = readList(showingswitch);
        JScrollPane newScrollPane = new JScrollPane(tasklist, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollBar bar = newScrollPane.getVerticalScrollBar();
        bar.validate();
        bar.setValue(bar.getMinimum());
        System.out.println(bar.getMaximum());
        windows.add(newScrollPane,BorderLayout.CENTER);
        scollPane[0]=newScrollPane;
        windows.revalidate();
        windows.repaint();
    }

    /**
     * Read the list of task form task control class. and set the layout and appearance. And then load every task UI into a total container then return that container
     * @param switches The state of the filer: 0->All task would be shown 1->only show the overdue task 2->only show the upcoming tasks
     * @return The JPanel contains the UI of every tasks
     */
    public Box readList(int switches){
        ArrayList<Task> taskslist;
        taskslist = TaskControl.getTaskArrayList();
        if(taskslist==null){
            taskslist = new ArrayList<>();
        }
        Box totalContainer = Box.createVerticalBox();
        int i = 1;
        for (Task task:taskslist){
            if(switches==0){
                totalContainer.add(new SingleClass(task,this,i++));
            }else if(switches==1&&!task.getComplete()&&task.getDue_Date().compareTo(Calendar.getInstance())<0){
                totalContainer.add(new SingleClass(task,this,i++));
            }else if(switches==2&&!task.getComplete()&&task.getDue_Date().compareTo(Calendar.getInstance())>0){
                totalContainer.add(new SingleClass(task,this,i++));
            }
        }
        for (;i<5;i++){
            JPanel emptyblock = new JPanel();
            emptyblock.setPreferredSize(new Dimension(200,100));
            totalContainer.add(emptyblock);
        }
        return totalContainer;
    }

    /**
     * Set the list showing filter. And make buttons mutual exclusion to each other
     * @param all The button shows all task list
     * @param overdue The button shows overdue task list
     * @param upcomming The button shows upcoming task list
     * @param switcher The switch determin which kind of list of task needs to show
     */
    public void showListSetting(JToggleButton all, JToggleButton overdue, JToggleButton upcomming, int switcher){
        switch (switcher){
            case 0:
                all.setSelected(true);
                overdue.setSelected(false);
                upcomming.setSelected(false);
                break;
            case 1:
                all.setSelected(false);
                overdue.setSelected(true);
                upcomming.setSelected(false);
                break;
            case 2:
                all.setSelected(false);
                overdue.setSelected(false);
                upcomming.setSelected(true);
        }
        flushScrollPane();
    }
}
