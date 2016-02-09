package com.cherryworm.dico.ui;

import java.io.File;

import org.springframework.context.event.EventListener;

import com.cherryworm.dico.events.TaskStatusChangedEvent;
import com.cherryworm.dico.models.Job;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

public class TaskPane extends HorizontalLayout {
	private static final long serialVersionUID = 6505016319843092706L;
	
	private Grid taskQueue;
	private Grid finishedTasks;
	private BeanContainer<Long, Job> taskQueueContainer;
	private BeanContainer<Long, Job> finishedTasksContainer;
	
	public TaskPane(WorkerGrids workerGrids) {		
		setImmediate(true);
		setCaption("Tasks");
		setIcon(new FileResource(new File("task_list.png")));
		setSizeFull();
		
		taskQueueContainer = new BeanContainer<>(Job.class);
		taskQueueContainer.setBeanIdProperty("id");
		taskQueue = new Grid("Task Queue", taskQueueContainer);
		taskQueue.setSizeFull();
		taskQueue.removeColumn("resultData");
		taskQueue.removeColumn("retries");
		taskQueue.removeColumn("runtime");
		taskQueue.removeColumn("worker");
		taskQueue.removeColumn("status");
		taskQueue.removeColumn("result");
		taskQueue.setColumnOrder("id", "arguments", "jobClass", "taskGroup");
		taskQueue.setImmediate(true);
		addComponent(taskQueue);
		
		workerGrids.setSizeFull();
		addComponent(workerGrids);
		
		finishedTasksContainer = new BeanContainer<>(Job.class);
		finishedTasksContainer.setBeanIdProperty("id");
		finishedTasks = new Grid("Finished Tasks", finishedTasksContainer);
		finishedTasks.setSizeFull();
		finishedTasks.removeColumn("retries");
		finishedTasks.removeColumn("worker");
		finishedTasks.removeColumn("status");
		finishedTasks.removeColumn("resultData");
		finishedTasks.setImmediate(true);
		finishedTasks.setColumnOrder("id", "arguments", "jobClass", "taskGroup", "runtime", "result");
		finishedTasks.getColumn("result").setRenderer(new ButtonRenderer(e -> getUI().getPage().setLocation("/result?id=" + e.getItemId().toString())));
		addComponent(finishedTasks);
		
		setSpacing(true);
		setMargin(true);
	}
	 
	@EventListener
	public void taskChanged(TaskStatusChangedEvent e) {
		Job j = e.job;
		
		taskQueueContainer.removeItem(j.getId());
		finishedTasksContainer.removeItem(j.getId());
		
		switch(j.getStatus()) {
			case REGISTERED:
				taskQueueContainer.addBean(j);
				break;
			case FINISHED:
			case FAILED:
				finishedTasksContainer.addBean(j);
				break;
			default:
		}
		
		taskQueue.clearSortOrder();
		finishedTasks.clearSortOrder();
	}
	
}
