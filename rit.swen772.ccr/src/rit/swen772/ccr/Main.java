package rit.swen772.ccr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.*;
import org.eclipse.egit.github.core.service.*;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class Main {
	public static void main(String [] args){
		/*GitHubClient client = new GitHubClient();
		client.setOAuth2Token(Credentials.TOKEN);
		
		RepositoryService service = new RepositoryService();
		RepositoryId repoID = new RepositoryId("yarnpkg", "yarn");
		CommitService cService = new CommitService();*/
		try {
			/*for(Repository repo : service.getRepositories("yarnpkg"))
				System.out.println(repo.getName());*/
			//Repository repo = service.getRepository("yarnpkg", "yarn");
			//System.out.println(repo.getDescription());
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
