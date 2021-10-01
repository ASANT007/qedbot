package com.tkis.qedbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QedBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(QedBotApplication.class, args);
		
		//ApplicationContext context = SpringApplication.run(QedBotApplication.class, args);
		
		
		  //RepositoryDetailsRepo repo = context.getBean(RepositoryDetailsRepo.class);
		  
		  //List<RepositoryDetails> repolist = repo.findByProjectId(1);
		  
		  
		  //List<Object[]> repolist = repo.findByProjectId(1);
		 
		  //repolist.forEach(e->{ System.out.println(e); });
		
		
			/*
			 * for(Object[] kfRow : repolist) { System.out.println("Repo_Id "+kfRow[0]
			 * +" "+kfRow[1]); }
			 */
		 
		
	}

}
