package de.fb;

public class MessageAnalysis {
	public static enum Sentiment {
		NEUTRAL, POSITIV, NEGATIV;
	}

	private Sentiment sentiment;
	private int wordCount;

	public MessageAnalysis() {
	}

	public MessageAnalysis(Sentiment sentiment, int wordCount) {
		super();
		this.sentiment = sentiment;
		this.wordCount = wordCount;
	}

	public Sentiment getSentiment() {
		return sentiment;
	}

	public void setSentiment(Sentiment sentiment) {
		this.sentiment = sentiment;
	}

	public int getWordCount() {
		return wordCount;
	}

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

}
