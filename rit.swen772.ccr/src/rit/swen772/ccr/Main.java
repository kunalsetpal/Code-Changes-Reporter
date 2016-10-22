package rit.swen772.ccr;

import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.*;

public class Main {
	public static void main(String [] args){
		//GitHubClient client = new GitHubClient();
		//client.setOAuth2Token("8311bb341a74820827290dd8b1c325e70532624a");
		
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
			System.out.println(counter);
			Main mt = new Main();
			Main.GitHubServiceTest ghst = mt.new GitHubServiceTest(repo);
			
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	private class GitHubServiceTest extends GitHubService{
		public GitHubServiceTest(IRepositoryIdProvider repo){
			System.out.println(this.getId(repo));
		}
	}
}
