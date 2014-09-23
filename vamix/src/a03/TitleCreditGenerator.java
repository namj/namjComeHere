package a03;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
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
		
		//terminal command to build 10sec video from selected image
		String cmd = "avconv -loop 1 -i "+ _imagePath +" -t 00:00:10 -r 24 -s 1920x1080 -y videoFromImage.mp4";
		ProcessBuilder Builder = new ProcessBuilder("/bin/bash","-c",cmd);
		Builder.redirectErrorStream(true);
		Process process = Builder.start();
		InputStream stdout = process.getInputStream();
		BufferedReader stdoutB = new BufferedReader(new InputStreamReader(stdout));
		String line = null;
		while ((line = stdoutB.readLine()) != null) {
			System.out.println(line);
		}
		
		//terminal command to add 10sec and music to the 10sec video
		cmd = "avconv -i videoFromImage.mp4 -i "+ _musicPath +" -c:a copy -t 10 -vf \"drawtext=fontcolor=white:fontsize=30:fontfile=/usr/share/fonts/truetype/ubuntu-font-family/Ubuntu-L.ttf:text='"+ _text +"':x=30:y=h-text_h-30\" -y titleCreditPage.mp4";
		ProcessBuilder Builder2 = new ProcessBuilder("/bin/bash","-c",cmd);
		Builder2.redirectErrorStream(true);
		Process process2 = Builder2.start();
		InputStream stdoutC = process2.getInputStream();
		BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
		line = null;
		while ((line = stdoutD.readLine()) != null) {
			System.out.println(line);
		}
		
		if (_titleOrCredit == true) {
			
			//terminal command to turn titlepage(openingscene) into .ts 
			cmd = "avconv -i titleCreditPage.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file1.ts";
			ProcessBuilder Builder3 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder3.redirectErrorStream(true);
			Process process3 = Builder3.start();
			InputStream stdoutE = process3.getInputStream();
			BufferedReader stdoutF = new BufferedReader(new InputStreamReader(stdoutE));
			line = null;
			while ((line = stdoutF.readLine()) != null) {
				System.out.println(line);
			}
			/**
			//terminal command to turn main vid into .ts
			cmd = "avconv -i "+ _videoPath +" -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file2.ts";
			ProcessBuilder Builder4 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder4.redirectErrorStream(true);
			Process process4 = Builder4.start();
			InputStream stdoutG = process4.getInputStream();
			BufferedReader stdoutH = new BufferedReader(new InputStreamReader(stdoutG));
			line = null;
			while ((line = stdoutH.readLine()) != null) {
				System.out.println(line);
			}*/
			
			//terminal command to concat the 2 .ts files and turn into mp4.
			cmd = "avconv -i concat:file1.ts\\|file2.ts -c copy -bsf:a aac_adtstoasc -y Out.mp4";
			ProcessBuilder Builder5 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder5.redirectErrorStream(true);
			Process process5 = Builder5.start();
			InputStream stdoutI = process5.getInputStream();
			BufferedReader stdoutJ = new BufferedReader(new InputStreamReader(stdoutI));
			line = null;
			while ((line = stdoutJ.readLine()) != null) {
				System.out.println(line);
			}
			
		} else {
			
			//terminal command to turn titlepage(openingscene) into .ts 
			cmd = "avconv -i titleCreditPage.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file3.ts";
			ProcessBuilder Builder3 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder3.redirectErrorStream(true);
			Process process3 = Builder3.start();
			InputStream stdoutE = process3.getInputStream();
			BufferedReader stdoutF = new BufferedReader(new InputStreamReader(stdoutE));
			line = null;
			while ((line = stdoutF.readLine()) != null) {
				System.out.println(line);
			}
			/**
			//terminal command to turn main vid into .ts
			cmd = "avconv -i "+ _videoPath +" -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file2.ts";
			ProcessBuilder Builder4 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder4.redirectErrorStream(true);
			Process process4 = Builder4.start();
			InputStream stdoutG = process4.getInputStream();
			BufferedReader stdoutH = new BufferedReader(new InputStreamReader(stdoutG));
			line = null;
			while ((line = stdoutH.readLine()) != null) {
				System.out.println(line);
			}*/
			
			//terminal command to concat the 2 .ts files and turn into mp4.
			cmd = "avconv -i concat:file2.ts\\|file3.ts -c copy -bsf:a aac_adtstoasc -y Out.mp4";
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
		return 0;
	}
	
	@Override
	protected void done() {
		
		try {
			if (this.get() == 0) {
				JOptionPane.showMessageDialog(null,"Title page(s) generation successful!");
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
