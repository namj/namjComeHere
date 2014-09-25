package a03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
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
		this.process = process;
		this.mediaFile = file;
		for (fileTypes fT : fileTypes.values()) {
			if (file.getName().contains("." + fT)) {
				fileType = "." + fT;
				break;
			} else {
				fileType = ".mp4";
			}
		}
		//Button setup for the frames
		cancelButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				audioProcess.destroy();
				isCancelled = true;
			}
		});
		okButton.setEnabled(false);
		okButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				processFrame.dispose();
			}
		});
		progressLabel = new JLabel();
		
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
	
	public void setSaveDir(String saveDir) {
		this.saveDir = saveDir;
	}
	
	public void setForOverlay(String musicPath, long time) {
		audioPath = musicPath;
		overlayDur = time;
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		String cmd;
		if (process.equals("ex") || process.equals("rm&ex")) {
			cmd = "avconv -i " + mediaFile.getAbsolutePath() + " -vn -c:a libmp3lame"
					+ " -q:a 2 " + saveDir +".mp3";
			audioProcessBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
			audioProcess = audioProcessBuilder.start();
			int exit = audioProcess.waitFor();
			if (process.equals("rm&ex")) {
				if (exit == 0) {
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
		if (process.equals("rm") || process.equals("rm&ex")) {
			cmd = "avconv -i " + mediaFile.getAbsolutePath() + " -c:v copy -an AudioRM" + fileType;
			audioProcessBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
			audioProcess = audioProcessBuilder.start();
			int exit = audioProcess.waitFor();
			if (exit == 0) {
				return 0;
			} else {
				return 666;
			}
		}
		if (process.equals("ov")) {
			cmd = "avconv -i " + mediaFile.getAbsolutePath() + " -i " + audioPath + 
					" -filter_complex [0:a][1:a]amix[out] -map \"[out]\" -map 0:v -c:v copy"
					+ " -t " + overlayDur + " -strict experimental overlay" + fileType;
			audioProcessBuilder = new ProcessBuilder("/bin/bash","-c",cmd);
			audioProcess = audioProcessBuilder.start();
			int exit = audioProcess.waitFor();
			if (exit == 0) {
				return 0;
			} else {
				return 666;
			}
		}
		if (process.equals("rp")) {
			cmd = "avconv -i " + mediaFile.getAbsolutePath() + " -i " + audioPath +
					" -map 1:a -map 0:v -c:v copy -t " + overlayDur + " -strict experimental " +
					"replaced" + fileType;
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
		okButton.setEnabled(true);
		if (isCancelled) {
			if (process.equals("ex") || hasExtracted) {
				File file = new File(saveDir + ".mp3");
				file.delete();
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

	@Override
	protected void process(List<Void> chunks) {
		processFrame.setTitle("Removing audio...");
		progressLabel.setText("Please wait until audio is removed.");
		hasExtracted = true;
	}
}
