package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "task_name", nullable = false)
	private String taskName;
	
	@Column(name = "startTime", nullable = false)
	private String startTime;
	
	@Column(name = "end_time", nullable = false)
	private String endTime;
	
	@Column(name = "start_time_int", nullable = false)
	private int startTimeInt;
	
	@Column(name = "end_time_int", nullable = false)
	private int endTimeInt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getStartTimeInt() {
		return startTimeInt;
	}
	public void setStartTimeInt(int startTimeInt) {
		this.startTimeInt = startTimeInt;
	}
	public int getEndTimeInt() {
		return endTimeInt;
	}
	public void setEndTimeInt(int endTimeInt) {
		this.endTimeInt = endTimeInt;
	}
	
	public Task(String taskName, String startTime, String endTime, int startTimeInt, int endTimeInt) {
		super();
		this.taskName = taskName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.startTimeInt = startTimeInt;
		this.endTimeInt = endTimeInt;
	}
	
	public Task() {
		
	}
	
	
}
