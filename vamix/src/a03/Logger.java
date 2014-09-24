package a03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	
	public void update(String text, String musicPath, String imagePath) throws IOException {
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_vamixFolder + "/editlog.txt", false)));
		out.println(text);
		out.println(musicPath);
		out.println(imagePath);
		out.close();
		
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
				in.close();
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
				in.close();
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
				//the third line should contain the path
				String line = in.readLine();
				line = in.readLine();
				line = in.readLine();
				in.close();
				return line;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return "";
	}
	
	//method that deletes edit log file
	public void deleteLog() throws IOException{
		Files.delete(Paths.get(_vamixFolder + "/editlog.txt"));
	}
}
