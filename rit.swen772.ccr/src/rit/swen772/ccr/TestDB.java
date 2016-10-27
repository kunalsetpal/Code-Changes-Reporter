package rit.swen772.ccr;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

@DatabaseTable(tableName = "testdb")
public class TestDB implements IDAO {
	//@DatabaseField(generatedId = true)
	//private int id;
	@DatabaseField(id = true)
	private String name;
	@DatabaseField
	private String password;
	
	private static final Dao<TestDB, String> testDB = initialize();
	
	public TestDB() { }
	
	public TestDB(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	private static Dao<TestDB, String> initialize(){
		Dao<TestDB, String> temp;
		try {
			temp = DaoManager.createDao(DBCalls.getDBCalls().getConnectionSource(), TestDB.class);
			return temp;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		try {
			TableUtils.createTableIfNotExists(DBCalls.getDBCalls().getConnectionSource(), TestDB.class);
			testDB.create(this);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public TestDB get() {
		// TODO Auto-generated method stub
		if(!this.name.equals("")){
			synchronized (TestDB.class) {
				try {
					return testDB.queryForId(this.name);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
