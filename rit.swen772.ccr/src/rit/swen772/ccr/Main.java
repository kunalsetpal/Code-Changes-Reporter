package rit.swen772.ccr;

<<<<<<< HEAD
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.ObjectInputStream.GetField;
import java.nio.file.attribute.GroupPrincipal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.event.ForkApplyPayload;
import org.eclipse.egit.github.core.service.*;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

=======
>>>>>>> 802e326b516baf94ec368a23b37cef40155e2428
public class Main {
	
	public static void main(String [] args){
<<<<<<< HEAD
		client = new GitHubClient();
		client.setCredentials("LumbardhAgaj", "SIRcleaner123");
//		client.setOAuth2Token(Credentials.TOKEN);
		//githubClient.setCredentials(Credentials.USER_NAME, Credentials.PASSWORD);
		
		//final RepositoryId repoID = new RepositoryId("adblockplus", "adblockplusandroid");

//		RepositoryService repoService = new RepositoryService(client);
		
		RepositoryService service = new RepositoryService();
		RepositoryId repoID = new RepositoryId("libgdx","libgdx");
		CommitService cService = new CommitService();
		
		
		//COMMENT this out if you want to reduce calls
		//fetchContributorsAndCommitsPerReleaseForRepository(repoID);
		
		
		try {
			getNumberOfForksPerRelease(repoID);
			/*for(Repository repo : service.getRepositories("yarnpkg"))
				System.out.println(repo.getName());*/
			//Repository repo = service.getRepository("yarnpkg", "yarn");
			//System.out.println(repo.getDescription());
			//int counter = 0;
			//Repository repo = repoService.getRepository(repoID);
			//Repository repo = service.getRepository("yarnpkg", "yarn");
			//int counter = 0;
			/*for(RepositoryTag repoTags : service.getTags(repo)){
				System.out.println(repoTags.getName());
				++counter;
			}*/
			/*for(RepositoryRelease repoReleases : service.getReleases(repoID)){
				System.out.println(repoReleases.getTagName());
				++counter;
			}*/
			/*for(RepositoryCommit commit : cService.getCommits(repoID)){
				System.out.println(commit.getCommit().getMessage());
				System.out.println(commit.getCommit().getAuthor().getName());
				System.out.println(commit.getCommit().getAuthor().getDate());
				System.out.println(commit.getCommit().getCommitter().getName());
				System.out.println(commit.getCommit().getCommitter().getDate());
				System.out.println("============");
				counter++;
				if(counter > 20)
					break;
			}*/
			
			/*Map<RepositoryRelease, RepositoryCommit> lList = new HashMap();
			
			List<RepositoryRelease> lRepoReleases = service.getReleases(repoID);
			List<RepositoryCommit> lRepoCommit = cService.getCommits(repoID);
			
			/*Foo[] array = new Foo[list.size()];
			list.toArray(array);*/
			//.toArray()
			//RepositoryRelease[] rArray = lRepoReleases.toArray(new RepositoryRelease[lRepoReleases.size()]);

			//?since=2016-10-20T00:00:00-5:00
			//System.out.println(counter);
			/*Main m = new Main();
			Main.ORMSQLite orm = m.new ORMSQLite();*/
			
			//For Database
//			new Main().new ORMSQLite();

			
//			for(Release repoReleases : repoService.getReleases(repo)){
//				System.out.println(repoReleases.getTagName());
//				System.out.println(repoReleases.getBody());
//				System.out.println(repoReleases.getAuthor().getLogin());
//				System.out.println(repoReleases.getCreatedAt().toString());
//				++counter;
//			}
			
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	/*public static void fetchContributorsAndCommitsPerReleaseForRepository(RepositoryId repositoryID) {
		
		try {
			RepositoryService repoService = new RepositoryService(client);
			
			List <Release>listRelease = repoService.getReleases(repositoryID);
			System.out.println(listRelease.size());
			
			//Sort Releases
			Collections.sort(listRelease, (release1, release2) -> release1.getCreatedAt().compareTo(release2.getCreatedAt()));
			
			CommitService commitService = new CommitService(client);
			List <RepositoryCommit>listCommit = commitService.getCommits(repositoryID);
			
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
	}*/
	
	public static void  getNumberOfForksPerRelease(RepositoryId repositoryID) throws IOException
	{
		
		RepositoryService repoService = new RepositoryService(client);		
		List <Release> listRelease = repoService.getReleases(repositoryID);
		Map<Long, Integer> numberOfForksPerRel = new HashMap<Long,Integer>();
		
		
		List <Repository> repForks = repoService.getForks(repositoryID);
		int nonreleaseforksCounter = 0;
		
		for(int i=0;i<listRelease.size();i++)
		{
			Release current = listRelease.get(i);
			Release next = null;
			int forksCounter = 0;	//for every release, reset the forksCounterPerRelease to zero;
			
			if((i+1)!=listRelease.size())
			{
				next = listRelease.get(i+1);
			}
			
			for(int j =0;j<repForks.size();j++)
			{
				Date currentForkDate = repForks.get(j).getCreatedAt();
				if(next!=null)
				{
					if(currentForkDate.before(current.getCreatedAt()) && currentForkDate.after(next.getCreatedAt()))
					{
							numberOfForksPerRel.put(current.getId(),++forksCounter);
					}
					else if(i==0 && currentForkDate.after(current.getCreatedAt()))	//find all non-release forks, or forks that occurred after the latest release.
					{
							numberOfForksPerRel.put(100L,++nonreleaseforksCounter);
					}			
				}
				else
				{
					if(currentForkDate.before(current.getCreatedAt()))// find all the forks made in the earliest release.
					{
						numberOfForksPerRel.put(current.getId(), ++forksCounter);			
					}
				}
				
			}
		}
		
		for (Long key: numberOfForksPerRel.keySet())
		{
			int val = numberOfForksPerRel.get(key);
			System.out.println("Release id="+ key+ " , Number of forks="+val);
		}
		int relCounter=0;
		
		for(Release rel : listRelease)
		{	relCounter++;
			System.out.println("Release id"+ rel.getId()+ " , created at:"+rel.getCreatedAt()+ " release number"+ relCounter);
		}
		System.out.println(listRelease.size());

		/*int repoCounter = 0;
		for(Repository r:repForks)
		{	repoCounter++;
			System.out.println(r.getCreatedAt()+ " "+ r.getId()+ " "+ repoCounter);
		}	*/
	}
	
	
	
	
	private class ORMSQLite{
		private final String databaseUrl = "jdbc:sqlite:sample.db";
		private JdbcConnectionSource connectionSource;
=======
		
		/*
		 * DB Call
		 */
		TestDB sObject = new TestDB("Satoshi", "Ketchum");
		//sObject.save();
		
		TestDB gObject = new TestDB();
		gObject.setId(sObject.getId());
		gObject = gObject.getByID();
		System.out.println(gObject.getPassword() + " - " + gObject.getName());
		/*
		 * End DB Call
		 */
>>>>>>> 802e326b516baf94ec368a23b37cef40155e2428
		
		//GitHubCalls gitHubCalls = new GitHubCalls("yarnpkg", "yarn");
	}
}
