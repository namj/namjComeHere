package a03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
<<<<<<< HEAD
=======
import javax.swing.JFileChooser;
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class AudioProcessor extends SwingWorker<Integer,Void>{
	private enum fileTypes {
		mpg, avi, mp4, rv, divx, wmv, mov
	}

	private boolean isCancelled = false;
	private boolean hasExtracted = false;
	ProcessBuilder audioProcessBuilder;
	Process audioProcess;
	
	//Get user's screen dimension
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	
	private String process;
	private File mediaFile;
	private String fileType;
	private String saveDir;
	private String audioPath;
	private long overlayDur;
	
	private JFrame processFrame;
	private JLabel progressLabel;
	private JButton okButton = new JButton("Okay");
	private JButton cancelButton = new JButton("Cancel");
	
	public AudioProcessor(String process, File file) {
<<<<<<< HEAD
		this.process = process;
		this.mediaFile = file;
=======
		//string determining which audio process 
		this.process = process;
		this.mediaFile = file;
		//matching the file with different file types to get an appropriate output file type
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
		for (fileTypes fT : fileTypes.values()) {
			if (file.getName().contains("." + fT)) {
				fileType = "." + fT;
				break;
			} else {
				fileType = ".mp4";
			}
		}
<<<<<<< HEAD
		//Button setup for the frames
		cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
=======
		//Button setup for the progress frame
		cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//cancel button destroys the process
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
				audioProcess.destroy();
				isCancelled = true;
			}
		});
		okButton.setEnabled(false);
		okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
<<<<<<< HEAD
=======
				//when okay is enabled and pressed disposes the frame
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
				processFrame.dispose();
			}
		});
		progressLabel = new JLabel();
		
<<<<<<< HEAD
=======
		//for appropriate audio process set up frame and label
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
		if (process.equals("ex") || process.equals("rm&ex")) {
			processFrame = new JFrame("Extracting audio...");
			progressLabel.setText("Please wait until audio is extracted.");
		} else if (process.equals("rm")) {
			processFrame = new JFrame("Removing audio...");
			progressLabel.setText("Please wait until audio is removed.");
		} else if (process.equals("ov")) {
			processFrame = new JFrame("Overlaying audio...");
			progressLabel.setText("Please wait until audio is overlayed.");
		} else {
			processFrame = new JFrame("Replacing audio...");
			progressLabel.setText("Please wait until audio is replaced.");
		}
<<<<<<< HEAD
=======
		//set up of frame and its components
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
		processFrame.setLayout(null);
		processFrame.setBounds((_screenWidth-400)/2, (_screenHeight-140)/2, 400, 140);
		
		progressLabel.setBounds(30,10,350,40);
		processFrame.add(progressLabel);
		
		cancelButton.setBounds(processFrame.getWidth()-105,60,100,35);
		okButton.setBounds(cancelButton.getX()-105,60,100,35);
		processFrame.add(cancelButton);
		processFrame.add(okButton);
		
		processFrame.setVisible(true);
	}
	
<<<<<<< HEAD
=======
	//method to set up the process save file
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
	public void setSaveDir(String saveDir) {
		this.saveDir = saveDir;
	}
	
<<<<<<< HEAD
=======
	//method to retrieve music file path and media length time for overlay/ replace process
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
	public void setForOverlay(String musicPath, long time) {
		audioPath = musicPath;
		overlayDur = time;
	}
	
