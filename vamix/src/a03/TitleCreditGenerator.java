package a03;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class TitleCreditGenerator extends SwingWorker<Integer, String> implements ActionListener {
	
	private boolean _titleOrCredit; // true indicates title, false indicates credit
	private String _text;
	private String _musicPath;
	private String _imagePath;
	private String _videoPath;
	private JFrame _frame;
	private JLabel _progressText;
	private JButton _cancelButton;
	

	public TitleCreditGenerator(boolean titleOrCredit, String text, String music, String image, String path){
		_titleOrCredit = titleOrCredit;
		_text = text;
		_musicPath = music;
		_imagePath = image;
		_videoPath = path;
		
		_frame = new JFrame();
		_progressText = new JLabel("encoding...");
		_cancelButton = new JButton("Cancel");
		_cancelButton.addActionListener(this);
		
		_frame.setBackground(Color.LIGHT_GRAY);
		_frame.setSize(300,150);
		_frame.add(_progressText);
		_frame.add(_cancelButton);
		_frame.setVisible(true);
		
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
		//print output from terminal to console
		while ((line = stdoutB.readLine()) != null) {
			System.out.println(line);
			publish("Creating video from image...");
			//if cancel button has been pressed
			if (isCancelled()){
				//destroy process and return exit value
				process.destroy();
				int exitValue = process.waitFor();
				return exitValue;
			}
		}
		//if process hasn't finished happily, return exit value which will be non zero
		if (process.waitFor() != 0){
			return process.waitFor();
		}
		
		//terminal command to add 10sec and music to the 10sec video
		cmd = "avconv -i videoFromImage.mp4 -i "+ _musicPath +" -c:a copy -t 10 -vf \"drawtext=fontcolor=white:fontsize=30:fontfile=/usr/share/fonts/truetype/ubuntu-font-family/Ubuntu-L.ttf:text='"+ _text +"':x=30:y=h-text_h-30\" -y titleCreditPage.mp4";
		ProcessBuilder Builder2 = new ProcessBuilder("/bin/bash","-c",cmd);
		Builder2.redirectErrorStream(true);
		Process process2 = Builder2.start();
		InputStream stdoutC = process2.getInputStream();
		BufferedReader stdoutD = new BufferedReader(new InputStreamReader(stdoutC));
		line = null;
		//print output from terminal to console
		while ((line = stdoutD.readLine()) != null) {
			System.out.println(line);
			publish("adding music and text to video...");
			//if cancel button has been pressed
			if (isCancelled()){
				//destroy process and return exit value
				process2.destroy();
				int exitValue = process2.waitFor();
				return exitValue;
			}
		}
		//if process hasn't finished happily, return exit value which will be non zero
		if (process2.waitFor() != 0){
			return process2.waitFor();
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
			//print output from terminal to console
			while ((line = stdoutF.readLine()) != null) {
				System.out.println(line);
				publish("encoding title page(s)...");
				//if cancel button has been pressed
				if (isCancelled()){
					//destroy process and return exit value
					process3.destroy();
					int exitValue = process3.waitFor();
					return exitValue;
				}
			}
			//if process hasn't finished happily, return exit value which will be non zero
			if (process3.waitFor() != 0){
				return process3.waitFor();
			}
			
			//terminal command to turn main vid into .ts
			cmd = "avconv -i "+ _videoPath +" -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file2.ts";
			ProcessBuilder Builder4 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder4.redirectErrorStream(true);
			Process process4 = Builder4.start();
			InputStream stdoutG = process4.getInputStream();
			BufferedReader stdoutH = new BufferedReader(new InputStreamReader(stdoutG));
			line = null;
			//print output from terminal to console
			while ((line = stdoutH.readLine()) != null) {
				System.out.println(line);
				publish("encoding main video... \nThis process may take a few minutes");
				//if cancel button has been pressed
				if (isCancelled()){
					//destroy process and return exit value
					process4.destroy();
					int exitValue = process4.waitFor();
					return exitValue;
				}
			}
			//if process hasn't finished happily, return exit value which will be non zero
			if (process4.waitFor() != 0){
				return process4.waitFor();
			}
			
			//terminal command to concat the 2 .ts files and turn into mp4.
			cmd = "avconv -i concat:file1.ts\\|file2.ts -c copy -bsf:a aac_adtstoasc -y Out.mp4";
			ProcessBuilder Builder5 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder5.redirectErrorStream(true);
			Process process5 = Builder5.start();
			InputStream stdoutI = process5.getInputStream();
			BufferedReader stdoutJ = new BufferedReader(new InputStreamReader(stdoutI));
			line = null;
			//print output from terminal to console
			while ((line = stdoutJ.readLine()) != null) {
				System.out.println(line);
				publish("adding title page to video...");
				//if cancel button has been pressed
				if (isCancelled()){
					//destroy process and return exit value
					process5.destroy();
					int exitValue = process5.waitFor();
					return exitValue;
				}
			}
			//if process hasn't finished happily, return exit value which will be non zero
			if (process5.waitFor() != 0){
				return process5.waitFor();
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
			//print output from terminal to console
			while ((line = stdoutF.readLine()) != null) {
				System.out.println(line);
				publish("encoding credit page(s)...");
				//if cancel button has been pressed
				if (isCancelled()){
					//destroy process and return exit value
					process3.destroy();
					int exitValue = process3.waitFor();
					return exitValue;
				}
			}
			//if process hasn't finished happily, return exit value which will be non zero
			if (process3.waitFor() != 0){
				return process3.waitFor();
			}
			
			//terminal command to turn main vid into .ts
			cmd = "avconv -i "+ _videoPath +" -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y file2.ts";
			ProcessBuilder Builder4 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder4.redirectErrorStream(true);
			Process process4 = Builder4.start();
			InputStream stdoutG = process4.getInputStream();
			BufferedReader stdoutH = new BufferedReader(new InputStreamReader(stdoutG));
			line = null;
			//print output from terminal to console
			while ((line = stdoutH.readLine()) != null) {
				System.out.println(line);
				publish("encoding main video... This process may take a few minutes");
				//if cancel button has been pressed
				if (isCancelled()){
					//destroy process and return exit value
					process4.destroy();
					int exitValue = process4.waitFor();
					return exitValue;
				}
			}
			//if process hasn't finished happily, return exit value which will be non zero
			if (process4.waitFor() != 0){
				return process4.waitFor();
			}
			
			//terminal command to concat the 2 .ts files and turn into mp4.
			cmd = "avconv -i concat:file2.ts\\|file3.ts -c copy -bsf:a aac_adtstoasc -y Out.mp4";
			ProcessBuilder Builder5 = new ProcessBuilder("/bin/bash","-c",cmd);
			Builder5.redirectErrorStream(true);
			Process process5 = Builder5.start();
			InputStream stdoutI = process5.getInputStream();
			BufferedReader stdoutJ = new BufferedReader(new InputStreamReader(stdoutI));
			line = null;
			//print output from terminal to console
			while ((line = stdoutJ.readLine()) != null) {
				System.out.println(line);
				publish("adding credit page to video...");
				//if cancel button has been pressed
				if (isCancelled()){
					//destroy process and return exit value
					process5.destroy();
					int exitValue = process5.waitFor();
					return exitValue;
				}
			}
			//if process hasn't finished happily, return exit value which will be non zero
			if (process5.waitFor() != 0){
				return process5.waitFor();
			}
			
		}
		return 0;
	}
	
	@Override
	protected void done() {
		
		try {
			if (this.get() == 0) {
				JOptionPane.showMessageDialog(null,"Done!");
			} else {
				JOptionPane.showMessageDialog(null,"Error!");
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void process(List<String> chunks) {
		
		for (int i = 0 ; i < chunks.size() ; i ++ )
		_progressText.setText(chunks.get(i));
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _cancelButton){
			this.cancel(true);
		}
		
	}
	
}
