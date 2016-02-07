package com.cherryworm.dico.events;

import org.springframework.context.ApplicationEvent;

import com.cherryworm.dico.protos.TaskResultProto.TaskResult;

public class TaskResultReceivedEvent extends ApplicationEvent {
	private static final long serialVersionUID = 8322862772760821742L;
	
	public TaskResult message;
	
	public TaskResultReceivedEvent(Object source, TaskResult message) {
		super(source);
		this.message = message;
	}
	
}
