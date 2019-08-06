package com.example.avinash.gradeoptimizer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class TaskActivity extends SingleFragmentActivity {

    //NOT IN USE ANYMORE
    private static final String EXTRA_TASK_ID = "com.example.avinash.gradeoptimizer.task_id";
    @Override
    protected Fragment createFragment() {
        UUID taskId = (UUID)getIntent().getSerializableExtra(EXTRA_TASK_ID);
        return TaskFragment.newInstance(taskId);
    }

    public static Intent newIntent(Context packageContext, UUID taskId) {
        Intent intent = new Intent(packageContext, TaskActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        return intent;
    }



}
