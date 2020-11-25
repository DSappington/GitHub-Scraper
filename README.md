# GitHub Scraper
This is an old project that I created a while back that explores the various methods of scraping a GitHub repository to collect repository versions.  
<b>Standard scrape:</b> scraping each version page independently (Not recommended)  
<b>GitHub API: </b> A much more efficient way of data collection and is perferred. 

# PLEASE NOTE
This is currently a side project and for EDUCATIONAL purposes. This project is currently in no way, shape, or form an ongoing project and was developed as an idea.


# GitHub-Scraper.jar

 GitHub-Scraper.jar is a fun little Windows taskbar tool that I created that puts a friendly GUI interface in front of the user without overwhelming them with a full application.   

Simply put, the windows taskbar tool (when executed) will appear at the bottom right.  
1. When the task (at the bottom right) is clicked, it will ask for a GitHub URL (Example, "https://github.com/DSappington/GitHub-Scraper").   
2. Once entered, A file saver will appear.  
3. Save the file to where ever you would like, and we are done.

Check the file and all of the versions will be written to that designated location.  
Below is the Code and how it functions:
<b>APICaller:</b> utilizes the GitHub API to make called to a GitHub repo   
<b>App:</b> Windows Taskbar executable  
<b>Crawler:</b> (OLD) crawls the GitHub repo webpage.  
<b>Format:</b> Format class to clean up System calls. (easier for debugging purposes).  
<b>VersionSort:</b> Sorts the versions collected before writing to file.  
<b>VersionWriter:</b> Write the versions (collected) to a file.  
