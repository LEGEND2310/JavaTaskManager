package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;

@Controller
public class TaskController {
	
	private TaskService taskService;
	
	public static void sortTasks(ArrayList<Task> tasks) {
		Collections.sort(tasks, new Comparator<Task>() {
			public int compare(Task t1, Task t2) {
				return Integer.valueOf(t1.getStartTimeInt()).compareTo(t2.getStartTimeInt());
			}
		});			
	}
	
	public static int convertToMinutes(String time) {
		if(time.substring(0, 2).matches("-0")) {
			return -1;
		}
		int hours = Integer.parseInt(time.substring(0, 2));
		int minutes = Integer.parseInt(time.substring(3));
		if(hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
			return -1;
		}
		return (hours*60 + minutes);
	}
	
	public static ArrayList<ArrayList<Integer>> displayFreeSlots(ArrayList<Task> tasks) {
		ArrayList<Integer> minutesInDay = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> slots = new ArrayList<ArrayList<Integer>>(); 
		for(int i = 0; i < 1440; i++) {
			minutesInDay.add(0);
		}
		
		for(int j = 0; j < tasks.size(); j++) {
			int mt = tasks.get(j).getStartTimeInt();
			while(mt < tasks.get(j).getEndTimeInt()) {
				minutesInDay.set(mt, 1);
				mt++;
			}
		}
		ArrayList<Integer> mins = new ArrayList<Integer>();
		if(minutesInDay.get(0) == 0) {
			mins.add(0);
		}
		for(int k = 1; k < minutesInDay.size(); k++) {
			if(minutesInDay.get(k) == 0) {
				if(minutesInDay.get(k -1) != 0) {
					mins.add(k+1);
				}
			} else if(minutesInDay.get(k) == 1) {
				if(minutesInDay.get(k -1) != 1) {
					mins.add(k-1);
				}
			}
		}
		if(mins.get(mins.size() - 1) == 1440) {
			mins.remove(mins.size() - 1);
		} else {
			mins.add(1439);
		}
		
		for(int idx = 0; idx < mins.size(); idx+=2) {
			ArrayList<Integer> temp = new ArrayList<>();
			temp.add(mins.get(idx));
			temp.add(mins.get(idx + 1));
			slots.add(temp);
		}
		
		return slots;
	}

	public TaskController(TaskService taskService) {
		super();
		this.taskService = taskService;
	}
	
	@GetMapping("/tasks")
	public String taskManager(Model model) {
		return "tasks";
	}
	
	@GetMapping("/displayfreeslots")
	public String listFreeSlots(Model model) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		for(Task task: taskService.getAllTasks()) {
			Task temp = new Task();
			temp.setId(task.getId());
			temp.setTaskName(task.getTaskName());
			temp.setStartTime(task.getStartTime());
			temp.setEndTime(task.getEndTime());
			temp.setStartTimeInt(task.getStartTimeInt());
			temp.setEndTimeInt(task.getEndTimeInt());
			tasks.add(temp);
		}
		ArrayList<ArrayList<Integer>> slots = displayFreeSlots(tasks);
		ArrayList<ArrayList<String>> slotsStr = new ArrayList<ArrayList<String>>();
		
		for(int ix = 0; ix < slots.size(); ix++) {
			ArrayList<String> temp = new ArrayList<String>();
			int start = slots.get(ix).get(0);
			int end = slots.get(ix).get(1);
			int startHour = start / 60;
			int startMin = start % 60;
			int endHour = end / 60;
			int endMin = end % 60;
			String startStrHour = (startHour<10)?"0" + startHour : "" + startHour;
			String startStrMin = (startMin<10)?"0" + startMin : "" + startMin;
			String endStrHour = (endHour<10)?"0" + endHour : "" + endHour;
			String endStrMin = (endMin<10)?"0" + endMin : "" + endMin;
			String startStr = (startStrHour + ":" + startStrMin);
			String endStr = (endStrHour + ":" + endStrMin);
			
			temp.add(startStr);
			temp.add(endStr);
			slotsStr.add(temp);
		}
		
		model.addAttribute("slotsStr", slotsStr);
		return "display_free_slots";
	}
	
//	Handler to handle list tasks and return model and view
	@GetMapping("/displaytasks")
	public String listTasks(Model model) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		for(Task task: taskService.getAllTasks()) {
			Task temp = new Task();
			temp.setId(task.getId());
			temp.setTaskName(task.getTaskName());
			temp.setStartTime(task.getStartTime());
			temp.setEndTime(task.getEndTime());
			temp.setStartTimeInt(task.getStartTimeInt());
			temp.setEndTimeInt(task.getEndTimeInt());
			tasks.add(temp);
		}
		sortTasks(tasks);
		model.addAttribute("tasks", tasks);
		return "display_tasks";
	}
	
	@GetMapping("/tasks/new")
	public String createTaskForm(Model model) {
		
//		create task object to hold new task data
		Task task = new Task();
		
		model.addAttribute("task", task);
		
		return "create_task";
		
	}
	
	@PostMapping("/displaytasks")
	public String saveTasks(@ModelAttribute("task") Task task) {
		task.setStartTimeInt(convertToMinutes(task.getStartTime()));
		task.setEndTimeInt(convertToMinutes(task.getEndTime()));
		taskService.saveTask(task);
		return "redirect:/displaytasks";
	}
	
	@GetMapping("/tasks/edit/{id}")
	public String editTaskForm(@PathVariable Long id, Model model) {
		model.addAttribute("task", taskService.getTaskById(id));
		return "edit_task";
	}
	
	@PostMapping("/displaytasks/{id}")
	public String updateTask(@PathVariable Long id,
			@ModelAttribute("task") Task task,
			Model model) {
		
		Task existingTask = taskService.getTaskById(id);
		existingTask.setId(id);
		existingTask.setTaskName(task.getTaskName());
		existingTask.setStartTime(task.getStartTime());
		existingTask.setEndTime(task.getEndTime());
		existingTask.setStartTimeInt(convertToMinutes(task.getStartTime()));
		existingTask.setEndTimeInt(convertToMinutes(task.getEndTime()));
		taskService.saveTask(existingTask);
		
		taskService.updateTask(existingTask);
		return "redirect:/displaytasks";
	}
	
	
//	Handling Delete Requests
	
	@GetMapping("tasks/{id}")
	public String deleteTask(@PathVariable Long id) {
		taskService.deleteTaskById(id);
		return "redirect:/displaytasks";
	}
}
