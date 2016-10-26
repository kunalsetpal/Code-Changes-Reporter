package rit.swen772.ccr;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBCalls {
	private final String databaseUrl = "jdbc:sqlite:sample.db";
	private JdbcConnectionSource connectionSource;
	
	public DBCalls(){
		try {
			this.connectionSource = new JdbcConnectionSource(databaseUrl);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveNamePassword(String name, String password){
		Dao<TestDB, String> testDB;
		try {
			testDB = DaoManager.createDao(connectionSource, TestDB.class);
			TableUtils.createTableIfNotExists(connectionSource, TestDB.class);
			TestDB tdb = new TestDB(name, password);
			testDB.create(tdb);
			connectionSource.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
