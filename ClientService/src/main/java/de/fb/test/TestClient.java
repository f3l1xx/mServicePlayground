package de.fb.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import com.google.common.io.CharStreams;
import com.google.common.util.concurrent.Uninterruptibles;

public class TestClient {
	private static String host = MoreObjects.firstNonNull(System.getenv("MSG_SERVICE_HOST"), "localhost");

	public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {

		Runnable r1 = new Runnable() {

			public void run() {

				while (true) {

					try {
						URL url = new URL("http://" + host + ":8075/msg-analyze/test%20happy%20foo");

						HttpURLConnection myURLConnection = (HttpURLConnection) url.openConnection();

						// myURLConnection.setRequestProperty("Authorization",
						// "Basic dGVzdDpzZWNyZXQ=");

						myURLConnection.setRequestProperty("Authorization", "Basic bWF4QG11c3Rlci5kZTpzZWNyZXQ="); // max@muster.de:secret

						InputStream is = myURLConnection.getInputStream();
						ObjectMapper mapper = new ObjectMapper();
						MessageAnalysis value = mapper.readValue(is, MessageAnalysis.class);
						System.out.println(value);
						Uninterruptibles.sleepUninterruptibly(20, TimeUnit.MILLISECONDS);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		Runnable r2 = new Runnable() {

			public void run() {

				while (true) {

					try {
						URL url = new URL("http://" + host + ":8075/msg-analyze/ping");

						HttpURLConnection myURLConnection = (HttpURLConnection) url.openConnection();
						myURLConnection.setRequestProperty("Authorization", "Basic bWF4QG11c3Rlci5kZTpzZWNyZXQ="); // max@muster.de:secret

						InputStream is = myURLConnection.getInputStream();
						System.out.println(CharStreams.toString(new InputStreamReader(is, "UTF-8")));

						Uninterruptibles.sleepUninterruptibly(new Random().nextInt(50), TimeUnit.MILLISECONDS);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		new Thread(r1).start();
		new Thread(r2).start();

		Thread thread = new Thread(r2);
		thread.start();
		thread.join();

	}

}
