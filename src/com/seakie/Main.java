package com.seakie;

import java.io.IOException;
import java.net.MalformedURLException;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		Netflix.setPlaylist("https://www.youtube.com/playlist?list=PLvahqwMqN4M0GRkZY8WkLZMb6Z-W7qbLA");
		Netflix.startParse();
//		Netflix.Parse("https://www.youtube.com/watch?v=KJCL9LR6rJI&list=PLvahqwMqN4M0GRkZY8WkLZMb6Z-W7qbLA&index=34&t=0s");
	}

}
