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
import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.Release;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryBranch;
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
	private CommitService cService;
	private String userName;
	private String project;
	private List<Repository> repositories;
	private List<Release> releases;
	private List<RepositoryCommit> commits;
	private List<RepositoryBranch> branches;
	private List<RepositoryTag> tags;
	private List <Repository> forks;
	
	public GitHubCalls(String userName, String project){
		this.userName = userName;
		this.project = project;
		this.client = new GitHubClient();
		//this.client.setCredentials(Credentials.USER_NAME, Credentials.PASSWORD);
		this.client.setOAuth2Token(Credentials.TOKEN);
		this.service = new RepositoryService();
		this.repoID = new RepositoryId(this.userName, this.project);
		this.cService = new CommitService();
		
		this.repositories = null;
		this.releases = null;
		this.commits = null;
		this.branches = null;
		this.tags = null;
		this.forks = null;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
		this.repoID = new RepositoryId(this.userName, this.project);
	}
	
	public void getRepositories(){
		// get list of repositories
		try {
			this.repositories = this.service.getRepositories(this.userName);
			for(Repository repo : this.repositories)
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
			this.tags = this.service.getTags(this.repoID);
			for(RepositoryTag repoTags : this.tags){
				System.out.println(repoTags.getName());
			}
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public void getReleases(){
		// get releases for repository
		try {
			this.releases = this.service.getReleases(this.repoID);
			for(Release repoReleases : this.releases){
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
			this.commits = cService.getCommits(repoID);
			int counter = 0;
			for(RepositoryCommit commit : this.commits){
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
			if(this.releases == null)
				this.releases = this.service.getReleases(this.repoID);
			//System.out.println(listRelease.size());
			
			//Sort Releases
			Collections.sort(this.releases, (release1, release2) -> release1.getCreatedAt().compareTo(release2.getCreatedAt()));
			
			List <RepositoryCommit> listCommit = null;
			if(this.commits != null)
				listCommit = this.commits;
			else
				listCommit = cService.getCommits(this.repoID);
			
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
			
			for (Release release : this.releases) {
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
	
	public void getNumberOfForksPerRelease()
	{	//TODO: CHECK getContributorsAndCommitsPerRelease() FOR SORTING AND FILTERING
		Map<Long, Integer> numberOfForksPerRel = new HashMap<Long,Integer>(); // I am saving for each release id the number of forks that it has. It's easier to save the values in db.
		try
		{
			if(this.releases == null)
				this.releases = this.service.getReleases(this.repoID);
			// Its the best to implement a local method to get releases, which invokes only once the github api and for each function call within the method returns a newly created object with releases.
			if(this.forks == null)
				this.forks = this.service.getForks(this.repoID);
			int nonReleaseForksCounter = 0;
		
			for(int i=0;i<this.releases.size();i++)
			{
				Release current = this.releases.get(i);
				Release next = null;
				int forksCounter = 0;	//for every release, reset the forksCounterPerRelease to zero;			
				if((i+1)!=this.releases.size()) // if this is not the earliest release than fetch it for interval comparison.
				{	next = this.releases.get(i+1); } 
				
				for(int j =0;j<this.forks.size();j++) // for each fork, look if it falls in release i or i+i
				{
					Date currentForkDate = this.forks.get(j).getCreatedAt();
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
			
			for (Long key: numberOfForksPerRel.keySet())
			{
				int val = numberOfForksPerRel.get(key);
				System.out.println("Release id="+ key+ " , Number of forks="+val);
			}
		}catch(IOException exception)
		{
			System.out.println(exception.getMessage());
		}
	}
	
	public void  getNumberOfBranchesPerRelease()
	{	//TODO: CHECK getContributorsAndCommitsPerRelease() FOR SORTING AND FILTERING
		Map<Long, Integer> numberOfBranchesPerRel = new HashMap<Long,Integer>(); // I am saving for each release id the number of forks that it has. It's easier to save the values in db.
		try
		{
			if(this.releases == null)
				this.releases = this.service.getReleases(this.repoID);
			// Its the best to implement a local method to get releases, which invokes only once the github api and for each function call within the method returns a newly created object with releases.
			if(this.branches == null)
				this.branches = this.service.getBranches(this.repoID);

			int nonReleaseBranchesCounter = 0;
			
			for(int i=0;i<this.releases.size();i++)
			{
				Release current = this.releases.get(i);
				Release next = null;
				int branchCounter = 0;	//for every release, reset the forksCounterPerRelease to zero;			
				if((i+1)!=this.releases.size()) // if this is not the earliest release than fetch it for interval comparison.
				{	next = this.releases.get(i+1); } 
				
				for(int j =0;j<this.branches.size();j++) // for each fork, look if it falls in release i or i+i
				{
					//TODO: VERIFY IF getSha() RETRIEVES AN UNIQUE IDENTIFIER AND GET IT FROM THE this.commits
					String branchCommitSha = this.branches.get(j).getCommit().getSha();
					RepositoryCommit commitcomment = this.cService.getCommit(this.repoID, branchCommitSha);
					Date currentBranchDate = commitcomment.getCommit().getCommitter().getDate();	
					if(next!=null)
					{
						if(!currentBranchDate.after(current.getCreatedAt()) && currentBranchDate.after(next.getCreatedAt()))
						{	numberOfBranchesPerRel.put(current.getId(),++branchCounter);		 }
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
}
