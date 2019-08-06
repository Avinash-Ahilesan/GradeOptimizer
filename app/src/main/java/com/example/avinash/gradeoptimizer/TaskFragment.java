package com.example.avinash.gradeoptimizer;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;

public class TaskFragment extends Fragment {
    private long MAX_TIMER;
    private final long MAX_TIMER_WORK = 1500000;
    private final long MAX_TIMER_SHORT_BREAK= 300000;
    private final long MAX_TIMER_LONG_BREAK = 900000;

    private static final String ARG_TASK_ID="task_id";
    private static final String KEY_TIME_LEFT = "time_left";
    private static final String KEY_TIMER_MODE = "timer_mode";

    private Task mTask;
    private Button mTimerButton;
    private Button mResetTimerButton;
    private Button mSkipTimerButton;
    private Button mTaskCompletedButton;
    private EditText mTitleText;
    private TextView mTimerTextView;
    private TextView mSessionNumber;
    private ProgressBar mTimerProgressBar;
    private String mTimerMode="regular";

    private CountDownTimer mCountDownTimer;
    private long mTimeLeftMiliseconds=1500000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task, container, false);
        UUID taskId = (UUID)getArguments().getSerializable(ARG_TASK_ID);
        mTask = TaskSingleton.get(getActivity()).getTask(taskId);

        if(savedInstanceState != null){
            mTimeLeftMiliseconds = savedInstanceState.getLong(KEY_TIME_LEFT);
            mTimerMode= savedInstanceState.getString(KEY_TIMER_MODE);
            Toast.makeText(getContext(),R.string.task_title, Toast.LENGTH_SHORT).show();;
        }

        mSessionNumber = (TextView)v.findViewById(R.id.number_of_sessions);
        mSessionNumber.setText(Integer.toString(mTask.getSessionsCompleted()/2) + "/10" );

        mTimerProgressBar = v.findViewById(R.id.progressBar);
        mTimerProgressBar.setIndeterminate(false);
        mTimerProgressBar.setProgress(0);
        mTimerTextView = v.findViewById(R.id.timer_countdown);
        mTitleText = v.findViewById(R.id.titleText);
        mTitleText.setText(mTask.getTaskName());
        mTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTask.setTaskName(mTitleText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mResetTimerButton = v.findViewById(R.id.reset_button);
        mResetTimerButton.setEnabled(false);
        mResetTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountDownTimer.cancel();
                mTimeLeftMiliseconds = MAX_TIMER;
                updateTimerUI();
                mTimerButton.setText(R.string.start_timer_button);
                mResetTimerButton.setEnabled(false);
            }
        });
        mTimerButton = v.findViewById(R.id.timer_button);
        mTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimerButton.getText() == getText(R.string.start_timer_button)) {
                    startTimer();
                    mTimerButton.setText(R.string.stop_timer_button);
                    mResetTimerButton.setEnabled(true);
                } else {
                    stopTimer();
                    mTimerButton.setText(R.string.start_timer_button);
                }
            }
        });
        mTaskCompletedButton = v.findViewById(R.id.completeTask);
        mTaskCompletedButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mTask.setTaskCompleted(true);
                getActivity().finish();
            }
        });
        mSkipTimerButton = v.findViewById(R.id.skipButton);
        mSkipTimerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mTask.incrementSessionsCompleted();
                stopTimer();
                mTimerButton.setText(R.string.start_timer_button);
                updateTimeLeft();
                updateTimerUI();
            }
        });
        updateTimerUI();
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        TaskSingleton.get(getActivity()).updateTask(mTask);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_TIME_LEFT, mTimeLeftMiliseconds);
        outState.putString(KEY_TIMER_MODE, mTimerMode);
    }

    public static TaskFragment newInstance(UUID taskId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private void startTimer() {
        updateTimeLeft();

        mCountDownTimer = new CountDownTimer(mTimeLeftMiliseconds, 1000) {
            @Override
            public void onTick(long l) {
                mTimeLeftMiliseconds = l;
                updateTimerUI();
            }

            @Override
            public void onFinish() {
                mTask.incrementSessionsCompleted();
                updateTimeLeft();
                updateTimerUI();
                if(mTask.getSessionsCompleted() >= 10){
                    Toast.makeText(getContext(), R.string.too_many_sessions, Toast.LENGTH_LONG).show();
                }
                if(mTimerMode=="regular"){
                    //work noise ending
                }
                else{
                    //other noise to signal ending
                }
                //TODO: add sound and notification
            }
        }.start();
    }

    private void stopTimer() {
        mCountDownTimer.cancel();
    }

    private void updateTimerUI() {
        long minutes = mTimeLeftMiliseconds / 60000;
        long seconds = mTimeLeftMiliseconds % 60000 / 1000;

        String timeLeft = "";
        timeLeft += minutes;
        timeLeft += ":";
        if (seconds < 10)
            timeLeft += "0";
        timeLeft += seconds;

        mTimerTextView.setText(timeLeft);
       // mTimerProgressBar.setProgress((int)(mTimeLeftMiliseconds/MAX_TIMER));

        double timeLeftMS = mTimeLeftMiliseconds;
        double maxTimeMS = MAX_TIMER;
        double percentage= (timeLeftMS/maxTimeMS)*100.0;
        mTimerProgressBar.setProgress((int)(100-percentage)); //ISSUE: NOT SMOOTH
    }
    private void updateTimeLeft(){

         if(mTask.getSessionsCompleted() % 2 == 0) {
             mTimeLeftMiliseconds = MAX_TIMER_WORK;
             MAX_TIMER=MAX_TIMER_WORK;
             mTimerMode="regular";
             if(Build.VERSION.SDK_INT >Build.VERSION_CODES.LOLLIPOP) {
                 mTimerProgressBar.getProgressDrawable().setColorFilter(Color.parseColor("#e0e0e0"), PorterDuff.Mode.MULTIPLY);

             }

         }
        else if (mTask.getSessionsCompleted() == 7 || mTask.getSessionsCompleted() == 15) {
             mTimeLeftMiliseconds = MAX_TIMER_LONG_BREAK;
             MAX_TIMER=MAX_TIMER_LONG_BREAK;
             mTimerMode="long_break";
             if(Build.VERSION.SDK_INT >Build.VERSION_CODES.LOLLIPOP) {
                 mTimerProgressBar.getProgressDrawable().setColorFilter(GREEN, PorterDuff.Mode.MULTIPLY);

             }
         }
        else {
             mTimeLeftMiliseconds = MAX_TIMER_SHORT_BREAK;
             MAX_TIMER=MAX_TIMER_SHORT_BREAK;
             mTimerMode="short_break";
             if(Build.VERSION.SDK_INT >Build.VERSION_CODES.LOLLIPOP) {
                 mTimerProgressBar.getProgressDrawable().setColorFilter(GREEN, PorterDuff.Mode.MULTIPLY);
             }
         }

        mSessionNumber.setText(Integer.toString((int)Math.ceil((double)mTask.getSessionsCompleted()/2)) + "/10" );


    }
}
