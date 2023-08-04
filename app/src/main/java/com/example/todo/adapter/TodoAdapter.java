package com.example.todo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.model.Category;
import com.example.todo.model.Todo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<Todo> allTodos = new ArrayList<>();

    private OnItemClickListener listener;

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo currentTodo = allTodos.get(position);
        holder.title.setText(currentTodo.getTitle());
        holder.description.setText(currentTodo.getDescription());
        holder.isCompleted.setText(currentTodo.isCompleted() ? "Task Completed" : "Task Incomplete");

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        holder.date.setText("Complete by: " + dateFormat.format(currentTodo.getTodoDate()));
        holder.createdDate.setText("Created at: " + dateFormat.format(currentTodo.getCreatedOn()));

        int priority = currentTodo.getPriority();
        if(priority == 0) {
            holder.priority.setText("High");
        }
        else if(priority == 1) {
            holder.priority.setText("Medium");
        }
        else {
            holder.priority.setText("Low");
        }
    }

    @Override
    public int getItemCount() {
        return allTodos.size();
    }

    public Todo getTodoAt(int position) {
        return allTodos.get(position);
    }

    public void setTodos(List<Todo> todos) {
        this.allTodos = todos;
        notifyDataSetChanged();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description, isCompleted, date, createdDate, priority;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.todo_item_layout_tv_title);
            description = itemView.findViewById(R.id.todo_item_layout_tv_description);
            isCompleted = itemView.findViewById(R.id.todo_item_layout_tv_completed);
            date = itemView.findViewById(R.id.todo_item_layout_tv_date);
            createdDate = itemView.findViewById(R.id.todo_item_layout_tv_created_at);
            priority = itemView.findViewById(R.id.todo_item_layout_tv_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(allTodos.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Todo todo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
