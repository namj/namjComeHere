package a03;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

/**
 * This class allows user to preview their title/credit pages
 * before they actually merge it with their main video.
 * 
 * @author Namjun Park npar350 Andy Choi mcho588
 *
 */

public class Previewer {
	
	private JFrame _frame;
	private JLabel _label;

	
	//constructor the previewer.
	public Previewer() {
		
		_frame = new JFrame();
		_label = new JLabel("encoding...");
		
		_frame.setBackground(Color.LIGHT_GRAY);
		_frame.setLayout(new BorderLayout());
		_frame.setLocation(600,400);
		_frame.setSize(300,150);
		_frame.add(_label,BorderLayout.CENTER);
	
		_frame.setVisible(true);
		
	}
	
	//method that performs the actuall viewing
	public void view(String _text, String _musicPath, String _imagePath, String _resolution, String _font, String _textSize, String _colour) throws InterruptedException, IOException {
	
		SwingWorker<Integer, String> worker = new SwingWorker<Integer, String>(){

			@Override
			protected Integer doInBackground() throws Exception {

				publish("Please wait...(1/3)");
				
				//command to generate video out of image
				String cmd = "avconv -loop 1 -i "+ _imagePath +" -t 00:00:10 -r 24 -s "+_resolution+" -y videoFromImage.mp4";
				ProcessBuilder Builder = new ProcessBuilder("/bin/bash","-c",cmd);
				Process process = Builder.start();
				if (process.waitFor() != 0){
					return process.waitFor();
				}
				
				publish("Please wait...(2/3)");
				
				//command to apply text and music
				String cmd2 = "avconv -i videoFromImage.mp4 -i "+ _musicPath +" -c:a copy -t 10 -vf \"drawtext=fontcolor="+_colour+":fontsize="+_textSize+":fontfile=./fonts/"+_font+":text='"+ _text +"':x=30:y=h-text_h-30\" -y preview.mp4";
				ProcessBuilder Builder2 = new ProcessBuilder("/bin/bash","-c",cmd2);
				Process process2 = Builder2.start();
				if (process2.waitFor() != 0){
					return process2.waitFor();
				}
				
				publish("Please wait...(3/3)");
				
				//command to play the video through avplay.
				String cmd3 = "avplay -i preview.mp4";
				ProcessBuilder Builder3 = new ProcessBuilder("/bin/bash","-c",cmd3);
				Process process3 = Builder3.start();
				if (process3.waitFor() != 0){
					return process3.waitFor();
				}
				
				return 0;
			}
			
			@Override
			protected void process(List<String> chunks) {
				for (int i = 0; i< chunks.size(); i++){
					_label.setText(chunks.get(i));
				}
			}
			
			@Override
			protected void done() {
				
				//display error message if processes didnt finish happliy
				try {
					if (this.get() != 0){
						JOptionPane.showMessageDialog(null, "Error playing preview.");
					}
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//delete video files when done
				try {
					Files.deleteIfExists(Paths.get("videoFromImage.mp4"));
					Files.deleteIfExists(Paths.get("preview.mp4"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				_frame.dispose();
			}
		};
		
		worker.execute();
		
	}
}
