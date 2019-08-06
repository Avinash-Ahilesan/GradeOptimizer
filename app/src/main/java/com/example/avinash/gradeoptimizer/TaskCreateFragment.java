package com.example.avinash.gradeoptimizer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

public class TaskCreateFragment extends Fragment {
    private static final String ARG_TASK_ID="task_id";
    private Task mTask;
    private Button mAddTaskButton;
    private EditText mTaskTitle;
    private EditText mTaskDescription;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_task_create, container, false);
        //UUID taskId = (UUID)getArguments().getSerializable(ARG_TASK_ID);
        //mTask = TaskSingleton.get(getActivity()).getTask(taskId);
       mAddTaskButton = v.findViewById(R.id.add_task_button);
        mTaskTitle = v.findViewById(R.id.edit_task_title);
        mTaskDescription = v.findViewById(R.id.edit_task_description);
        mAddTaskButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  mTask=new Task();
                                                  mTask.setTaskName(mTaskTitle.getText().toString());
                                                  mTask.setTaskDescription(mTaskDescription.getText().toString());
                                                  TaskSingleton.get(getActivity()).addTask(mTask);
                                                  getActivity().finish();
                                              }
                                          }
        );
        return v;
    }
    /*public TaskCreateFragment newInstance(UUID taskId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);
        TaskCreateFragment taskCreateFragment = new TaskCreateFragment();
        taskCreateFragment.setArguments(args);
        return taskCreateFragment;

    }*/
}
