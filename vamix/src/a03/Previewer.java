package a03;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Previewer {
	
	JFrame _frame;
	JLabel _label;
	String cmd;
	ProcessBuilder Builder;
	Process process;
	
	public Previewer() {
		/**
		_frame = new JFrame();
		_frame.setBackground(Color.LIGHT_GRAY);
		_frame.setLayout(new BorderLayout());
		_frame.setLocation(600,400);
		_frame.setSize(300,150);
		_frame.add(_label = new JLabel("Please wait..."),BorderLayout.CENTER);
		_frame.setVisible(true);
		*/
		_frame = new JFrame();
		_label = new JLabel("encoding...");
		
		_frame.setBackground(Color.LIGHT_GRAY);
		_frame.setLayout(new BorderLayout());
		_frame.setLocation(600,400);
		_frame.setSize(300,150);
		_frame.add(_label,BorderLayout.NORTH);
		_frame.setVisible(true);
	}
	
	public void view(String _text, String _musicPath, String _imagePath, String _resolution, String _font, String _textSize, String _colour) throws InterruptedException, IOException {
	
		
		cmd = "avconv -loop 1 -i "+ _imagePath +" -t 00:00:50 -r 24 -s "+_resolution+" -y videoFromImage.mp4";
		Builder = new ProcessBuilder("/bin/bash","-c",cmd);
		process = Builder.start();
		process.waitFor();
		
		cmd = "avconv -i videoFromImage.mp4 -i "+ _musicPath +" -c:a copy -t 10 -vf \"drawtext=fontcolor="+_colour+":fontsize="+_textSize+":fontfile=./fonts/"+_font+":text='"+ _text +"':x=30:y=h-text_h-30\" -y titleCreditPage.mp4";
		Builder = new ProcessBuilder("/bin/bash","-c",cmd);
		process = Builder.start();
		//if process hasn't finished happily, return exit value which will be non zero
		process.waitFor();
		
		_frame.dispose();
	
		cmd = "avplay -i titleCreditPage.mp4";
		Builder = new ProcessBuilder("/bin/bash","-c",cmd);
		process = Builder.start();
		
		Files.deleteIfExists(Paths.get("videoFromImage.mp4"));
		Files.deleteIfExists(Paths.get("titleCreditPage.mp4"));
	}
}
