package APICaller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import Format.Format;
import Sort.VersionSort;

/*
 * 		callGitAPI() method 
 * 		@version: 1.0-beta
 * 		@author: Derek Sappington
 * 		@description: Formats vendor/product, Calls GitHub API, parses GitHub json tags from vendor/product.   
 * 		@note: GitHub rate limit is 5000 request per hour. class needs auth0 token.
 */


public class GitHubAPICall {


	public LinkedList<String> callGitAPI(String url) throws MalformedURLException, IOException {

		Pattern p = Pattern.compile("[a-zA-Z]+"); // to parse Alphabetical values 
		Matcher m; // Match version to regex
		String str; // split json tag value
		
		ArrayList<String> arr = new ArrayList<String>();				// stores GitHub API json tags
		LinkedList<String> parseList = new LinkedList<String>();		// stores tags with alphabetical values
		LinkedList<String> finalList = new LinkedList<String>();		// stores tags with numerical values

		url  = parseURL(url); // parses user URL to get vendor/product

		Format.print("Fetching %s...", url); //print parsed URL 

		HttpURLConnection httpcon = (HttpURLConnection) new URL(url).openConnection(); // request version 
		
		httpcon.addRequestProperty("User-Agent", "Mozilla/5.0");	// HTTP properties 


		BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream())); //read input stream


		//Read line by line
		StringBuilder responseSB = new StringBuilder(); 
		String line;
		while ( ( line = in.readLine() ) != null) {
			responseSB.append("\n"+ line);

		}
		in.close();

		Arrays.stream(responseSB.toString().split("\"ref\":")).skip(1).map(l -> l.split(",")[0]).forEach(l -> arr.add((String) l));

		//parses Version and add to finalList
		for(int i = 0; i < arr.size(); i++){
			str = new String((String) arr.get(i));
			String[] parts = str.split("refs/tags/");
			parseList.add(parts[1].replaceAll("\"", ""));
		}
		
		arr.removeAll(arr);
		//loops through finalList to move index with string to end
		for(int i = 0; i < parseList.size(); i++) {
			m = p.matcher(parseList.get(i));

			if(m.find()) {
				arr.add(parseList.get(i)); // Version with strings
			} else if (!m.find()) {
				finalList.add(parseList.get(i)); // Version without strings
			}
		}
		//sorts version correctly formatted version 
		VersionSort.versionSort(finalList);
		finalList.add("\n");
		finalList.addAll(arr);

		// tells user scrape is complete 
		return finalList; 

	}



	//Parses URL 
	private String parseURL(String url) {

		String[] parts = url.split(".com");
		String part2 = parts[1];

		String[] split = part2.split("/");
		String product = split[1] + "/" +split[2];


		String newURL = "https://api.github.com/repos/" + product + "/git/refs/tags";

		return newURL;
	}


}
