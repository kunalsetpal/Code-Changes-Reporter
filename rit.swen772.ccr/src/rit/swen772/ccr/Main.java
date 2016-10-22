package rit.swen772.ccr;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.*;

public class Main {
	public static void main(String [] args){
		//GitHubClient client = new GitHubClient();
		GitHubClient githubClient = new GitHubClient();
		githubClient.setCredentials("USERNAME", "PASSWORD");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//final RepositoryId repoID = new RepositoryId("adblockplus", "adblockplusandroid");

		RepositoryService repoService = new RepositoryService(githubClient);
				
		
		RepositoryService service = new RepositoryService();
		try {
			/*for(Repository repo : service.getRepositories("yarnpkg"))
				System.out.println(repo.getName());*/
			//Repository repo = repoService.getRepository(repoID);
			Repository repo = service.getRepository("yarnpkg", "yarn");
			int counter = 0;
			/*for(RepositoryTag repoTags : service.getTags(repo)){
				System.out.println(repoTags.getName());
				++counter;
			}*/
			
			List <Release>listRelease = repoService.getReleases(repo);
			System.out.println(listRelease.size());
			
//			for(Release repoReleases : repoService.getReleases(repo)){
//				System.out.println(repoReleases.getTagName());
//				System.out.println(repoReleases.getBody());
//				System.out.println(repoReleases.getAuthor().getLogin());
//				System.out.println(repoReleases.getCreatedAt().toString());
//				++counter;
//			}
			
			Collections.sort(listRelease, (release1, release2) -> release1.getCreatedAt().compareTo(release2.getCreatedAt()));
			
			CommitService commitService = new CommitService(githubClient);
			List <RepositoryCommit>listCommit = commitService.getCommits(repo);
			
			Collections.sort(listCommit, (repoCommit1, repoCommit2) -> repoCommit1.getCommit().getCommitter().getDate().compareTo(repoCommit2.getCommit().getCommitter().getDate()));
			
			PrintWriter printWriter = new PrintWriter("CCRS.txt");
			
			Date previousReleaseDate = new Date();
			boolean isFirst = true;
			
			int releaseNumber = 0;
			int commitNumber = 0;
			
			for (Release release : listRelease) {
				releaseNumber++;
//				System.out.println("RELEASE: " + release.getCreatedAt() + " " + release.getName());
				printWriter.println("RELEASE #" + releaseNumber);
				printWriter.println("RELEASE DETAILS: " + release.getCreatedAt() + " " + release.getName());
				printWriter.println("");

				ArrayList<String> releaseContributorsList = new ArrayList<String>();
				
				final Date pReleaseDate = previousReleaseDate;
				
				List <RepositoryCommit> releaseCommits;
				if (isFirst) {
					releaseCommits = listCommit.stream().filter(repoCommit -> repoCommit.getCommit().getCommitter().getDate().before(release.getCreatedAt())).collect(Collectors.toList());
					isFirst = !isFirst;
				} else {
					releaseCommits = listCommit.stream().filter(repoCommit -> repoCommit.getCommit().getCommitter().getDate().before(release.getCreatedAt()) && repoCommit.getCommit().getCommitter().getDate().after(pReleaseDate)).collect(Collectors.toList());
				}
				
				previousReleaseDate = release.getCreatedAt();
				
				int releaseCommitNumber = 0;
				
				for (RepositoryCommit repositoryCommit : releaseCommits) {
					commitNumber++;
					releaseCommitNumber++;
					printWriter.println("COMMIT #" + commitNumber);
					printWriter.println("RELEASE COMMIT #" + releaseCommitNumber);
					printWriter.println(repositoryCommit.getCommit().getCommitter().getName());
					printWriter.println(repositoryCommit.getCommit().getMessage());
					printWriter.println(repositoryCommit.getCommit().getCommitter().getDate());
					
					if (!releaseContributorsList.contains(repositoryCommit.getCommit().getCommitter().getEmail())) {
						releaseContributorsList.add(repositoryCommit.getCommit().getCommitter().getEmail());
					}
					
					printWriter.println("----------------------------------------------------");
				}

				printWriter.println("");
				printWriter.println("=============================================================================");
				printWriter.println("# CONTRIBUTORS PER RELEASE: " + releaseContributorsList.size());
				printWriter.println("RELEASE CONTRIBUTORS: " + releaseContributorsList.toString());
				printWriter.println("=============================================================================");

			}
			
			
//			System.out.println(counter);
			
		} catch(Exception e) { e.printStackTrace(); }
	}
}
