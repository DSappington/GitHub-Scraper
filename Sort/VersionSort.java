package Sort;

import java.util.LinkedList;

public class VersionSort {
	/*
	 * 		VersionSort() method
	 * 		@version: 1.0-beta
	 * 		@author: Derek Sappington
	 * 		@description:  sorts  finalList pointers before version with character/letters are appended to the end. 
	 * 		@note: Only sort numerical values. Not very affective, but both scrapers include a regex conditions to separate the complete versions
	 * 			   from subversion. regex parses versions with alphabetical character into separate List. (ex. -beta, -rc, v1.3.2, ect.) I am still looking to find an
	 * 			   affective algorithm to overcome the complexity and uncertainty that comes with a wide range for vendors verion structure.
	 * 		@idea: Search LinkedList one by one and apply a similarity percentage???   
	 * 
	 */

	public static LinkedList<String> versionSort(LinkedList<String> finalList) {
		String temp = new String();
		for (int i = 1; i < finalList.size(); i++) {
			for(int j = i ; j > 0 ; j--){
				if(compareVersion(finalList.get(j), finalList.get(j -1)) <0){
					temp = finalList.get(j);
					finalList.get(j).equals(finalList.get(j -1));
					finalList.get(j-1).equals(temp);
				}
			}
		}
		return finalList;
	} 


	//Will compare version numbers breaking it apart into a String array
	public static int compareVersion(String version1, String version2) {
		String[] arr1 = version1.split("\\.");
		String[] arr2 = version2.split("\\.");

		int i=0;
		while(i<arr1.length || i<arr2.length){
			if(i<arr1.length && i<arr2.length){
				if(Integer.parseInt(arr1[i]) < Integer.parseInt(arr2[i])){
					return -1;
				}else if(Integer.parseInt(arr1[i]) > Integer.parseInt(arr2[i])){
					return 1;
				}
				else if(Integer.parseInt(arr1[i]) == Integer.parseInt(arr2[i]))
				{
					int result = specialCompare(version1,version2);
					if(result != 0)
					{
						return result;
					}
				}
			} else if(i<arr1.length){
				if(Integer.parseInt(arr1[i]) != 0){
					return 1;
				}
			} else if(i<arr2.length){
				if(Integer.parseInt(arr2[i]) != 0){
					return -1;
				}
			}

			i++;
		}

		return 0;
	}




	// Meant for when version numbers such as 2 and 2.0 arise. This method will make sure to
	// put the smaller version number ( in length) first
	public static int specialCompare(String str1, String str2)
	{
		String[] arr1 = str1.split("\\.");
		String[] arr2 = str2.split("\\.");
		for(int i =1; i<arr1.length;i++)
		{
			if(Integer.parseInt(arr1[i]) != 0)
			{
				return 0;
			}
		}
		for(int j =1; j<arr2.length;j++)
		{
			if(Integer.parseInt(arr2[j]) != 0)
			{
				return 0;
			}
		}
		if(arr1.length < arr2.length)
		{
			return -1;
		}
		else
		{
			return 1;
		}

	}
}