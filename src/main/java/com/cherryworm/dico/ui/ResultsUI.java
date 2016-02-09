package com.cherryworm.dico.ui;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import com.cherryworm.dico.events.TaskStatusChangedEvent;
import com.cherryworm.dico.models.Job;
import com.cherryworm.dico.services.JobCacheService;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("valo")
@SpringUI(path = "/result")
@Push
public class ResultsUI extends UI {
	private static final long serialVersionUID = -5575085365030094419L;

	@Autowired
	private JobCacheService jobCache;
	
	private byte[] content;
	private TextArea text = new TextArea();
	private ClickListener current;
	private Button binary, hex, string, base64;
	private long id;
	
	@Override
	protected void init(VaadinRequest request) {
		if(request.getParameter("id") == null) 	{
			content = "Please provide a job id!".getBytes(StandardCharsets.UTF_8);
			id = -1;
		}
		else {
			long id = Long.parseLong(request.getParameter("id"));
			Job j = jobCache.getJob(id);
			if(j == null || j.getResultData() == null) {
				content = "There is no result data for this job yet. This page will update as soon as the data is availlable.".getBytes(StandardCharsets.UTF_8);
			}
			else {
				content = j.getResultData();
			}
		}
				
		VerticalLayout content = new VerticalLayout();
		content.setDefaultComponentAlignment(Alignment.TOP_CENTER);	
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		binary = new Button("Binary", this::binary);
		binary.setWidth(150, Unit.PIXELS);
		hex = new Button("Hexadecimal", this::hex);
		hex.setWidth(150, Unit.PIXELS);
		string = new Button("String", this::string);
		string.setWidth(150, Unit.PIXELS);
		base64 = new Button("Base64", this::base64);
		base64.setWidth(150, Unit.PIXELS);
		buttons.addComponents(binary, hex, string, base64);
		buttons.setHeight(50, Unit.PIXELS);
		
		text.setSizeFull();
		text.setHeight("1000px");
		
		content.addComponents(buttons, text);
		
		string(null);
		
		setContent(content);
	}
	
	private void binary(ClickEvent e) {
		if(content != null) {
			text.setValue(BinaryCodec.toAsciiString(content));
		}
		binary.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		hex.setStyleName(null);
		string.setStyleName(null);
		base64.setStyleName(null);
		current = this::binary;
	}
	
	private void hex(ClickEvent e) {
		if(content != null) {
			text.setValue(Hex.encodeHexString(content));
		}
		binary.setStyleName(null);
		hex.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		string.setStyleName(null);
		base64.setStyleName(null);
		current = this::hex;
	}
	
	private void string(ClickEvent e) {
		if(content != null) {
			text.setValue(new String(content, StandardCharsets.UTF_8));
		}
		binary.setStyleName(null);
		hex.setStyleName(null);
		string.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		base64.setStyleName(null);
		current = this::string;
	}
	
	private void base64(ClickEvent e) {
		if(content != null) {
			text.setValue(Base64.encodeBase64String(content));
		}
		binary.setStyleName(null);
		hex.setStyleName(null);
		string.setStyleName(null);
		base64.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		current = this::base64;
	}
	
	@EventListener
	public void taskStatusChanged(TaskStatusChangedEvent e) {
		if(e.job.getId() != id) {
			return;
		}
		if(e.job.getResultData() != null) {
			content = e.job.getResultData();
			access(() -> current.buttonClick(null));
		}
	}

}
