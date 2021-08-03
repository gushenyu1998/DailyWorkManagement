package ca.cmpt213.a4.model;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * The templete of the task in the list and the to string method of that
 */
public class Task {
    private String title = "default";
    private String note = "default";
    private GregorianCalendar due_Date = (GregorianCalendar) Calendar.getInstance();
    private boolean Complete = false;

    public Task(String title,String note,GregorianCalendar dueDate){
        this.title = title;
        this.note = note;
        this.due_Date=dueDate;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public GregorianCalendar getDue_Date() {
        return due_Date;
    }

    public boolean getComplete(){
        return Complete;
    }

    public void setComplete(boolean set){
        Complete = set;
    }


    @Override
    public String toString() {
        return  "Name: "+getTitle()+
                "\nNote: "+getNote()+
                "\nDue Date: "+getDue_Date().get(Calendar.YEAR)+
                "-"+(getDue_Date().get(Calendar.MONTH)+1)+
                "-"+getDue_Date().get(Calendar.DATE)+
                " "+(getDue_Date().get(Calendar.HOUR)==0?12:getDue_Date().get(Calendar.HOUR))+
                ":"+String.format("%02d",getDue_Date().get(Calendar.MINUTE))+
                " "+(getDue_Date().get(Calendar.AM_PM)==Calendar.AM ?"AM" : "PM");
    }
}
