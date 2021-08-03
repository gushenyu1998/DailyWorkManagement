package ca.cmpt213.a4.control;

import ca.cmpt213.a4.model.Task;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;

/**
 * Task control provide the function of:
 * <p>Update the local jsonfile, read the local json file, add and remove task, create the new local json file for task</p>
 */
public class TaskControl {
    private static ArrayList<Task> taskArrayList = null;
    private static final String sourseTask = "src/taskTrack.json";

    /**
     * update the local file from the task list stored in the arraylist
     * @throws IOException The exception of the write function meets IO error. the GUI would handle it by JOptionPane finnal close the file operator
     */
    public static void updateFile() throws IOException {
        Gson gson = new Gson();
        FileWriter fw = new FileWriter(new File(sourseTask));
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            String LA = gson.toJson(taskArrayList);
            bw.write(LA);
        } finally {
            bw.flush();
            fw.close();
        }
    }

    /**
     * Read the task by GSON and load it to the task array list
     * @throws FileNotFoundException If the file do not exist the throw that exceptin. The other part would operate that exception properly
     */
    public static void readTaskArray() throws FileNotFoundException {
        taskArrayList = new ArrayList<>();
        Gson gson = new Gson();
        File file = new File(sourseTask);
        JsonElement filename = JsonParser.parseReader(new FileReader(file));
        JsonArray fileObject = filename.getAsJsonArray();
        String ff = fileObject.toString();
        Type ListType = new TypeToken<ArrayList<Task>>(){}.getType();
        taskArrayList = gson.fromJson(ff,ListType);
    }
    public static ArrayList<Task> getTaskArrayList() {
        return taskArrayList;
    }

    /**
     * Add task and sort the list
     * @param task the task need to add to task arraylist
     */
    public static void addTasks(Task task)  {
        taskArrayList.add(task);
        sortTask();
    }

    /**
     * Remove the task from list
     * @param task the task need to remove
     */
    public static void removeTask(Task task){
        taskArrayList.remove(task);
    }

    /**
     * Sort the task
     */
    public static void sortTask(){
        Collections.sort(taskArrayList, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return  o1.getDue_Date().compareTo(o2.getDue_Date());
            }
        });
    }

    /**
     * Create a new empty task and an empty
     */
    public static void newTaskArray(){
        taskArrayList = new ArrayList<>();
        try {
            updateFile();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Cannot build new task the program would crush","Error",JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }
}
