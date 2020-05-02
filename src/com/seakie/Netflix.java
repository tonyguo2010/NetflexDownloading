package com.seakie;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Netflix {

	private static String playListUrl;
	private static String urlBase;

	public static void setPlaylist(String string) throws MalformedURLException {
		playListUrl = string;
		urlBase = new URL(playListUrl).getProtocol() + "://" + new URL(playListUrl).getHost();
	}

	public static void startParse() throws IOException, InterruptedException {
		Document doc = Jsoup.connect(playListUrl).get();

		// Elements links = doc.select("a[href]");
		Elements links = doc.getElementsByClass("pl-video-title-link yt-uix-tile-link yt-uix-sessionlink  spf-link ");

		for (Element link : links) {
			// System.out.println(urlBase + link.attr("href"));
			AsyncYoutubeDownloader videoPage = new AsyncYoutubeDownloader(urlBase + link.attr("href"));
			videoPage.start();
			Thread.sleep(5000);
		}
	}



}
