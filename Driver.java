//Richard Dzreke
//package com.usf.245.a2;
package CS245_Project02;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import javax.swing.text.Document;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;

public class Driver {
	private static final int Integer_MAX = 0;
	/** getWebpage gets that contents of the website and returns
	 * @param url
	 * @return String holding contents of website
	 * @throws IOException
	 */
	public static String getWebpageContent(String url) throws IOException {
		org.jsoup.Connection conn = Jsoup.connect(url);
		org.jsoup.nodes.Document doc =  conn.get();
		String result = ((org.jsoup.nodes.Document) doc).body().text();
		return result;
	}
	/**formLinked list is a void method that handles most functions of my program
	 * @param contents
	 * @param q
	 * @throws IOException
	 */
	public static void formLinkedList(String contents, Queue q) throws IOException {
		Properties prop = readPropertiesFile("C:\\Windows\\Temp\\data_files\\textanalyzer.properties");
		String[] arr = prop.getProperty("avoid").split(",");
		//ArrayList created to handle words to avoid
		ArrayList<String> avoid = new ArrayList<String>();
		//Adds the words to be avoided into an ArrayList
		for(int i = 0; i < arr.length; i++) {
			avoid.add(arr[i]);
		}
		//Splits the string holding all the contents of the website into individual Characters
		char[] arr2 = contents.toCharArray();
		String contents2 = "";
		//Loops through the Array created and outputs a new string that contains only letters, numbers, "'", and spaces
		for(int i = 0; i < arr2.length; i++) {
			int ascii = (int)arr2[i];
			if(Character.isLetter(arr2[i]) || Character.isDigit(arr2[i])) {
				contents2+=arr2[i];
			}else if(ascii == 0x0020 || ascii == 0x0027) {
				contents2+=arr2[i];
			}else {
				contents2+=" ";
			}
		}
		//A third array is used to contain the individual words that can be formed from splitting
		//the array by " " 
		String[] arr3 = contents2.split(" ");
		//New Linkedlist is created of object type Words, which I created to hold a word and it's count
		LinkedList<Words> list = new LinkedList<>();
		//This arraylist was created to keep track of the individual words and make sure that I don't repeatedly add a word
		//to my linkedlist
		ArrayList<String> listRepeat = new ArrayList<>();
		//This for loop handles me adding words to my linked list
		for(int i = 0; i < arr3.length; i++) {
			if(!arr3[i].equals("")) {
				//A new Words object is created and trimmed of spaces trailing or proceeding it
				//
				Words word = new Words(arr3[i].trim());
				String word2 = word.getWord();
				//If statement checks if the word is only in the Linkedlist already 
				if(Repeat(word2, listRepeat) == false) {
					//Checks if it's one of the words to be avoided
					if(toAvoid(word2, avoid) == false) {
						//If all the conditions are met the count of the word is set and the word is added to
						//my LinkedList
						word.setCount(wordCount(word2, arr3));
						list.add(word);
						//word is also added to my ArrayList of repeated words
						listRepeat.add(word.getWord());
					}
				}
			}
		}
		//Sorts my LinkedList based on my compartor
		Collections.sort(list, new Comparator<Words>(){

			@Override
			//My comparator sorts my LinkedList based on the frequency a word occurs
			public int compare(Words o1, Words o2) {
				if(o1.getCount() > o2.getCount()) {
					return -1;
				}else if(o1.getCount() < o2.getCount()) {
					return 1;
				}else {
					return 0;
				}
			}
			
		});
		//This is the main method that handles the output of my program
		deQueue(list, q);
	}
	/**This method checks if the word that is being attempted to be added to my LinkedList already exists in the LinkedList 
	 * @param word
	 * @param arr
	 * @return whether the word is already in my LinkedList
	 */
	public static boolean Repeat(String word, ArrayList<String> arr){
		boolean res = false;
		if(arr.size() == 0) {
			return res;
		}
		for(int i = 0; i < arr.size(); i++) {
			if(word.equals(arr.get(i))) {
				res = true;
				break;
			}
		}
		return res;
	}
	/**Gets the count of the word that is being added to my LinkedList in the website 
	 * @param word
	 * @param line
	 * @return the count of the word on the website
	 */
	public static int wordCount(String word, String[] line) {
		int count = 0;
		for(int i = 0; i < line.length; i++) {
			if(word.equals(line[i].toLowerCase())) {
				count++;
			}
		}
		return count;
	}
	/**Returns true if the word attempted to be added to my LinkedList matches any of the words in my property file 
	 * @param word
	 * @param avoid
	 * @return whether the word that is being attempted to be added should be avoided
	 */
	public static boolean toAvoid(String word, ArrayList<String> avoid) {
		for(int i = 0; i < avoid.size(); i++) {
			if(word.equals(avoid.get(i))) {
				return true;
			}
		}
		return false;
	}
	
