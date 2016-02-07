package com.cherryworm.dico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.cherryworm.dico.ui.CreateJobClassPane;
import com.cherryworm.dico.ui.CreateTaskPane;
import com.cherryworm.dico.ui.StartServerForm;
import com.cherryworm.dico.ui.TaskPane;
import com.cherryworm.dico.ui.WorkerGrids;

@SpringBootApplication
@ComponentScan({ "com.cherryworm.dico" })
public class DicoManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DicoManagerApplication.class, args);
	}
	
	@Bean
	public CreateJobClassPane createJobClassPane() {
		return new CreateJobClassPane();
	}
	
	@Bean
	public CreateTaskPane createTaskPane() {
		return new CreateTaskPane();
	}
	
	@Bean
	public StartServerForm startServerForm() {
		return new StartServerForm();
	}
	
	@Bean
	public TaskPane taskPane() {
		return new TaskPane(workerGrids());
	}
	
	@Bean
	public WorkerGrids workerGrids() {
		return new WorkerGrids();
	}
	
}
