package com.cherryworm.dico.ui;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.event.EventListener;

import com.cherryworm.dico.events.TaskStatusChangedEvent;
import com.cherryworm.dico.models.Job;
import com.cherryworm.dico.protos.TaskStatusProto.TaskStatus.TaskStatusUpdate;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

public class WorkerGrids extends VerticalLayout {
	private static final long serialVersionUID = -132367693523575041L;

	private Map<Long, Grid> grids = new HashMap<>();
	
	public WorkerGrids() {
		setImmediate(true);
	}
	
	public void addWorker(long id) {
		Grid grid = new Grid("Worker " + id);
		
		BeanContainer<Long, Job> container = new BeanContainer<>(Job.class);
		container.setBeanIdProperty("id");
		grid.setContainerDataSource(container);
		
		grid.removeColumn("resultData");
		grid.removeColumn("runtime");
		grid.removeColumn("workerId");
		grid.removeColumn("status");
		grid.setImmediate(true);
		grid.setColumnOrder("id", "arguments", "jobClass", "taskGroup", "retries");
		
		grids.put(id, grid);
		addComponent(grid);
	}
	
	@SuppressWarnings("unchecked")
	public void addJobToWorker(Job job, long id) {
		((BeanContainer<Long, Job>) grids.get(id).getContainerDataSource()).addBean(job);
		grids.get(id).clearSortOrder();
	}
	
	public void removeJob(long id) {
		for(Grid grid : grids.values()) {
			grid.getContainerDataSource().removeItem(id);
			grid.clearSortOrder();
		}
	}
	
	public void removeWorker(String ip) {
		removeComponent(grids.get(ip));
		grids.remove(ip);
	}
	
	@EventListener
	public void taskChanged(TaskStatusChangedEvent e) {
		Job j = e.job;
		
		removeJob(j.getId());
		
		if(j.getStatus() == TaskStatusUpdate.STARTED) {
			if(!grids.containsKey(j.getWorkerId())) {
				addWorker(j.getWorkerId());
			}
			addJobToWorker(j, j.getWorkerId());
		}
	}
	
}
