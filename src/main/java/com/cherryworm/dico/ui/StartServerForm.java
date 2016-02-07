package com.cherryworm.dico.ui;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.cherryworm.dico.services.SendToServerService;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class StartServerForm extends FormLayout {
	private static final long serialVersionUID = 6625437046781562748L;
	
	@Autowired
	private SendToServerService sendService;
	
	private TextField host = new TextField("Host", "localhost");
	private TextField port = new TextField("Port", "7778");
	private Button send = new Button("Connect", this::send);
	
	public StartServerForm() {		
		host.setRequired(true);
		host.setImmediate(true);
		port.addValidator((Object o) -> ((String)o).matches("^-?\\d+$"));
		port.setRequired(true);
		port.setImmediate(true);
		send.setStyleName(ValoTheme.BUTTON_PRIMARY);
		send.setClickShortcut(KeyCode.ENTER);
		
		setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		addComponent(host);
		addComponent(port);
		addComponent(send);
		
		setMargin(true);
	}
	
	private void send(ClickEvent e) {
		if(!port.isValid() || !host.isValid()) return;
		
		try {
			sendService.connect(host.getValue(), Integer.parseInt(port.getValue()));
			JavaScript.getCurrent().execute("window.location.reload();");
		} catch (IOException ex) {
			ex.printStackTrace();
			send.setComponentError(new UserError("Couldn't connect to the server!"));
			Notification notification = new Notification("Couldn't connect to the server!", Type.ERROR_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
		}
	}
	
}
