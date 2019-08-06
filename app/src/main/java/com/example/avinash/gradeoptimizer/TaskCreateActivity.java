package com.example.avinash.gradeoptimizer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class TaskCreateActivity extends SingleFragmentActivity {
    private static String EXTRA_TASK_ID = "com.example.avinash.gradeoptimizer.task_id";
    @Override
    protected Fragment createFragment() {
        return new TaskCreateFragment();
    }


}
