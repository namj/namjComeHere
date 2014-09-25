package a03;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalSliderUI;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MainPanel extends JPanel{
	String iconPath = "./icons";
	//all GUI/video objects set private for use within class
	private PlaybackPanel pbP;
	private VolumePanel vP;
	
	private JSlider mediaProgress;
	private JLabel videoTime;
	private JTextField videoName;
	private File mediaFile;
	private EmbeddedMediaPlayer currentVideo;
	private EmbeddedMediaPlayerComponent videoPlayer;
	private MediaProgressChecker mPC;

	public MainPanel(EmbeddedMediaPlayerComponent vidPlayer) {
		//set layout for panel as null, for absolute positioning
		super(null);
		
		setBackground(Color.GRAY);
		
		//video player set from the input of the constructor
		videoPlayer = vidPlayer;
		
		//---------------------MEDIA PLAYER SETUP----------------------------//
		
		//media player added to the panel (all size and location previously set up)
		add(videoPlayer);
		
		//setup of media progress slider to keep track of media
		mediaProgress = new JSlider();
		mediaProgress.setSize(videoPlayer.getWidth()-70,20);
		mediaProgress.setLocation(videoPlayer.getX()+70,770);
		mediaProgress.setValue(0);
		mediaProgress.setBackground(Color.BLACK);
		//modify default UI to remove progress knob of slider
		mediaProgress.setUI(new MetalSliderUI() {
			@Override
			public void paintThumb(Graphics g) {
				//do nothing to get rid of the knob
			}
		});
		//mouse listener for entering and leaving for cursor change, also clicking on point
		//of progress to jump to part
		mediaProgress.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				//when it's enabled, when mouse enters, hand cursor is used
				if (mediaProgress.isEnabled()) {
					setCursor(new Cursor(Cursor.HAND_CURSOR));		
				}
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				//when exits, default cursor is used
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));		
			}
			@Override
			public void mousePressed(MouseEvent arg0) {	
				if (mediaProgress.isEnabled()) {
					currentVideo.pause();
					
				}
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {	
				if (mediaProgress.isEnabled()) {
					try {
						//get mouse point
						double point = mediaProgress.getMousePosition().getX();
						//get ratio of slider value per pixel
						double ratio = mediaProgress.getMaximum() / (mediaProgress.getWidth()-8);
						//get the value to be used for value/ time
						double result = ratio * point;
						//set slider value, play video in time
						mediaProgress.setValue((int)result);
						currentVideo.setTime((long)result);
					} catch (NullPointerException e) {
						//do nothing
					}
					currentVideo.play();
					pbP.setToPause();
				}
			}			
		});
		mediaProgress.setEnabled(false);
		add(mediaProgress);
		
		videoTime = new JLabel("  00:00:00");
		videoTime.setOpaque(true);
		videoTime.setBounds(videoPlayer.getX(), 770, 70, 20);
		videoTime.setForeground(Color.WHITE);
		videoTime.setBackground(Color.BLACK);
		add(videoTime);
		
		//set up for text field to show what video/ audio is playing
		videoName = new JTextField("Please open a media file...");
		videoName.setSize(275, 30);
		videoName.setLocation(850, 20);
		videoName.setEditable(false);
		add(videoName);
		
		//-------------------- MEDIA BUTTON SETUP---------------------------------//
		
		pbP = new PlaybackPanel();
		add(pbP);
		
		//---------------------------MEDIA VOLUME CONTROL--------------------------//
		
		vP = new VolumePanel();
		add(vP);
		
	}
	
	//-------------------------METHODS FOR FUNCTION ENABLITLIY------------------//

	//method to enable all media buttons
	public void setMediaButtonOn() {
		pbP.videoOn();
		vP.volumeOn();
		mediaProgress.setEnabled(true);
	}
	//method to enable only audio buttons
	public void setAudioButtonOn() {
		pbP.audioOn();
		vP.volumeOn();
		mediaProgress.setEnabled(true);
	}
	
	//-----------------------METHODS TO SET UP COMPONENTS FROM OTHER CLASSES------------------//
	
	//method to set the current video (media)
		public void setCurrentVid(EmbeddedMediaPlayer vid, File vidFile){
			currentVideo = vid;
			mediaFile = vidFile;
			pbP.setCurrentVideo(currentVideo);
			vP.setCurrentVideo(currentVideo);
			//media name is shown on text field to show what's playing
			videoName.setText(vidFile.getName());
			//set up of progress slider with media time length
			mediaProgress.setMinimum(0);
			mediaProgress.setMaximum((int)currentVideo.getLength());
			//when a valid current video is set, begin media progress checking
			mPC = new MediaProgressChecker(this);
			mPC.execute();
		}
	//an additional button to easily open files
	public void setOpenButton(JButton openButton) {
		ImageIcon openFile = new ImageIcon(iconPath + "/open_button.gif");
		openButton.setIcon(openFile);
		openButton.setOpaque(false);
		openButton.setContentAreaFilled(false);
		openButton.setBorderPainted(false);
		openButton.setFocusPainted(false);
		add(openButton);
	}
	//an additional button to easily download files
	public void setButton(JButton button) {
		add(button);
	}
	
	//----------------------METHODS TO ACCESS PRIVATE OBJECTS-----------//
	
	//method to retrieve the current
	public EmbeddedMediaPlayer getMedia() {
		return currentVideo;
	}
	
	//--------------------METHOD TO UPDATE GUI FROM OTHER CLASSES-----------------//
	
	//method to literally restart video that was playing, when it ends
	public void restart() {
		currentVideo.startMedia(mediaFile.getAbsolutePath());
		currentVideo.pause();
		setCurrentVid(currentVideo, mediaFile);
		pbP.setToDefault();
	}
	
	//method used by video progress checker to update progress bar of video
	public void updateMediaProgress() {
		mediaProgress.setValue((int)currentVideo.getTime());
		String hs,ms,ss;
		long time = currentVideo.getTime()/1000;
		int hours = (int) time / 3600;
	    int remainder = (int) time - hours * 3600;
	    int mins = remainder / 60;
	    remainder = remainder - mins * 60;
	    int secs = remainder;
	    if (hours < 10) {
	    	hs = "0" + hours;
	    } else {
	    	hs = "" + hours;
	    }
	    if (mins < 10) {
	    	ms = "0" + mins;
	    } else {
	    	ms = "" + mins;
	    }
	    if (secs < 10) {
	    	ss = "0" + secs;
	    } else {
	    	ss = "" + secs;
	    }
	    videoTime.setText("   " + hs + ":" + ms + ":" + ss);
	}
}
