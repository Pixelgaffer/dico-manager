package com.cherryworm.dico.events;

import org.springframework.context.ApplicationEvent;

import com.cherryworm.dico.protos.TaskStatusProto.TaskStatus;

public class TaskStatusReceivedEvent extends ApplicationEvent {
	private static final long serialVersionUID = -7411277030494718342L;
	
	public TaskStatus message;
	
	public TaskStatusReceivedEvent(Object source, TaskStatus message) {
		super(source);
		this.message = message;
	}
	
}
