package a03;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class Downloader extends SwingWorker<Integer,String[]> {
	//download frame the downloading swing worker is working with
	private DownloadFrame currentDlFrame;
	//boolean to notify if download is cancelled
	private boolean isCancelled;
	//boolean to notify if download is paused
	private boolean isPaused;
	//boolean to know whether downloading or not
	private boolean isRun;
<<<<<<< HEAD
=======
	//save directory + file name
	private String savePath;
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
	
	public Downloader(DownloadFrame d) {
		currentDlFrame = d;
		isCancelled = false;
		isPaused = false;
		isRun = true;
	}
	
	
	@Override
	protected Integer doInBackground() throws Exception {
<<<<<<< HEAD
		String cmd = "wget -c --progress=dot " + currentDlFrame.getURL();
		ProcessBuilder downloadBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
		downloadBuilder.redirectErrorStream(true);
		Process download = downloadBuilder.start();
		InputStream stdout = download.getInputStream();
		BufferedReader stdoutB = new BufferedReader(new InputStreamReader(stdout));
		String line = null;
		while ((line = stdoutB.readLine()) != null) {
			if (line.contains("wget: unable to resolve host address")) {
				return 666;
			}
			if (isPaused) {
				download.destroy();
=======
		//process builder and bash command to download a file from a URL, and its process
		String cmd = "wget -c --progress=dot " + currentDlFrame.getURL() + " -O " + savePath + ".mp3";
		ProcessBuilder downloadBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
		downloadBuilder.redirectErrorStream(true);
		Process download = downloadBuilder.start();
		//stdout of process is read as input
		InputStream stdout = download.getInputStream();
		BufferedReader stdoutB = new BufferedReader(new InputStreamReader(stdout));
		String line = null;
		//read through each line of the redirected error stream
		while ((line = stdoutB.readLine()) != null) {
			//an error code for non media files
			if (line.contains("wget: unable to resolve host address")) {
				return 666;
			}
			//destroy process when 'paused or cancelled'
			if (isPaused) {
				download.destroy();
				//return for a normal pause command
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
				return 667;
			}
			if(isCancelled) {
				download.destroy();
<<<<<<< HEAD
				break;
			}
=======
				//loops is broken when cancelled
				break;
			}
			//waits for the line to contain 'Length:' which allows the retrieval of the 
			//file size of media download
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
			if (line.contains("Length:")) {
				int sB1 = line.indexOf('[');
				int sB2 = line.indexOf(']');
				String fileType = line.substring(sB1+1, sB2);
				if (fileType.contains("video") || fileType.contains("audio")) {
					int rB1 = line.indexOf('(');
					int rB2 = line.indexOf(')');
					String size = line.substring(rB1+1, rB2);
					currentDlFrame.setFileSize(size);
				} else {
					return 666;
				}
			}
<<<<<<< HEAD
=======
			//for when download starts, obtain data and update by publish()
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
			if (line.contains("..........")) {
				int percent = line.indexOf('%');
				String number = line.substring(percent-3, percent).replaceAll("[^0-9]", "");
				int m = line.indexOf('M');
				String speed, time;
				if (m == -1) {
					String shorter = line.substring(percent);
					int k = shorter.indexOf('K');
					speed = shorter.substring(k-3, k+1);
					time = shorter.substring(k+2);
				} else {
					speed = line.substring(m-4, m+1);
					time = line.substring(m+2);
				}
				String[] info = new String[3];
				info[0] = number;
				info[1] = speed;
				info[2] = time;
				publish(info);
			}
		}
<<<<<<< HEAD
=======
		//obtain exit value, via get()
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
		int exitValue = download.waitFor();
		return exitValue;
	}
	
	@Override
	protected void done() {
		//appropriate pop up for different exit statuses
		try {
			if (this.get() == 0){
				JOptionPane.showMessageDialog(null,"Download Successful!");
			}else if (this.get() == 1){
				JOptionPane.showMessageDialog(null,"Error Encountered\n(error: generic error code)");
			}else if (this.get() == 2){
				JOptionPane.showMessageDialog(null,"Error Encountered\n(error: parse error)");
			}else if (this.get() == 3){
				JOptionPane.showMessageDialog(null,"Error Encountered\n(error: File I/0 error)");
			}else if (this.get() == 4){
				JOptionPane.showMessageDialog(null,"Error Encountered\n(error: network failure)");
			}else if (this.get() == 5){
				JOptionPane.showMessageDialog(null,"Error Encountered\n(error: SSL verification failure)");
			}else if (this.get() == 6){
				JOptionPane.showMessageDialog(null,"Error Encountered\n(error: generic error code)");
			}else if (this.get() == 7){
				JOptionPane.showMessageDialog(null,"Error Encountered\n(error: Username/password authentication failure)");
			}else if (this.get() == 8){
				JOptionPane.showMessageDialog(null,"Error Encountered\n(error: Protocol errors)");
			}else if (this.get() == 666) {
				JOptionPane.showMessageDialog(null,"Error Encountered\n(error: non-media file)");
			}else if (this.get() == 143) {
				JOptionPane.showMessageDialog(null, "Download has been cancelled!");
			}else if (this.get() == 667) {
				//paused hence no error in this
			}else{
				JOptionPane.showMessageDialog(null,"Error Encountered\n(error: unknown");
			}
		} catch (HeadlessException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		//after download frame is closed
		if (isCancelled && (isPaused == false)) {
			currentDlFrame.exit();
		} else if (isPaused){
			//if paused, frame is not exited, instead pause button becomes resume button
		} else {
			currentDlFrame.exit();
		}
		isRun = false;
	}

	//update of download progress to the download frame
	@Override
	protected void process(List<String[]> list) {
		String[] info = list.get(list.size()-1);
		String number = info[0];
		String speed = info[1];
		String time = info[2];
		currentDlFrame.updateDlInfo(number, speed, time);
	}
	
<<<<<<< HEAD
=======
	//method to set the save directory/file name 
	public void setSave(String save) {
		savePath = save;
	}
	
	//method to pause* operation
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
	public void pause() {
		isPaused = true;
	}
	
	//method to stop operation
	public void stop() {
		isCancelled = true;
	}
	
<<<<<<< HEAD
=======
	//method to check if downloader is still running
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
	public boolean isRunning() {
		return isRun;
	}
}
