package de.fb.test;

import java.util.Set;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class SentimentController {

	@RequestMapping("/hello")
	String home() {
		return "Hello World!";
	}

	@RequestMapping("/sentiment/{sentence}")
	Sentiment getSentiment(@PathVariable String sentence) {

		System.out.println("Analyzing sentiment for " + sentence);
		
		Set<String> splitted = SentimentLogic.split(sentence);

		int happyCount = SentimentLogic.getHappyCount(splitted);
		int sadCount = SentimentLogic.getUnHappyCount(splitted); 

		if (happyCount == sadCount) {
			return Sentiment.NEUTRAL;
		} else {
			return happyCount > sadCount ? Sentiment.POSITIV : Sentiment.NEGATIV;
		}
	}

}
