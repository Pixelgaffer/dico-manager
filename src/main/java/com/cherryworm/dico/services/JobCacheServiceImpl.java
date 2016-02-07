package com.cherryworm.dico.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.cherryworm.dico.events.TaskResultReceivedEvent;
import com.cherryworm.dico.events.TaskStatusChangedEvent;
import com.cherryworm.dico.events.TaskStatusReceivedEvent;
import com.cherryworm.dico.models.Job;
import com.cherryworm.dico.protos.TaskStatusProto.TaskStatus;

@Service
public class JobCacheServiceImpl implements JobCacheService {
	
	private Map<Long, Job> jobs = new HashMap<>();
	
	@Override
	public Job getJob(long id) {
		return jobs.get(id);
	}
	
	@EventListener
	public TaskStatusChangedEvent receivedTaskResult(TaskResultReceivedEvent e) {		
		if(getJob(e.message.getId()) != null) {
			getJob(e.message.getId()).setResultData(e.message.getData().toByteArray());
			return new TaskStatusChangedEvent(this, getJob(e.message.getId()));
		}
		return null;
	}
	
	@EventListener
	public TaskStatusChangedEvent receivedTaskStatus(TaskStatusReceivedEvent e) {		
		TaskStatus m = e.message;
		Job j = getJob(m.getId());
		
		if(j == null) {
			j = new Job(m.getId(), m.getOptions(), m.getTaskGroup(), m.getType());
			jobs.put(m.getId(), j);
		} else {
			j.setStatus(m.getType());
			if(m.hasRuntime())
				j.setRuntime(m.getRuntime());
			if(m.hasWorkerId())
				j.setWorkerId(m.getWorkerId());
			if(m.hasRetries())
				j.setRetries(m.getRetries());
		}
		
		return new TaskStatusChangedEvent(this, j);
	}

}
