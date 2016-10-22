package rit.swen772.ccr;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "testdb")
public class TestDB {
	@DatabaseField(id = true)
	private String name;
	@DatabaseField
	private String password;
	
	public TestDB(){
		//needed
	}
	
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
}
