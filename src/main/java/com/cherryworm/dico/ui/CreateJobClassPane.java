package com.cherryworm.dico.ui;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.easyuploads.UploadField;
import org.vaadin.easyuploads.UploadField.FieldType;
import org.vaadin.easyuploads.UploadField.StorageMode;

import com.cherryworm.dico.protos.SelfDescribingMessageProto.SelfDescribingMessage.MessageType;
import com.cherryworm.dico.protos.SubmitCodeProto.SubmitCode;
import com.cherryworm.dico.services.SendToServerService;
import com.google.protobuf.ByteString;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
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
		send.setClickShortcut(KeyCode.ENTER);
		fileUpload.setCaption("Binary");
		fileUpload.setRequired(true);
		fileUpload.setFileDeletesAllowed(true);
		fileUpload.setFieldType(FieldType.BYTE_ARRAY);
		fileUpload.setReadThrough(true);
		fileUpload.setStorageMode(StorageMode.MEMORY);
		name.setRequired(true);
		
		addComponent(name);
		addComponent(fileUpload);
		addComponent(send);
		
		setMargin(true);
	}

	private void create(ClickEvent e) {
		byte[] file = (byte[]) fileUpload.getValue();
		
		if(!name.isValid() || !fileUpload.isValid() || file == null) {
			send.setComponentError(new UserError("Please fill out all the required fields!"));
			Notification notification = new Notification("Please fill out all the required fields!", Type.ERROR_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			return;
		}
		
		try {
			sendService.send(SubmitCode.newBuilder().setArchive(ByteString.copyFrom(file)).setJobType(name.getValue()).build(), MessageType.SUBMIT_CODE);
			Notification notification = new Notification("Succesfully created the job class!", Type.HUMANIZED_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			send.setComponentError(null);
		} catch (IOException e1) {
			e1.printStackTrace();
			Notification notification = new Notification("Couldn't create job class!", Type.ERROR_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			send.setComponentError(new UserError("Couldn't create job class!!"));
		}
		
		
	}

}
