package Crawler;

import java.awt.AWTException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Format.Format;
import Sort.VersionSort;
/*
 * 		Spider() method
 * 		@version: 1.0-beta
 * 		@author: Derek Sappington
 * 		@description:  retrieves URL client, parses HTML tags, appends last version to current URL and continues until last version. 
 * 		@note: This may be replaced by APICaller() once APICaller has successfully been beta tested.  
 * 
 */

public class Spider  {
	static Pattern p = Pattern.compile("[a-zA-Z]+"); // regex for alphabetical values
	static Matcher m; // matches version to regex

	public LinkedList<String> Version_Spider(String url) throws IOException, AWTException {

		final String originalURL = url; //original URL 
		
		List<String> enumeratedVersion = new ArrayList<String>(); //stores version from page
		LinkedList<String> parseList = new LinkedList<String>(); // stores version with alphabetical values
		LinkedList<String> finalList = new LinkedList<String>();// stores version with numerical values

		int minimum = 1500; // sleeps scraper for 1500 secs to overcome robot detector
		int maximum = 3500; // sleeps scraper for 3500 secs to overcome robot detector

		// Print formatting so user knows what is being fetched.
		Format.print("Fetching %s...", url);

		while (true) {
			/*
			 * Grabs the url HTML and parses it
			 */
			Document doc = Jsoup.connect(url).get();
			Elements tagName = doc.getElementsByClass("tag-name");

			// Reports number of versions
			Format.print("\nVersions on page: (%d) ", tagName.size());
			Format.print("\nVersions scraped: (%d)", enumeratedVersion.size());

			// Loops version
			for (Element version : tagName) {

				enumeratedVersion.add(version.text().toString());
				Format.print(" * " + enumeratedVersion.size() + ": %s %s", version.attr("tag-name"), Format.trim(version.text(), 35));

			}

			// Checks if the version on the page have been scraped and RETURN BACK TO MAIN
			if (tagName.size() == 0) {
				String[] completeArr = new String[enumeratedVersion.size()];

				completeArr = enumeratedVersion.toArray(completeArr);




				// clean up before sorting
				for(int i =0; i < completeArr.length;i++) {

					m = p.matcher(completeArr[i]);

					if(m.find()) {
						parseList.add(completeArr[i]);	
					} else if (!m.find()) {
						finalList.add(completeArr[i]);	
					}


				}

				VersionSort.versionSort(finalList);
				finalList.add("\n");
				finalList.addAll(parseList);

				return finalList;


			}

			url = originalURL + "?after=" + enumeratedVersion.get(enumeratedVersion.size() - 1);
			Format.print("", url);

			try {
				int randomNum = minimum + (int) (Math.random() * maximum);
				Thread.sleep(randomNum);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
}





