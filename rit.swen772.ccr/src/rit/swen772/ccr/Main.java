package rit.swen772.ccr;

public class Main {
	
	public static void main(String [] args){
		
		/*
		 * DB Call
		 */
		//DBCalls dbCall = new DBCalls();
		//dbCall.saveNamePassword("Satoshi", "Ketchum");
		/*
		 * End DB Call
		 */
		
		GitHubCalls gitHubCalls = new GitHubCalls("yarnpkg", "yarn");
	}
}
