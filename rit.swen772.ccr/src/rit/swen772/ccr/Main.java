package rit.swen772.ccr;


public class Main {
	
	public static void main(String [] args){
			
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
			
			//GitHubCalls gitHubCalls = new GitHubCalls("yarnpkg", "yarn");
		}
}
