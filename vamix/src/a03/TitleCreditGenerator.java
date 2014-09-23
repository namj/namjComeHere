package a03;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.SwingWorker;

public class TitleCreditGenerator extends SwingWorker<Integer, Integer> {
	
	private boolean _titleOrCredit; // true indicates title, false indicates credit
	private String _text;
	private String _musicPath;
	private String _imagePath;
	private String _videoPath;
	

	public TitleCreditGenerator(boolean titleOrCredit, String text, String music, String image, String path){
		_titleOrCredit = titleOrCredit;
		_text = text;
		_musicPath = music;
		_imagePath = image;
		_videoPath = path;
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		// TODO Auto-generated method stub
		if (_titleOrCredit == true) {
			
			String cmd = "avconv -loop 1 -i "+ _imagePath +" -t 00:00:10 -r 24 -s 1920x1080 videoFromImage.mp4";
			ProcessBuilder Builder = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder.redirectErrorStream(true);
			Process process = Builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutB = new BufferedReader(new InputStreamReader(stdout));
			String line = null;
			while ((line = stdoutB.readLine()) != null) {
				System.out.println(line);
			}
			
			cmd = "avconv -i videoFromImage.mp4 -i "+ _musicPath +" -c:a copy -t 10 -vf \"drawtext=fontcolor=white:fontsize=30:fontfile=/usr/share/fonts/truetype/ubuntu-font-family/Ubuntu-L.ttf:text='"+ _text +"':x=30:y=h-text_h-30\" titlePage.mp4";
			ProcessBuilder Builder2 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder2.redirectErrorStream(true);
			Process process2 = Builder2.start();
			InputStream stdoutC = process2.getInputStream();
			BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
			line = null;
			while ((line = stdoutD.readLine()) != null) {
				System.out.println(line);
			}
			
			cmd = "avconv -i titlePage.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file1.ts";
			ProcessBuilder Builder3 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder3.redirectErrorStream(true);
			Process process3 = Builder3.start();
			InputStream stdoutE = process3.getInputStream();
			BufferedReader stdoutF = new BufferedReader(new InputStreamReader(stdoutE));
			line = null;
			while ((line = stdoutF.readLine()) != null) {
				System.out.println(line);
			}
			
			cmd = "avconv -i "+ _videoPath +" -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file2.ts";
			ProcessBuilder Builder4 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder4.redirectErrorStream(true);
			Process process4 = Builder4.start();
			InputStream stdoutG = process4.getInputStream();
			BufferedReader stdoutH = new BufferedReader(new InputStreamReader(stdoutG));
			line = null;
			while ((line = stdoutH.readLine()) != null) {
				System.out.println(line);
			}
			
			cmd = "avconv -i concat:file1.ts|file2.ts -c copy -bsf:a aac_adtstoasc -y Out.mp4";
			ProcessBuilder Builder5 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder5.redirectErrorStream(true);
			Process process5 = Builder5.start();
			InputStream stdoutI = process5.getInputStream();
			BufferedReader stdoutJ = new BufferedReader(new InputStreamReader(stdoutI));
			line = null;
			while ((line = stdoutJ.readLine()) != null) {
				System.out.println(line);
			}
			
			
		}
		
		return null;
	}


	
}
