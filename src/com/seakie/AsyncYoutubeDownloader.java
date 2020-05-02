package com.seakie;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AsyncYoutubeDownloader extends Thread {
	private String videoPageUrl = null;
	private String str1080 = null;
	private String str720 = null;
	private String strAudio = null;
	private String title = null;

	public AsyncYoutubeDownloader(String videoPageUrl) {
		super();
		this.videoPageUrl = videoPageUrl;
	}

	@Override
	public void run() {
		try {
			parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.run();
	}

	public void parse() throws IOException {
//		System.out.println(videoPageUrl);
		Document doc = Jsoup.connect(videoPageUrl).get();
		title = doc.title();
		Elements links = doc.getElementsByTag("script");
//		System.out.println(links.size());
		for (Element link : links) {
			if (link.toString().contains("var ytplayer = ytplayer")) {
//				System.out.println(link);
				String jsonResource = link.html().replace("var ytplayer = ytplayer || {};ytplayer.config = ", "")
						.replace(
								";ytplayer.load = function() {yt.player.Application.create(\"player-api\", ytplayer.config, ytplayer.web_player_context_config);ytplayer.config.loaded = true;};(function() {if (!!window.yt && yt.player && yt.player.Application) {ytplayer.load();}}());",
								"");
				fetchMedia(jsonResource);
			}
		}
	}
	
	private void fetchMedia(String jsonResource) throws MalformedURLException, IOException {
		JSONObject obj = new JSONObject(jsonResource);
		String strResponse = obj.getJSONObject("args").get("player_response")
				.toString();

		JSONObject response = new JSONObject(strResponse);

		JSONArray arr = response.getJSONObject("streamingData").getJSONArray("adaptiveFormats");
		for (int i = 0; i < arr.length(); i++) {
			if (arr.getJSONObject(i).getString("quality").contains("1080") && str1080 == null){
				str1080 = arr.getJSONObject(i).getString("url");
				System.out.println("Video 1080:");
				System.out.println(str1080);
			}
			if (arr.getJSONObject(i).getString("quality").contains("720") && str720 == null){
				str720 = arr.getJSONObject(i).getString("url");
				System.out.println("Video 720:");
				System.out.println(str720);
			}
			if (arr.getJSONObject(i).getString("mimeType").contains("mp4a") && strAudio == null){
				strAudio = arr.getJSONObject(i).getString("url");
				System.out.println("Audio:");
				System.out.println(strAudio);
			}
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");  
	    Date date = new Date();  
//	    System.out.println(formatter.format(date));  

	    title = formatter.format(date);
		File dir = new File(title);
		dir.mkdir();
		if (str1080 != null) {
			FileUtils.copyURLToFile(new URL(str1080), new File(title + "\\video.mp4"));
		} else if (str720 != null){
			FileUtils.copyURLToFile(new URL(str720), new File(title + "\\video.mp4"));
		}
		FileUtils.copyURLToFile(new URL(strAudio), new File(title + "\\audio.m4a"));
		System.out.println("File downloaded");
	}

}
