package de.fb;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.UrlEscapers;

import de.fb.MessageAnalysis.Sentiment;

public class MessageAnalyzerService {

	private ObjectMapper mapper = new ObjectMapper();

	private static final String HOST_SENTIMENT = "msg-sentiment";
	private static final String HOST_WORDCOUNT = "msg-word-count";

	public MessageAnalysis analyze(String text) throws InterruptedException, ExecutionException {

		LambdaHystrixCommand<Integer> countCmd = new LambdaHystrixCommand<>("CountCmd", () -> callCountingService(text),
				() -> 1);

		LambdaHystrixCommand<Sentiment> sentimentCmd = new LambdaHystrixCommand<>("SentimentCmd",
				() -> callSentimentService(text), () -> Sentiment.NEUTRAL);

		// async
		Future<Integer> futureCount = countCmd.queue();
		Future<Sentiment> futureSentiment = sentimentCmd.queue();

		return new MessageAnalysis(futureSentiment.get(), futureCount.get());
	}

	private int callCountingService(String text) {
		try {
			return mapper.readValue(new URL("http://" + HOST_WORDCOUNT + ":8070/count/" + escape(text)), Integer.class);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private Sentiment callSentimentService(String text) {
		try {
			Sentiment value = mapper.readValue(new URL("http://" + HOST_SENTIMENT + ":8090/sentiment/" + escape(text)),
					Sentiment.class);
			return value;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	static String escape(String text) {
		return UrlEscapers.urlFragmentEscaper().escape(text);
	}

}
