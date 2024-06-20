package com.example.todolist.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddNewTask;
import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.model.todomodel;

import java.util.List;

public class todoadapter extends RecyclerView.Adapter<todoadapter.ViewHolder> {

    private List<todomodel> todolist;
    private MainActivity activity;
    private com.example.todolist.DataBase.db db;

    public todoadapter(com.example.todolist.DataBase.db db, MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        todomodel item = todolist.get(position);
        holder.task.setText(item.getTask()); // Use getTask() method
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return todolist != null ? todolist.size() : 0;
    }

    private boolean toBoolean(int n){
        return n != 0;
    }

    public void setTasks(List<todomodel> todolist){
        this.todolist = todolist;
        notifyDataSetChanged();
    }

    public Context getContext(){ return activity; }

    public void deleteItem(int position){
        todomodel item = todolist.get(position);
        db.deleteTask(item.getId());
        todolist.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        todomodel item = todolist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
