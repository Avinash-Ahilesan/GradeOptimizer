package com.example.avinash.gradeoptimizer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "taskBase.db";

    public TaskBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TaskDbSchema.TaskTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                TaskDbSchema.TaskTable.Cols.UUID + ", " +
                TaskDbSchema.TaskTable.Cols.TITLE + ", " +
                TaskDbSchema.TaskTable.Cols.DESCRIPTION + ", " +
                TaskDbSchema.TaskTable.Cols.STATUS + ", " +
                TaskDbSchema.TaskTable.Cols.SESSIONS +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
