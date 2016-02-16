package de.fb.test;

import java.util.Set;

import com.google.common.base.Splitter;

import jersey.repackaged.com.google.common.collect.Sets;

public class SentimentLogic {

	private static Splitter splitter = Splitter.on(' ').trimResults().omitEmptyStrings();

	private static Set<String> happy = Sets.newHashSet("wow", "great", "happy", "lucky", ":-)");
	private static Set<String> sad = Sets.newHashSet("sad", "no", "unhappy", "unlucky", ":-(");
	
	public static Set<String> split(String sentence) {
		return Sets.newHashSet(splitter.split(sentence.toLowerCase()));
	}

	public static int getHappyCount(Set<String> splitted) {
		
		//TODO get recent list of words from another service
		
		return  Sets.intersection(splitted, happy).size();
	}

	public static int getUnHappyCount(Set<String> splitted) {
		return  Sets.intersection(splitted, sad).size();
	}

	
	
}
