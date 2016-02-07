package com.cherryworm.dico.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import com.cherryworm.dico.events.DisconnectedEvent;
import com.cherryworm.dico.services.SendToServerService;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.UI;

@Theme("valo")
@SpringUI(path = "")
public class DicoUI extends UI {
	private static final long serialVersionUID = 1592690302566835893L;

	@Autowired
	private SendToServerService sendService;
	
	@Autowired
	private StartServerForm startServer;
	
	@Autowired
	private TaskPane tasks;
	@Autowired
	private CreateTaskPane createTask;
	@Autowired
	private CreateJobClassPane createJobClass;

	@Override
	protected void init(VaadinRequest request) {
		setSizeFull();
		setImmediate(true);
		
		if (!sendService.isConnected()) {
			setContent(startServer);
		} else {
			Accordion accordion = new Accordion();
			accordion.setSizeFull();
			setContent(accordion);

			accordion.addComponent(tasks);

			accordion.addComponent(createTask);

			accordion.addComponent(createJobClass);
		}
	}
	
	@EventListener
	public void disconnected(DisconnectedEvent e) {
		System.out.println("Received disconnect event, reloading page...");
		getPage().getJavaScript().execute("window.location.reload();");
	}
}
