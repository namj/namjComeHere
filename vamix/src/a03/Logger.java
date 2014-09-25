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

import javax.swing.JTextArea;

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
	
	public void update(String text, String musicPath, String imagePath, int fontCombo, int sizeCombo, int colourCombo) throws IOException {
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_vamixFolder + "/editlog.txt", false)));
		out.println(musicPath);
		out.println(imagePath);
		out.println(fontCombo);
		out.println(sizeCombo);
		out.println(colourCombo);
		out.close();
		
		PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter(_vamixFolder + "/textlog.txt", false)));
		out2.println(text);
		out2.close();
		
	}
	
	//this method should return the text, ie first line of editlog file
	public void pullText(JTextArea text){
		
		if (new File(_vamixFolder + "/textlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/textlog.txt"));
				text.setText("");
				String line = in.readLine();
				while(line != null){
				  text.append(line + "\n");
				  line = in.readLine();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
	}
	
	//this method should return the path of music file last used. ie second line of edit log file
	public String pullMusicPath(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/editlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
				//should be first line;
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
	
	//this method should return the path of image file last used. ie third line of edit log file
	public String pullImagePath(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/editlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
				//the secondline should contain path to image
				in.readLine();
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
	
	public int pullFontIndex(){
		
		//check if log file exists
		if (new File(_vamixFolder + "/editlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
				//should be in 3rdline
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}

	public int pullSizeIndex(){
	
		//check if log file exists
		if (new File(_vamixFolder + "/editlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
				//should be in 4th line
				in.readLine();
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}

	public int pullColourIndex(){
	
		//check if log file exists
		if (new File(_vamixFolder + "/editlog.txt").exists()){
			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(_vamixFolder + "/editlog.txt"));
				//should be in 5th line
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				String line = in.readLine();
				in.close();
				return Integer.parseInt(line);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} 
		return 0;
	}
	
	//method that deletes edit log file
	public void deleteLog() throws IOException{
		Files.deleteIfExists(Paths.get(_vamixFolder + "/editlog.txt"));
		Files.deleteIfExists(Paths.get(_vamixFolder + "/textlog.txt"));
	}
	
}
