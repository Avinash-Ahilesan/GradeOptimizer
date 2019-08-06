package com.example.avinash.gradeoptimizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.avinash.gradeoptimizer.database.TaskBaseHelper;
import com.example.avinash.gradeoptimizer.database.TaskCursorWrapper;
import com.example.avinash.gradeoptimizer.database.TaskDbSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskSingleton {
    private static TaskSingleton sTaskSingleton;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public ArrayList<Task> getTasks()
    {
        ArrayList<Task> tasks = new ArrayList<>();

        TaskCursorWrapper cursor = queryCrimes(null, null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                tasks.add(cursor.getTask());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }
        return tasks;
    }

    public static TaskSingleton get(Context context)
    {
        if(sTaskSingleton ==null)
            sTaskSingleton =new TaskSingleton(context);
        return sTaskSingleton;
    }

    private TaskSingleton(Context context)
    {
        mContext = context.getApplicationContext(); //since this class is a singleton, avoid using Activity context since it will not be cleaned by GC
        mDatabase = new TaskBaseHelper(mContext).getWritableDatabase();

    }
    public void addTask(Task t){
        ContentValues values = getContentvalues(t);

        mDatabase.insert(TaskDbSchema.TaskTable.NAME, null, values);

    }

    public void removeTask(UUID id){
        mDatabase.delete(TaskDbSchema.TaskTable.NAME,
                TaskDbSchema.TaskTable.Cols.UUID + " = ?", new String[] {id.toString()});
        /*for(Task t : mTaskList){
            if(t.getId().equals(id))
                mTaskList.remove(t);
        }*/
    }
    public void updateTask(Task t){
        String uuidString = t.getId().toString();
        ContentValues values = getContentvalues(t);

        mDatabase.update(TaskDbSchema.TaskTable.NAME, values,
                TaskDbSchema.TaskTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }
    private TaskCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(TaskDbSchema.TaskTable.NAME,
                null, whereClause, whereArgs, null, null, null);
        return new TaskCursorWrapper(cursor);
    }

    public  Task getTask(UUID id)
    {
        TaskCursorWrapper cursor = queryCrimes(TaskDbSchema.TaskTable.Cols.UUID + " = ?",
                new String[]{id.toString()});
        try{
            if(cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getTask();
        }
        finally{
            cursor.close();
        }
    }

    private static ContentValues getContentvalues(Task task){
        ContentValues values = new ContentValues();
        values.put(TaskDbSchema.TaskTable.Cols.UUID, task.getId().toString());
        values.put(TaskDbSchema.TaskTable.Cols.TITLE, task.getTaskName());
        values.put(TaskDbSchema.TaskTable.Cols.DESCRIPTION, task.getTaskDescription());
        values.put(TaskDbSchema.TaskTable.Cols.STATUS, task.isTaskCompleted() ? 1 : 0);
        values.put(TaskDbSchema.TaskTable.Cols.SESSIONS, task.getSessionsCompleted());
        return values;
    }




}
