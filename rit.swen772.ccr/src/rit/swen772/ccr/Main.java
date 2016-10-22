package rit.swen772.ccr;

import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.*;

public class Main {
	public static void main(String [] args){
		//GitHubClient client = new GitHubClient();
		
		
		RepositoryService service = new RepositoryService();
		try {
			/*for(Repository repo : service.getRepositories("yarnpkg"))
				System.out.println(repo.getName());*/
			Repository repo = service.getRepository("yarnpkg", "yarn");
			//System.out.println(repo.getDescription());
			int counter = 0;
			/*for(RepositoryTag repoTags : service.getTags(repo)){
				System.out.println(repoTags.getName());
				++counter;
			}*/
			for(RepositoryRelease repoReleases : service.getReleases(repo)){
				System.out.println(repoReleases.getTagName());
				++counter;
			}
			
			System.out.println(counter);
			
		} catch(Exception e) { e.printStackTrace(); }
	}
}
