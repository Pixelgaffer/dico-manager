package com.cherryworm.dico.events;

import org.springframework.context.ApplicationEvent;

public class DisconnectedEvent extends ApplicationEvent {
	private static final long serialVersionUID = -3218255796227822333L;

	public DisconnectedEvent(Object source) {
		super(source);
	}

}
