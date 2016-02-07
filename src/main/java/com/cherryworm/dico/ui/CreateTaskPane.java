package com.cherryworm.dico.ui;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.cherryworm.dico.protos.SelfDescribingMessageProto.SelfDescribingMessage.MessageType;
import com.cherryworm.dico.protos.SubmitTaskProto.SubmitTask;
import com.cherryworm.dico.services.SendToServerService;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class CreateTaskPane extends FormLayout {
	private static final long serialVersionUID = 6315716819421499122L;
	
	@Autowired
	private SendToServerService sendService;
	
	private TextField arguments = new TextField("Statement");
	private ComboBox jobClasses = new ComboBox("Job Class");
	private TextField taskGroup = new TextField("Task Group");
	private Button send = new Button("Create", this::create);
	
	public CreateTaskPane() {
		setCaption("Create Task");
		setIcon(new FileResource(new File("new_task.png")));
		
		arguments.setWidth("50%");
		arguments.setRequired(true);
		jobClasses.setRequired(true);
		jobClasses.addItem("Create the rarest Pepe");
		send.setStyleName(ValoTheme.BUTTON_PRIMARY);
		send.setClickShortcut(KeyCode.ENTER);
		
		addComponent(arguments);
		addComponent(taskGroup);
		addComponent(jobClasses);
		addComponent(send);
		
		setMargin(true);
	}
	
	private void create(ClickEvent e) {
		if(!arguments.isValid() || !jobClasses.isValid()) {
			send.setComponentError(new UserError("Please fill out all the required fields!"));
			Notification notification = new Notification("Please fill out all the required fields!", Type.ERROR_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			return;
		}
		
		SubmitTask m = SubmitTask.newBuilder().setOptions(arguments.getValue()).setJobType(jobClasses.getValue().toString()).setMulti(true).build();
		try {
			sendService.send(m, MessageType.SUBMIT_TASK);
			
			Notification notification = new Notification("Succesfully submited the task!", Type.HUMANIZED_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			send.setComponentError(null);
		} catch (IOException e1) {
			e1.printStackTrace();
			Notification notification = new Notification("Couldn't connect to the server!", Type.ERROR_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			send.setComponentError(new UserError("Couldn't connect to the Server: " + e1.getMessage()));
		}
	}
	
}