	/**A void function that prints out the top 10 words ocuring words in my sorted LinkedList
	 * @param list
	 * prints out the results if the user enters in the command "Top10"
	 */
	public static void topTen(LinkedList<Words> list) {
		int count = 0;
		System.out.print("Top 10 words: ");
		for(Iterator b = list.iterator(); b.hasNext();) {
			Words word2 = (Words)b.next();
			if(count <= 8) {
				System.out.print(word2.getWord() + ": " + word2.getCount() + "| ");
			}
			if(count == 9) {
				System.out.print(word2.getWord() + ": " + word2.getCount() + "\n");
				break;
			}
			count++;
		}
	}
	/** This does the same thing as TopTen, except that it returns the top 100 words in my LinkedList 
	 * @param list
	 * prints out the top 100 words in the LinkedList
	 */
	public static void topOneHundred(LinkedList<Words> list) {
		int count = 0;
		System.out.print("Top 100 words: ");
		for(Iterator b = list.iterator(); b.hasNext();) {
			Words word2 = (Words)b.next();
			if(count <= 98) {
				System.out.print(word2.getWord() + ": " + word2.getCount() + "| ");
			}
			if(count == 99) {
				System.out.print(word2.getWord() + ": " + word2.getCount() + "\n");
				break;
			}
			count++;
		}
	}
	/** Handles reading through my property file 
	 * @param file
	 * @return prop, which is a properties object
	 * @throws IOException
	 */
	public static Properties readPropertiesFile(String file) throws IOException {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream(file);
			prop = new Properties();
			prop.load(fis);
			
		}catch(FileNotFoundException fnfe){
			fnfe.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}finally {
			fis.close();
		}
		return prop;
	}
	/** This the main method that handles the commands from the user and outputs the results accordingly 
	 * @param list
	 * @param q
	 * handles my Priority Queue and outputs the results of the commands
	 */
	public static void deQueue(LinkedList<Words> list, Queue q) {
		//shortestlen is used to help find the shorted word in my LinkedList
		int shortestlen = Integer.MAX_VALUE;
		//shortest holds the shortest word in my LinkedList
		String shortest = "";
		//longest holds the longest word in my LinkedList
		String longest = "";
		//count holds the frequency of the longest word in my LinkedList
		int count = 0;
		//count2 holds the frequency of the shortest word in my LinkedList
		int count2 = 0;
		//totalWords holds the amount of total words in the website
		int totalWords = 0;
		//totalLetters hold the amount of total letters in the website
		int totalLetters = 0;
		//loops through my linked list to find the longest and shortest words and the frequency
		//of their occurrence
		for(Iterator b = list.iterator(); b.hasNext();) {
			Words word = (Words)b.next();
			String word2 = word.getWord();
			if(word2.length() > longest.length()) {
				longest = word2;
				count = word.getCount();
			}
			if(word2.length() < shortestlen) {
				shortest = word2;
				shortestlen = word2.length();
				count2 = word.getCount();
			}
			totalWords+=word.getCount();
			//handles count if words have an apostrophe included
			if(word2.contains("'")) {
				totalLetters+=(word2.length()-1)*word.getCount();
			//regular count formula without apostrophe
			}else {
				totalLetters+=word2.length()*word.getCount();
			}
		}
		//Loops through my priority queue of commands
		//Outputs results of the command
		while(q.size() > 0) {
			String command = (String)q.remove();
			if(command.equals("Top10")) {
				topTen(list);
			}else if(command.equals("Top100")) {
				topOneHundred(list);
			}else if(command.equals("Longest")) {
				System.out.println("Longest:" + longest + ": " + count);
			}else if(command.equals("Shortest")) {
				System.out.println("Shortest: " + shortest + ": " + count2);
			}else if(command.equals("Summary")) {
				System.out.println("Summary: Total Words: " + totalWords + "| Total Letters: " + totalLetters);
			}else {
				System.out.println("Invalid command: Enter \"Top10\", \"Top100\", \"Longest\", \"Shortest\", or \"Summary\"");
			}
		}
	}
	public static void main(String[] args) throws IOException {
		//Gets the web address from the first argument
		String website = args[0];
		//contents contains the text that is inside the website
		String contents = getWebpageContent(website);
		Scanner scan = new Scanner(System.in);
		Queue<String> queue = new LinkedList<>();
		String input;
		//While loop handles the user input and adds it to my Priority Queue
		while(scan.hasNextLine() && (input = scan.nextLine()).length() != 0) {
			queue.add(input);
		}
		scan.close();
		//formLinkedList handles all other necessary functions of the program
		formLinkedList(contents, queue);
	}
}
