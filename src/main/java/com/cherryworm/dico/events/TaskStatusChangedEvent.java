package com.cherryworm.dico.events;

import org.springframework.context.ApplicationEvent;

import com.cherryworm.dico.models.Job;

public class TaskStatusChangedEvent extends ApplicationEvent {
	private static final long serialVersionUID = 4572485760775353980L;

	public Job job;
	
	public TaskStatusChangedEvent(Object source, Job job) {
		super(source);
		this.job = job;
	}
	
}
