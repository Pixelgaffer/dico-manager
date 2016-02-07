package com.cherryworm.dico.ui;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.easyuploads.UploadField;
import org.vaadin.easyuploads.UploadField.StorageMode;

import com.cherryworm.dico.services.SendToServerService;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class CreateJobClassPane extends FormLayout {
	private static final long serialVersionUID = 3171911098340662867L;
	
	@Autowired
	private SendToServerService sendService;
	
	private TextField name = new TextField("Name");
	private UploadField fileUpload = new UploadField(StorageMode.MEMORY);
	private Button send = new Button("Create", this::create);

	public CreateJobClassPane() {
		setCaption("Create Job Class");
		setIcon(new FileResource(new File("new_job_class.png")));
		
		send.setStyleName(ValoTheme.BUTTON_PRIMARY);
		fileUpload.setCaption("Binary");
		fileUpload.setRequired(true);
		name.setRequired(true);
		
		addComponent(name);
		addComponent(fileUpload);
		addComponent(send);
		
		setMargin(true);
	}

	private void create(ClickEvent e) {
		if(!name.isValid() || !fileUpload.isValid()) {
			send.setComponentError(new UserError("Please fill out all the required fields!"));
			Notification notification = new Notification("Please fill out all the required fields!", Type.ERROR_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			return;
		}
		
		//TODO jobcreate senden
		Notification notification = new Notification("Succesfully created the job class!", Type.HUMANIZED_MESSAGE);
		notification.setDelayMsec(2000);
		notification.show(Page.getCurrent());
		send.setComponentError(null);
	}

}
