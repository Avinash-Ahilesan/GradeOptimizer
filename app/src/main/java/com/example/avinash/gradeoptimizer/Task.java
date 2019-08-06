package com.example.avinash.gradeoptimizer;

import java.util.UUID;

public class Task {
    private UUID mId;
    private String mTaskName;
    private String mTaskDescription;

    //1 complete session is a full time interval
    private int sessionsCompleted=0;

    private boolean mTaskCompleted=false;

    public Task() {this(UUID.randomUUID()); }

    public Task(UUID id){
        mId = id;
    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String taskName) {
        mTaskName = taskName;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTaskDescription() {
        return mTaskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        mTaskDescription = taskDescription;
    }

    public boolean isTaskCompleted() {
        return mTaskCompleted;
    }
    public void setTaskCompleted(boolean taskCompleted)
    {
        mTaskCompleted = taskCompleted;
    }
    public int getSessionsCompleted(){
        return sessionsCompleted;
    }
    public void setSessionsCompleted(int sessions){sessionsCompleted=sessions;}
    public void incrementSessionsCompleted(){
        sessionsCompleted++;

    }
}
