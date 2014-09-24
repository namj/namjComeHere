package a03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	
	//declare/initialise components
	private File _homedir = new File(System.getProperty("user.home"));
	private File _vamixFolder = new File(_homedir, "/.vamix");
	
	//constructor
	private Logger() { 
		//if vamix folder doesnt exist, make one
		if (!_vamixFolder.exists()){
			_vamixFolder.mkdir();
		}
		
	}
	private static Logger instance = new Logger();
	//return singleton object of this panel
	public static Logger getInstance() {
		return instance;
	}
	
	public void update(String text, String musicPath, String imagePath) throws FileNotFoundException {
		
		PrintWriter writer = new PrintWriter(new File(_vamixFolder + "/editlog.txt"));
		writer.print(text);
		writer.print(musicPath);
		writer.print(imagePath);
		
	}
	
	//this method should return the text, ie first line of editlog file
	public String pullText(){
		
		//check if edit log even exists
		if (new File(_vamixFolder + "/editlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
				//the first line should contain the text
				String line = in.readLine();
				return line;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return "";
	}
	
	//this method should return the path of music file last used. ie second line of edit log file
	public String pullMusicPath(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/editlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
				//the second line should contain the path
				String line = in.readLine();
				line = in.readLine();
				return line;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return "";
	}
	
	//this method should return the path of image file last used. ie third line of edit log file
		public String pullImagePath(){
			//check if log file exists
			if (new File(_vamixFolder + "/editlog.txt").exists()){
				BufferedReader in;
				try {
					in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
					//the second line should contain the path
					String line = in.readLine();
					line = in.readLine();
					line = in.readLine();
					return line;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} 
			return "";
		}

}
