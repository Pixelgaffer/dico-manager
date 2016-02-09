package com.cherryworm.dico.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import com.cherryworm.dico.events.DisconnectedEvent;
import com.cherryworm.dico.services.SendToServerService;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

@Theme("valo")
@SpringUI(path = "")
@Push
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

	private static int lastId = 0;
	private int id = -1;
	
	public DicoUI() {
		super();
		if (id == -1) {
			lastId++;
			id = lastId;
		}
		System.err.println("CONSTRUCTED!CONSTRUCTED!CONSTRUCTED! " + id);
	}
	
	public DicoUI(Component component) {
		super(component);
		if (id == -1) {
			lastId++;
			id = lastId;
		}
		System.err.println("CONSTRUCTED!CONSTRUCTED!CONSTRUCTED! " + id);
	}

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

		addAttachListener(e -> System.err.println("ATTACHED!ATTACHED!ATTACHED!"));
		addDetachListener(e -> System.err.println("DETACHED!DETACHED!DETACHED! " + id));
	}

	@Override
	public void attach() {
		super.attach();
		System.err.println("ATTACHED!ATTACHED!ATTACHED!MEH! " + id);
	}

	@EventListener
	public void disconnected(DisconnectedEvent e) {
		System.out.println("Event fired on " + id);
		
		if (isAttached()) {
			System.out.println("init");
			access(() -> init(null));
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println("reloading");
			access(() -> getPage().getJavaScript().execute("window.location.reload();"));
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (isAttached()) {
				System.err.println("location");
				access(() -> getPage().setLocation("/"));
			}
		}
	}
}
