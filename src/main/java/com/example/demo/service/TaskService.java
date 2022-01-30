package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Task;

public interface TaskService {
	List<Task> getAllTasks();
	
	Task saveTask(Task task);
	
	Task getTaskById(Long id);
	
	Task updateTask(Task task);
	
	void deleteTaskById(Long id);
}
