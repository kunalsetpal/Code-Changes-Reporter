package rit.swen772.ccr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class Main {
	public static void main(String [] args){
		GitHubClient client = new GitHubClient();
		client.setOAuth2Token(Credentials.TOKEN);
		//githubClient.setCredentials(Credentials.USER_NAME, Credentials.PASSWORD);
		
		//final RepositoryId repoID = new RepositoryId("adblockplus", "adblockplusandroid");

		RepositoryService repoService = new RepositoryService(client);
		
		RepositoryService service = new RepositoryService();
		RepositoryId repoID = new RepositoryId("yarnpkg", "yarn");
		CommitService cService = new CommitService();
		try {
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
			new Main().new ORMSQLite();
			
			List <Release>listRelease = repoService.getReleases(repoID);
			System.out.println(listRelease.size());
			
//			for(Release repoReleases : repoService.getReleases(repo)){
//				System.out.println(repoReleases.getTagName());
//				System.out.println(repoReleases.getBody());
//				System.out.println(repoReleases.getAuthor().getLogin());
//				System.out.println(repoReleases.getCreatedAt().toString());
//				++counter;
//			}
			
			Collections.sort(listRelease, (release1, release2) -> release1.getCreatedAt().compareTo(release2.getCreatedAt()));
			
			CommitService commitService = new CommitService(client);
			List <RepositoryCommit>listCommit = commitService.getCommits(repoID);
			
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
			printWriter.close();
			
//			System.out.println(counter);
			
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	private class ORMSQLite{
		private final String databaseUrl = "jdbc:sqlite:sample.db";
		private JdbcConnectionSource connectionSource;
		
		public ORMSQLite(){
			try {
				connectionSource = new JdbcConnectionSource(databaseUrl);
				Dao<TestDB, String> testDB = DaoManager.createDao(connectionSource, TestDB.class);
				TableUtils.createTableIfNotExists(connectionSource, TestDB.class);
				TestDB tdb = new TestDB("Yakuma", "Saito");
				testDB.create(tdb);
				connectionSource.close();
			} catch(Exception e) { e.printStackTrace(); }
		}
	}
}
