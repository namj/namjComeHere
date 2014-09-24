package a03;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	private String _text; //text to apply
	private String _musicPath; //path to musicfile
	private String _imagePath; //path to image to make short video out of
	private String _videoPath; //path to main video
	private String _savePath; //path to save videos to
	
	private JFrame _frame;
	private JLabel _progressText;
	private JButton _cancelButton;
	private boolean _isCancelled = false;
	

	//constructor for this swingworker
	public TitleCreditGenerator(boolean titleOrCredit, String text, String music, String image, String path, String savePath){
		_titleOrCredit = titleOrCredit;
		_text = text;
		_musicPath = music;
		_imagePath = image;
		_videoPath = path;
		_savePath = savePath;
		
		_frame = new JFrame();
		_progressText = new JLabel("encoding...");
		_cancelButton = new JButton("Cancel");
		_cancelButton.addActionListener(this);
		
		_frame.setBackground(Color.LIGHT_GRAY);
		_frame.setLayout(new BorderLayout());
		_frame.setLocation(600,400);
		_frame.setSize(300,150);
		_frame.add(_progressText,BorderLayout.NORTH);
		_frame.add(_cancelButton,BorderLayout.SOUTH);
		_frame.setVisible(true);
		
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		
		int _indexOfBracket;
		int _counter = 0;
		
		//figure out the size of the main video to make video created from image the same size.
		String cmdX = "avconv -i "+ _videoPath +" 2>&1 | grep -i video:";
		ProcessBuilder BuilderX = new ProcessBuilder("/bin/bash","-c",cmdX);
		BuilderX.redirectErrorStream(true);
		Process processX = BuilderX.start();
		InputStream stdoutX = processX.getInputStream();
		BufferedReader stdoutY = new BufferedReader(new InputStreamReader(stdoutX));
		String lineX = null;
		//print output from terminal to console
		while ((lineX = stdoutY.readLine()) != null) {
			System.out.println(lineX);
			publish("Obtaining video resolution...");
			_indexOfBracket = lineX.indexOf('[');
			int i = _indexOfBracket -2 ;
			while (Character.isDigit(lineX.charAt(i)) || lineX.charAt(i) == 'x') {
				_counter = _counter + 1;
				i--;
			}
			//extract only the part which contains information about resolution.
			String resolution = lineX.substring(_indexOfBracket - _counter - 2, _indexOfBracket -2);
			System.out.println(resolution);;
			
			//if cancel button has been pressed
			if (_isCancelled){
				//destroy process and return exit value
				processX.destroy();
				int exitValue = processX.waitFor();
				return exitValue;
			}
		}
		
		if (processX.waitFor() != 0){
			return processX.waitFor();
		}
		
		//terminal command to build 10sec video from selected image
		String cmd = "avconv -loop 1 -i "+ _imagePath +" -t 00:00:10 -r 24 -s 1920x1080 -y "+ _savePath +"/videoFromImage.mp4";
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
			if (_isCancelled){
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
		cmd = "avconv -i "+ _savePath +"/videoFromImage.mp4 -i "+ _musicPath +" -c:a copy -t 10 -vf \"drawtext=fontcolor=white:fontsize=30:fontfile=/usr/share/fonts/truetype/ubuntu-font-family/Ubuntu-L.ttf:text='"+ _text +"':x=30:y=h-text_h-30\" -y "+ _savePath +"/titleCreditPage.mp4";
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
			if (_isCancelled){
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
			cmd = "avconv -i "+ _savePath +"/titleCreditPage.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "+ _savePath +"/file1.ts";
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
				if (_isCancelled){
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
			cmd = "avconv -i "+ _videoPath +" -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "+ _savePath +"/file2.ts";
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
				if (_isCancelled){
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
			cmd = "avconv -i concat:"+ _savePath +"/file1.ts\\|"+ _savePath +"/file2.ts -c copy -bsf:a aac_adtstoasc -y "+ _savePath +"/Out.mp4";
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
				if (_isCancelled){
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
			cmd = "avconv -i "+ _savePath +"/titleCreditPage.mp4 -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "+ _savePath +"/file3.ts";
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
				if (_isCancelled){
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
			cmd = "avconv -i "+ _videoPath +" -vcodec libx264 -acodec aac -bsf:v h264_mp4toannexb -f mpegts -strict experimental -y "+ _savePath +"/file2.ts";
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
				if (_isCancelled){
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
			cmd = "avconv -i concat:"+ _savePath +"/file2.ts\\|"+ _savePath +"/file3.ts -c copy -bsf:a aac_adtstoasc -y "+ _savePath +"/Out.mp4";
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
				if (_isCancelled){
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
			//if background work finished peacefully
			if (this.get() == 0) {
				JOptionPane.showMessageDialog(null,"Done!");
				//if user decides to save session
				if (JOptionPane.showConfirmDialog(null, "would you like to save your last(this) session?") == JOptionPane.OK_OPTION){
					try {
						Logger.getInstance().update(_text, _musicPath, _imagePath);
						JOptionPane.showMessageDialog(null,"Saved");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
			} else {
				JOptionPane.showMessageDialog(null,"Error!");
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//close progress frame;
		_frame.dispose();
		
	}
	
	@Override
	protected void process(List<String> chunks) {
		//update progress frame/text
		for (int i = 0 ; i < chunks.size() ; i ++ )
		_progressText.setText(chunks.get(i));
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _cancelButton){
			_isCancelled = true;
			_frame.dispose();
		}
		
	}
	
}
