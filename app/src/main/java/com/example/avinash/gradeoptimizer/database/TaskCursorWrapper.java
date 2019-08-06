package com.example.avinash.gradeoptimizer.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.avinash.gradeoptimizer.Task;

import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {
    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask(){
        String uuidString = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.UUID));
        String title = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.TITLE));
        String description = getString(getColumnIndex(TaskDbSchema.TaskTable.Cols.DESCRIPTION));
        int isTaskCompleted= getInt(getColumnIndex(TaskDbSchema.TaskTable.Cols.STATUS));
        int numberOfSessions = getInt(getColumnIndex(TaskDbSchema.TaskTable.Cols.SESSIONS));

        Task task = new Task(UUID.fromString(uuidString));
        task.setTaskName(title);
        task.setTaskDescription(description);
        task.setTaskCompleted(isTaskCompleted != 0);
        task.setSessionsCompleted(numberOfSessions);

        return task;
    }
}
