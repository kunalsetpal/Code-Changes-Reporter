package rit.swen772.ccr;

public class Main {
	
	public static void main(String [] args){
		
		/*
		 * DB Call
		 */
		TestDB sObject = new TestDB("Satoshi", "Ketchum");
		//sObject.save();
		
		TestDB gObject = new TestDB();
		gObject.setName("Hiraga");
		//gObject = gObject.get();
		System.out.println(gObject.getPassword());
		/*
		 * End DB Call
		 */
		
		//GitHubCalls gitHubCalls = new GitHubCalls("yarnpkg", "yarn");
	}
}
