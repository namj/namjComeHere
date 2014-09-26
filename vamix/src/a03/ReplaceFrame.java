package a03;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class ReplaceFrame extends OverlayFrame{
<<<<<<< HEAD

	public ReplaceFrame(EmbeddedMediaPlayer video, File videoFile) {
		super(video, videoFile);
		super.setLabel("file to replace video:");
		
		ActionListener a = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkIfAudio()) {
					long time = vid.getLength()/1000;
					AudioProcessor aP = new AudioProcessor("rp",vidFile);
=======
	private String savePath;

	public ReplaceFrame(EmbeddedMediaPlayer video, File videoFile, String saveDir) {
		//uses Overlay Frame's constructor
		super(video, videoFile, saveDir);
		//with a changed label
		super.setLabel("file to replace video:");
		
		//saved path is stored in a private field
		savePath = saveDir;
		
		//action listener for the replace function
		ActionListener a = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//checks for audio signal and sets up audio process swing worker with inputs
				if (checkIfAudio()) {
					long time = vid.getLength()/1000;
					AudioProcessor aP = new AudioProcessor("rp",vidFile);
					aP.setSaveDir(savePath);
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
					aP.setForOverlay(musicPath,time);
					aP.execute();
					dispose();
				} else {
					String msg = "Selected file is not an audio file!";
					JOptionPane.showMessageDialog(null, msg);
				}
			}
		};
		
<<<<<<< HEAD
=======
		//overrides the action button in overlay frame to make it an appropriate replace frame
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
		super.setAction("Replace", a);
	}
	
}
