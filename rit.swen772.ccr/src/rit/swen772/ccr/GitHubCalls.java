package rit.swen772.ccr;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.Release;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.RepositoryTag;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.StargazerService;
import org.eclipse.egit.github.core.service.WatcherService;

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
		//this.client.setOAuth2Token(Credentials.TOKEN);
		
		this.client = new GitHubClient();
		client.setCredentials("LumbardhAgaj", "SIRcleaner123");
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
	
	public static void  getNumberOfForksPerRelease(RepositoryId repositoryID)
	{
		GitHubClient client = new GitHubClient();
		client.setCredentials("LumbardhAgaj", "SIRcleaner123");
		
		RepositoryService repoService = new RepositoryService(client);
		Map<Long, Integer> numberOfForksPerRel = new HashMap<Long,Integer>(); // I am saving for each release id the number of forks that it has. It's easier to save the values in db.
		List <Release> listRelease;
		List <Repository> repForks;
		try
		{
			listRelease = repoService.getReleases(repositoryID); // Its the best to implement a local method to get releases, which invokes only once the github api and for each function call within the method returns a newly created object with releases.
			repForks = repoService.getForks(repositoryID);
			int nonReleaseForksCounter = 0;
		
			for(int i=0;i<listRelease.size();i++)
			{
				Release current = listRelease.get(i);
				Release next = null;
				int forksCounter = 0;	//for every release, reset the forksCounterPerRelease to zero;			
				if((i+1)!=listRelease.size()) // if this is not the earliest release than fetch it for interval comparison.
				{	next = listRelease.get(i+1); } 
				
				for(int j =0;j<repForks.size();j++) // for each fork, look if it falls in release i or i+i
				{
					Date currentForkDate = repForks.get(j).getCreatedAt();
					if(next!=null)
					{
						if(currentForkDate.before(current.getCreatedAt()) && currentForkDate.after(next.getCreatedAt()))
						{	numberOfForksPerRel.put(current.getId(),++forksCounter);}
						else if(i==0 && currentForkDate.after(current.getCreatedAt()))	//find all non-release forks, or forks that occurred after the latest release.
						{	numberOfForksPerRel.put(100L,++nonReleaseForksCounter);}			
					}
					else
					{	if(currentForkDate.before(current.getCreatedAt()))// find all the forks made in the earliest release.
						{	numberOfForksPerRel.put(current.getId(), ++forksCounter);			}
					}		
				}
			}
			
			int relCounter=0;
			
			for(Release rel : listRelease)
			{	relCounter++;
				System.out.println("Release id"+ rel.getId()+ " ,release name: "+rel.getTagName()+" created at:"+rel.getCreatedAt()+ " release number"+ relCounter);
			}
			
			for (Long key: numberOfForksPerRel.keySet())
			{
				int val = numberOfForksPerRel.get(key);
				System.out.println("Release id="+ key+ " , Number of forks="+val);
			}
		}catch(IOException exception)
		{
			System.out.println(exception.getMessage());
		}
		
		
		
		
		//.out.println(listRelease.size());*/

		/*int repoCounter = 0;
		for(Repository r:repForks)
		{	repoCounter++;
			System.out.println(r.getCreatedAt()+ " "+ r.getId()+ " "+ repoCounter);
		}	*/
	
	
	}
	public RepositoryId getRepository()
	{
		return repoID;
	}
	
	public static void main(String [] args)
	{		
		RepositoryId repoID = new RepositoryId("yarnpkg", "yarn");

		//getNumberOfBranchesPerRelease(repoID);
		getNumberOfWatchersPerRelease(repoID);
		//getNumberOfForksPerRelease(repoID);
	}
	
	public static void  getNumberOfBranchesPerRelease(RepositoryId repositoryID)
	{
		GitHubClient client = new GitHubClient();
		client.setCredentials("LumbardhAgaj", "SIRcleaner123");
		
		RepositoryService repoService = new RepositoryService(client);
		CommitService commitService = new CommitService(client);
		Map<Long, Integer> numberOfBranchesPerRel = new HashMap<Long,Integer>(); // I am saving for each release id the number of forks that it has. It's easier to save the values in db.
		List <Release> listRelease;
		List <RepositoryBranch> repBranch;
		
		try
		{
			listRelease = repoService.getReleases(repositoryID); // Its the best to implement a local method to get releases, which invokes only once the github api and for each function call within the method returns a newly created object with releases.
			System.out.println(listRelease.size()+ " number of releases");
			repBranch = repoService.getBranches(repositoryID);
			System.out.println(repBranch.size()+ ":number of branches");
			int nonReleaseBranchesCounter = 0;
			
			for(int i=0;i<listRelease.size();i++)
			{
				Release current = listRelease.get(i);
				Release next = null;
				int branchCounter = 0;	//for every release, reset the forksCounterPerRelease to zero;			
				if((i+1)!=listRelease.size()) // if this is not the earliest release than fetch it for interval comparison.
				{	next = listRelease.get(i+1); } 
				
				for(int j =0;j<repBranch.size();j++) // for each fork, look if it falls in release i or i+i
				{
					String branchCommitSha = repBranch.get(j).getCommit().getSha();
					RepositoryCommit commitcomment = commitService.getCommit(repositoryID, branchCommitSha);
					Date currentBranchDate = commitcomment.getCommit().getCommitter().getDate();	
					
					if(next!=null)
					{
						//System.out.println("Release id:"+current.getId()+"Current release date:"+current.getCreatedAt()+", next release date"+ next.getCreatedAt()+ ", currentBranchDate:"+currentBranchDate);
						if(!currentBranchDate.after(current.getCreatedAt()) && currentBranchDate.after(next.getCreatedAt()))
						{	numberOfBranchesPerRel.put(current.getId(),++branchCounter);
						
							
						}
						else if(i==0 && currentBranchDate.after(current.getCreatedAt()))	//find all non-release forks, or forks that occurred after the latest release.
						{	numberOfBranchesPerRel.put(100L,++nonReleaseBranchesCounter);}			
					}
					else
					{	if(!currentBranchDate.after(current.getCreatedAt()))// find all the forks made in the earliest release.
						{	numberOfBranchesPerRel.put(current.getId(), ++branchCounter);			}
					}	
				}
			}
		}catch(IOException exception)
		{
			System.out.println(exception.getMessage());
		}
		
		for (Long key: numberOfBranchesPerRel.keySet())
		{
			int val = numberOfBranchesPerRel.get(key);
			System.out.println("Release id="+ key+ " , Number of branches="+val);
		}
	}
	
	public static void  getNumberOfWatchersPerRelease(RepositoryId repositoryID)
	{
		GitHubClient client = new GitHubClient();
		client.setCredentials("LumbardhAgaj", "SIRcleaner123");
		
		RepositoryService repoService = new RepositoryService(client);
		StargazerService stargazer = new StargazerService(client);
		CommitService commitService = new CommitService(client);
		Map<Long, Integer> numberOfBranchesPerRel = new HashMap<Long,Integer>(); // I am saving for each release id the number of forks that it has. It's easier to save the values in db.
		List <Release> listRelease;
		List <User> repWatchers;
		
		try
		{
			listRelease = repoService.getReleases(repositoryID); // Its the best to implement a local method to get releases, which invokes only once the github api and for each function call within the method returns a newly created object with releases.
			System.out.println(listRelease.size()+ " number of releases");
			repWatchers = stargazer.getStargazers(repositoryID);
			System.out.println(repWatchers.size()+ ":number of repo watchers");
			int nonReleaseWatchersCounter =0;
			for(int i=0;i<listRelease.size();i++)
			{
				Release current = listRelease.get(i);
				Release next = null;
				int watcherCounter = 0;	//for every release, reset the forksCounterPerRelease to zero;			
				if((i+1)!=listRelease.size()) // if this is not the earliest release than fetch it for interval comparison.
				{	next = listRelease.get(i+1); } 
				
				for(int j =0;j<repWatchers.size();j++) // for each fork, look if it falls in release i or i+i
				{
					try
					{
					Date currentWatcherDate = repWatchers.get(j).getCreatedAt();
					System.out.println(currentWatcherDate);
					
					if(next!=null)
					{
						//System.out.println("Release id:"+current.getId()+"Current release date:"+current.getCreatedAt()+", next release date"+ next.getCreatedAt()+ ", currentBranchDate:"+currentBranchDate);
						if(!currentWatcherDate.after(current.getCreatedAt()) && currentWatcherDate.after(next.getCreatedAt()))
						{	numberOfBranchesPerRel.put(current.getId(),++watcherCounter);
						
							
						}
						else if(i==0 && currentWatcherDate.after(current.getCreatedAt()))	//find all non-release forks, or forks that occurred after the latest release.
						{	numberOfBranchesPerRel.put(100L,++nonReleaseWatchersCounter);}			
					}
					else
					{	if(!currentWatcherDate.after(current.getCreatedAt()))// find all the forks made in the earliest release.
						{	numberOfBranchesPerRel.put(current.getId(), ++watcherCounter);			}
					}	
					}
					catch(NullPointerException nullExc)
					{
						System.out.println("Date not found");
					
					}
				}
			}
			
		}
		
		catch(IOException exception)
		{
			System.out.println(exception.getMessage());
		}
		
		for (Long key: numberOfBranchesPerRel.keySet())
		{
			int val = numberOfBranchesPerRel.get(key);
			System.out.println("Release id="+ key+ " , Number of watchers="+val);
		}
	}
}
