package rit.swen772.ccr;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

public class DBCalls {
	private final String databaseUrl = "jdbc:sqlite:sample.db";
	private static JdbcConnectionSource connectionSource;
	private static DBCalls dbCalls = null;
	
	private DBCalls(){
		try {
			connectionSource = new JdbcConnectionSource(this.databaseUrl);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static DBCalls getDBCalls(){
		if(dbCalls == null){
			synchronized (DBCalls.class) {
				if(dbCalls == null)
					dbCalls = new DBCalls();
			}
		}
		return dbCalls;
	}
	
	public JdbcConnectionSource getConnectionSource(){
		synchronized (JdbcConnectionSource.class) {
			return connectionSource;
		}
	}
	
	public boolean closeConnection(){
		synchronized (JdbcConnectionSource.class) {
			try {
				connectionSource.close();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
}