<<<<<<< HEAD
	@Override
	protected Integer doInBackground() throws Exception {
		String cmd;
=======
	//process of bash command for appropriate processes, with a return of 666 for any error occurrence
	@Override
	protected Integer doInBackground() throws Exception {
		String cmd;
		//for any extract process copy the audio codec without the video and convert to mp3
		//format and save to the appropriate directory/file
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
		if (process.equals("ex") || process.equals("rm&ex")) {
			cmd = "avconv -i " + mediaFile.getAbsolutePath() + " -vn -c:a libmp3lame"
					+ " -q:a 2 " + saveDir +".mp3";
			audioProcessBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
			audioProcess = audioProcessBuilder.start();
			int exit = audioProcess.waitFor();
			if (process.equals("rm&ex")) {
				if (exit == 0) {
<<<<<<< HEAD
=======
					//a call to publish made for media file undergoing remove as well as extract
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
					publish();
				} else {
					return 666;
				}
			} else {
				if (exit == 0) {
					return 0;
				} else {
					return 666;
				}
			}
		}
<<<<<<< HEAD
		if (process.equals("rm") || process.equals("rm&ex")) {
			cmd = "avconv -i " + mediaFile.getAbsolutePath() + " -c:v copy -an AudioRM" + fileType;
=======
		//for any remove process copy the video codec without audio signal and save appropriately
		if (process.equals("rm") || process.equals("rm&ex")) {
			if (process.equals("rm&ex")) {
				//file chooser to obtain save directory/file 
				JFileChooser saveChooser = new JFileChooser(mediaFile);
				saveChooser.setDialogTitle("Select directory, enter file name to save as...");
				int response = saveChooser.showSaveDialog(null);
				if (response == JFileChooser.APPROVE_OPTION) {
					saveDir = saveChooser.getSelectedFile().getAbsolutePath();
				} else {
					return 667;
				}
			}
			cmd = "avconv -i " + mediaFile.getAbsolutePath() + " -c:v copy -an " + saveDir + fileType;
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
			audioProcessBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
			audioProcess = audioProcessBuilder.start();
			int exit = audioProcess.waitFor();
			if (exit == 0) {
				return 0;
			} else {
				return 666;
			}
		}
<<<<<<< HEAD
		if (process.equals("ov")) {
			cmd = "avconv -i " + mediaFile.getAbsolutePath() + " -i " + audioPath + 
					" -filter_complex [0:a][1:a]amix[out] -map \"[out]\" -map 0:v -c:v copy"
					+ " -t " + overlayDur + " -strict experimental overlay" + fileType;
=======
		//for any overlay process, map the audio from the two input files together to a single
		//audio signal then map the video from input 0 and map the mixed audio
		//copy the video codec and output the file appropriately for the video's duration
		if (process.equals("ov")) {
			cmd = "avconv -i " + mediaFile.getAbsolutePath() + " -i " + audioPath + 
					" -filter_complex [0:a][1:a]amix[out] -map \"[out]\" -map 0:v -c:v copy"
					+ " -t " + overlayDur + " -strict experimental " + saveDir + fileType;
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
			audioProcessBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
			audioProcess = audioProcessBuilder.start();
			int exit = audioProcess.waitFor();
			if (exit == 0) {
				return 0;
			} else {
				return 666;
			}
		}
<<<<<<< HEAD
		if (process.equals("rp")) {
			cmd = "avconv -i " + mediaFile.getAbsolutePath() + " -i " + audioPath +
					" -map 1:a -map 0:v -c:v copy -t " + overlayDur + " -strict experimental " +
					"replaced" + fileType;
=======
		//for any replace process, map the video for input 0, and map the audio for input 1 and 
		//output the file appropriately for the video's duration
		if (process.equals("rp")) {
			cmd = "avconv -i " + mediaFile.getAbsolutePath() + " -i " + audioPath +
					" -map 1:a -map 0:v -c:v copy -t " + overlayDur + " -strict experimental " +
					saveDir + fileType;
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
			audioProcessBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
			audioProcess = audioProcessBuilder.start();
			int exit = audioProcess.waitFor();
			if (exit == 0) {
				return 0;
			} else {
				return 666;
			}
		}
		return null;
	}

	@Override
	protected void done() {
<<<<<<< HEAD
		okButton.setEnabled(true);
=======
		//when process is done, okay button can be pressed
		okButton.setEnabled(true);
		//if the process was cancelled, delete the output file that was half worked on
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
		if (isCancelled) {
			if (process.equals("ex") || hasExtracted) {
				File file = new File(saveDir + ".mp3");
				file.delete();
<<<<<<< HEAD
			} else if (process.equals("rm") || hasExtracted) {
				File file = new File("./AudioRM" + fileType);
				file.delete();
			} else if (process.equals("ov")) {
				File file = new File("./overlay" + fileType);
				file.delete();
			} else if (process.equals("rp")) {
				File file = new File("./replace" + fileType);
				file.delete();
			}
			processFrame.dispose();
		} else {
=======
			} else {
				File file = new File(saveDir + fileType);
				file.delete();
			}
			processFrame.dispose();
		// if the process was not cancelled
		} else {
			//following the exit code from get(), show the appropriate message via label
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
			try {
				if (process.equals("ex")) {
					if (get() == 0) {
						progressLabel.setText("Extraction Successful!");
					} else {
						progressLabel.setText("Extraction failed: Unexpected error");
					}
				} else if (process.equals("rm") || process.equals("rm&ex")) {
					if (get() == 0) {
						progressLabel.setText("Audio Removal Successful!");
<<<<<<< HEAD
=======
					} else if (get() == 667) {
						//cancelled option for rm&ex
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
					} else {
						progressLabel.setText("Removal failed: Unexpected error");
					}
				} else if (process.equals("ov")) {
					if (get() == 0) {
						progressLabel.setText("Overlay Successful!");
					} else {
						progressLabel.setText("Overlay failed: Unexpected error");
					}
				} else if (process.equals("rp")) {
					if (get() == 0) {
						progressLabel.setText("Replacement Successful!");
					} else {
						progressLabel.setText("Replacement failed: Unexpected error");
					}
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

<<<<<<< HEAD
=======
	//mid process for extraction before removal
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
	@Override
	protected void process(List<Void> chunks) {
		processFrame.setTitle("Removing audio...");
		progressLabel.setText("Please wait until audio is removed.");
		hasExtracted = true;
	}
}
