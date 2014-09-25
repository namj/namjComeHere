package a03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Previewer {
	
	public void view(String _imagePath, String _resolution) throws IOException{
	
		String cmd = "avconv -loop 1 -i "+ _imagePath +" -t 00:00:10 -r 24 -s "+_resolution+" -y videoFromImage.mp4";
		ProcessBuilder Builder = new ProcessBuilder("/bin/bash","-c",cmd);
		Builder.redirectErrorStream(true);
		Process process = Builder.start();
		
		String cmd2 = "avplay -i /home//namjun/Desktop/206/assign3//namjComeHere/vamix/videoFromImage.mp4";
		ProcessBuilder Builder2 = new ProcessBuilder("/bin/bash","-c",cmd2);
		Builder2.redirectErrorStream(true);
		Process process2 = Builder2.start();
		
		//Files.deleteIfExists(Paths.get("videoFromImage.mp4"));
	}
}
