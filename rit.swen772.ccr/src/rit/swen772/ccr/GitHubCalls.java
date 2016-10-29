package rit.swen772.ccr;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.Release;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.RepositoryTag;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;

public class GitHubCalls {
	private GitHubClient client;
	private RepositoryService service;
	private RepositoryId repoID;
	private Repository repo;
	private CommitService cService;
	private String userName;
	private String project;
	
	public GitHubCalls(String userName, String project){
		this.userName = userName;
		this.project = project;
		
		//this.client.setCredentials(Credentials.USER_NAME, Credentials.PASSWORD);
		this.client.setOAuth2Token(Credentials.TOKEN);
		
		this.client = new GitHubClient();
		this.service = new RepositoryService();
		this.repoID = new RepositoryId(this.userName, this.project);
		this.cService = new CommitService();
	}
	
	public void setUserName(String userName){
		this.userName = userName;
		this.repoID = new RepositoryId(this.userName, this.project);
	}
	
	public void getRepositories(){
		// get list of repositories
		try {
			for(Repository repo : this.service.getRepositories(this.userName))
				System.out.println(repo.getName());
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public void getRepositoryDescription(){
		// get description of repositories
		try {
			Repository repo = this.service.getRepository(this.repoID);
			// another way of initializing a Repository object 
			//Repository repo = this.service.getRepository(this.userName, this.project);
			System.out.println(repo.getDescription());
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public void getRepositoryTags(){
		// get tags of repositories
		try {
			for(RepositoryTag repoTags : this.service.getTags(this.repo)){
				System.out.println(repoTags.getName());
			}
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public void getReleases(){
		// get releases for repository
		try {
			for(Release repoReleases : this.service.getReleases(this.repoID)){
				System.out.println(repoReleases.getTagName());
				System.out.println(repoReleases.getBody());
				System.out.println(repoReleases.getAuthor().getLogin());
				System.out.println(repoReleases.getCreatedAt().toString());
			}
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public void getCommitsInfo(){
		// get information of commits for a repository. It is limited to 20 commits
		try {
			int counter = 0;
			for(RepositoryCommit commit : cService.getCommits(repoID)){
				System.out.println(commit.getCommit().getMessage());
				System.out.println(commit.getCommit().getAuthor().getName());
				System.out.println(commit.getCommit().getAuthor().getDate());
				System.out.println(commit.getCommit().getCommitter().getName());
				System.out.println(commit.getCommit().getCommitter().getDate());
				System.out.println("============");
				counter++;
				if(counter > 20)
					break;
			}
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public void getContributorsAndCommitsPerRelease() {
		// save release and contributors per release
		try {
			List <Release>listRelease = this.service.getReleases(this.repoID);
			//System.out.println(listRelease.size());
			
			//Sort Releases
			Collections.sort(listRelease, (release1, release2) -> release1.getCreatedAt().compareTo(release2.getCreatedAt()));
			
			CommitService commitService = new CommitService(this.client);
			List <RepositoryCommit>listCommit = commitService.getCommits(this.repoID);
			
			//Sort Repository Commits
			Collections.sort(listCommit, (repoCommit1, repoCommit2) -> repoCommit1.getCommit().getCommitter().getDate().compareTo(repoCommit2.getCommit().getCommitter().getDate()));
			
			//This file will be created in your source repository. It will contain the details
			//TODO: This has to be added to the Database.
			PrintWriter printWriter = new PrintWriter("CCRS.txt");
			
			Date previousReleaseDate = new Date();
			boolean isFirst = true;
			
			int releaseNumber = 0;
			int commitNumber = 0;
			ArrayList<CommitUser> projectContributorsList = new ArrayList<CommitUser>();
			Map<String, List<RepositoryCommit>> commitsByContributors = new HashMap<String, List<RepositoryCommit>>();
			
			for (Release release : listRelease) {
				releaseNumber++;
				printWriter.println("RELEASE #" + releaseNumber);
				printWriter.println("RELEASE DETAILS: " + release.getCreatedAt() + " " + release.getName());
				printWriter.println("");

				ArrayList<String> releaseContributorsList = new ArrayList<String>();
				
				final Date pReleaseDate = previousReleaseDate;
				
				//Identify Commits Per Release
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
					
					//Identify Contributors per Release
					if (!releaseContributorsList.contains(repositoryCommit.getCommit().getCommitter().getEmail())) {
						releaseContributorsList.add(repositoryCommit.getCommit().getCommitter().getEmail());
					}

					//Identify Contributors for the Project
					if (!projectContributorsList.contains(repositoryCommit.getCommit().getCommitter())) {
						projectContributorsList.add(repositoryCommit.getCommit().getCommitter());						
					}
					
					//Mapping Commits with Contributors
					String commitUserEmail = repositoryCommit.getCommit().getCommitter().getEmail();
					
					if (commitsByContributors.get(commitUserEmail) == null) {
						List <RepositoryCommit> repoCommits = new ArrayList<RepositoryCommit>(); 
						repoCommits.add(repositoryCommit);
						commitsByContributors.put(commitUserEmail, repoCommits);
					} else {
						List <RepositoryCommit> repoCommits = commitsByContributors.get(commitUserEmail);
						repoCommits.add(repositoryCommit);
						commitsByContributors.put(commitUserEmail, repoCommits);
					}

					
					printWriter.println("----------------------------------------------------");
				}

				printWriter.println("");
				printWriter.println("=============================================================================");
				printWriter.println("# CONTRIBUTORS PER RELEASE: " + releaseContributorsList.size());
				printWriter.println("RELEASE CONTRIBUTORS: " + releaseContributorsList.toString());
				printWriter.println("=============================================================================");

			}

			printWriter.println("=============================================================================");
			printWriter.println("PROJECT CONTRIBUTORS: ");
			for (CommitUser commitUser : projectContributorsList) {
				printWriter.println(commitUser.getEmail());
				printWriter.println("Commits: " + commitsByContributors.get(commitUser.getEmail()).size());					
			}
			printWriter.println("=============================================================================");

			printWriter.close();
			
		} catch(Exception e) { e.printStackTrace(); }
	}
}
