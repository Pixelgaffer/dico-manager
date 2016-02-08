package com.cherryworm.dico.models;

import com.cherryworm.dico.protos.TaskStatusProto.TaskStatus.TaskStatusUpdate;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Job {
	
	@NonNull @Getter
	private long id;
	
	@Getter @Setter
	private byte[] resultData;
	
	@NonNull @Getter
	private String arguments;
	
	@Getter @Setter
	private long runtime;
	
	@Getter @Setter
	private String worker;
	
	@NonNull @Getter
	private String taskGroup;
	
	@Getter @Setter
	private long retries;
	
	@Getter @Setter
	private String jobClass;
	
	@NonNull @Getter @Setter
	private TaskStatusUpdate status;
	
}
