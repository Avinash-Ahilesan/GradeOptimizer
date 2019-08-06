package com.example.avinash.gradeoptimizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TaskListFragment extends Fragment {


    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mTaskAdapter;
    private int lastClickedTaskPosition;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_task:
                Intent intent = new Intent(getContext(), TaskCreateActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task_list, container, false);
        mTaskRecyclerView = (RecyclerView) v.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_list, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI(lastClickedTaskPosition);
    }

    private void updateUI() {
        TaskSingleton taskStorage = TaskSingleton.get(getActivity());
        List<Task> tasks = taskStorage.getTasks();
        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(tasks);
            mTaskRecyclerView.setAdapter(mTaskAdapter);
        } else {
            mTaskAdapter.setTasks(tasks);
            mTaskAdapter.notifyDataSetChanged();
        }
    }
    private void updateUI(int position) {   //only difference is last line of code, for efficiency (reload 1 item instead of full list)
        TaskSingleton taskStorage = TaskSingleton.get(getActivity());
        List<Task> tasks = taskStorage.getTasks();
        if (mTaskAdapter == null) {
            mTaskAdapter = new TaskAdapter(tasks);
            mTaskRecyclerView.setAdapter(mTaskAdapter);
        } else {
            mTaskAdapter.setTasks(tasks);
            mTaskAdapter.notifyItemChanged(position);
        }
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private ImageView mTaskInProgressImageView;
        private ImageView mTaskCompletedImageView;
        private ImageView mDeleteImageView;

        private Task mTask;

        public TaskHolder(View v) {

            super(v);

            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.task_title);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.task_description);
            mTaskInProgressImageView = (ImageView) itemView.findViewById(R.id.taskInProgress);
            mTaskCompletedImageView = (ImageView) itemView.findViewById(R.id.taskCompleted);
            mDeleteImageView = (ImageView) itemView.findViewById(R.id.delete_image_view);
            mDeleteImageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    TaskSingleton.get(getActivity()).removeTask(mTask.getId());
                    updateUI(getAdapterPosition());
                }
            });

            mTaskCompletedImageView.setVisibility(View.GONE);
        }

        public void bind(Task task) {
            mTask = task;
            mTitleTextView.setText(mTask.getTaskName());
            mDescriptionTextView.setText(mTask.getTaskDescription());
            mTaskInProgressImageView.setVisibility((mTask.isTaskCompleted() ? View.GONE : View.VISIBLE));
            mTaskCompletedImageView.setVisibility((mTask.isTaskCompleted() ? View.VISIBLE : View.GONE));
        }

        @Override
        public void onClick(View view) {
            if (mTask.isTaskCompleted()) {
                Toast.makeText(getContext(), R.string.task_already_completed, Toast.LENGTH_LONG).show();
            } else {
                Intent intent = TaskPagerActivity.newIntent(getActivity(), mTask.getId());
                lastClickedTaskPosition=getAdapterPosition();
                startActivity(intent);
            }
        }
    }
    private class EmptyViewHolder extends RecyclerView.ViewHolder{

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
    private class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VIEW_TYPE_EMPTY = 0;
        private static final int VIEW_TYPE_LIST_ITEM=1;
        private List<Task> mTasks;

        public void setTasks(List<Task> tasks){
            mTasks=tasks;
        }

        public TaskAdapter(List<Task> tasks) {
            mTasks = tasks;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //TODO: Create new viewholder for empty and edit rest of adapter
            View v;
            RecyclerView.ViewHolder vh;
            if (viewType==VIEW_TYPE_EMPTY) {
                Log.d("taskadapter", "inflating placeholder");
                v= LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_recycler_view, parent, false);
                return new EmptyViewHolder(v);
            }
            Log.d("taskadapter", "inflating regular");
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_task, parent, false);
            return new TaskHolder(v);
        }

        @Override
        public int getItemViewType(int position) {
            if(mTasks.size()==0){
                return VIEW_TYPE_EMPTY;
            }
            return VIEW_TYPE_LIST_ITEM;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            int viewType = getItemViewType(position);
            if(viewType==VIEW_TYPE_EMPTY){

            }
            else{
                Task task = mTasks.get(position);
                TaskHolder taskHolder = (TaskHolder)viewHolder;
                taskHolder.bind(task);
            }
        }

       /* @Override
        public void onBindViewHolder(@NonNull TaskHolder taskHolder, int i) {
            Task task = mTasks.get(i);
            taskHolder.bind(task);
        }*/

        @Override
        public int getItemCount() {
            //without this, the empty view will never show up (need to return 1 even if list empty)
            if(mTasks.size() == 0){
                return 1;
            }else {
                return mTasks.size();
            }
        }
    }
}
