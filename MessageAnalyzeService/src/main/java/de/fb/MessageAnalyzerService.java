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

	ObjectMapper mapper = new ObjectMapper();

	public MessageAnalysis analyze(String text) throws InterruptedException, ExecutionException {

		LambdaHystrixCommand<Integer> countCmd = new LambdaHystrixCommand<>(() -> callCountingService(text), () -> 1);

		LambdaHystrixCommand<Sentiment> sentimentCmd = new LambdaHystrixCommand<>(() -> callSentimentService(text),
				() -> Sentiment.NEUTRAL);

		Future<Integer> futureCount = countCmd.queue();
		Future<Sentiment> futureSentiment = sentimentCmd.queue();

		return new MessageAnalysis(futureSentiment.get(), futureCount.get());
	}

	private int callCountingService(String text) {
		try {
			return mapper.readValue(new URL("http://localhost:8070/count/" + escape(text)), Integer.class);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private Sentiment callSentimentService(String text) {
		try {
			Sentiment value = mapper.readValue(new URL("http://localhost:8090/sentiment/" + escape(text)),
					Sentiment.class);
			return value;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private String escape(String text) {
		return UrlEscapers.urlFragmentEscaper().escape(text);
	}

}
