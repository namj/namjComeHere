package a03;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class ReplaceFrame extends OverlayFrame{

	public ReplaceFrame(EmbeddedMediaPlayer video, File videoFile) {
		super(video, videoFile);
		super.setLabel("file to replace video:");
		
		ActionListener a = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkIfAudio()) {
					long time = vid.getLength()/1000;
					AudioProcessor aP = new AudioProcessor("rp",vidFile);
					aP.setForOverlay(musicPath,time);
					aP.execute();
					dispose();
				} else {
					String msg = "Selected file is not an audio file!";
					JOptionPane.showMessageDialog(null, msg);
				}
			}
		};
		
		super.setAction("Replace", a);
	}
	
}
