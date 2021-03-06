package a03;

import javax.swing.*;

import com.sun.jna.Native;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

public class Menu extends JFrame implements ActionListener{
	
	//Get user's screen dimension
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	//Main frame size
	private static final int _menuHeight = 900;
	private static final int _menuWidth = 1200;
<<<<<<< HEAD
 
	private String _mediaPath = "";
	private File _mediaFile;
=======

	private String _mediaPath = "";
	private File _mediaFile;
	private String savePath = "";
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
	
	private MainPanel container;
	private EmbeddedMediaPlayerComponent ourMediaPlayer;
	private EmbeddedMediaPlayer currentVideo;
<<<<<<< HEAD
	
	private JButton openButton;
	private JButton dlButton;
=======
	//additional buttons for the main panel
	private JButton openButton;
	private JButton dlButton;
	private JButton editButton;
>>>>>>> 1d2df0380db5a8bd8f38a92c51a7fe350b674709
	
	public Menu() {
		
		//Frame setup
		setTitle("VAMIX Draft");
		setSize(_menuWidth,_menuHeight);
		setLocation(_screenWidth/2 - _menuWidth/2, _screenHeight/2 - _menuHeight/2);
		setResizable(false);
		setLayout(null);
		//custom close program exiting listener
		
		WindowListener exitListener = new WindowAdapter() {
			//Before the frame is closed set volume to default, and not mute if muted
			@Override
			public void windowClosing(WindowEvent e) {
				//if a video has been used, set default volume to 50 and make it not muted
				if (currentVideo != null) {
					currentVideo.setVolume(60);
					if (currentVideo.isMute()) {
						currentVideo.mute(); //not mute if muted
					}
				}
				System.exit(0); //exit program
			}		
		};
		addWindowListener(exitListener);
		
		//-----------------------------------------------------------------------
		
		//Container SETUP
		
		//embedded media player setup
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		ourMediaPlayer = new EmbeddedMediaPlayerComponent();
		int videoPlayerWidth = this.getWidth()-10;
		int videoPlayerHeight = 700;
		ourMediaPlayer.setSize(videoPlayerWidth,videoPlayerHeight);
		ourMediaPlayer.setLocation((_menuWidth-videoPlayerWidth)/2,70);
		
		//instantiate main panel passing in Media Player
		container = new MainPanel(ourMediaPlayer);
		
		//a shortcut open button for the interface set up
		openButton = new JButton();
		openButton.setSize(30,30);
		openButton.setLocation(815,20);
		openButton.setActionCommand("Open File");
		openButton.addActionListener(this);
		container.setOpenButton(openButton);
		
		//a shortcut download button for the interface set up
		dlButton = new JButton("Download");
		dlButton.setSize(150,40);
		dlButton.setLocation(50,15);
		dlButton.setActionCommand("Download File");
		dlButton.addActionListener(this);
		container.setButton(dlButton);
		
		//a shortcut edit button for the edit function frame
		editButton = new JButton("Edit");
		editButton.setBounds(220, 15, 150, 40);
		editButton.setActionCommand("Edit");
		editButton.addActionListener(this);
		container.setButton(editButton);
		
		setContentPane(container);
		
		//------------------------------------------------------------------------
		
		//Menu bar added
		setJMenuBar(setUpMenuBar());

	}
	
	//Method to set up menu bar to be used in the frame
	private JMenuBar setUpMenuBar() {
		//create object for all menu bar, menus and items
		JMenu file, edit, help, _space, _space2;

		JMenuItem _open, _exit, _dl, _title, _credit;
		JMenuItem _rmAudio,_exAudio,_ovAudio, _rpAudio, _read;
		JMenuBar menuBar = new JMenuBar();
		
		//set the graphics (color) for the Menu bar
		menuBar.setBackground(Color.DARK_GRAY);
		menuBar.setBorderPainted(true);
		
		//empty spaces to put in between menu options (spacings)
		_space = new JMenu();
		_space.setEnabled(false);
		_space2 = new JMenu();
		_space2.setEnabled(false);
		
		//setup of menu 'file' 
		file = new JMenu("File");
		file.setForeground(Color.LIGHT_GRAY);
		//setup of all the items belonging to the 'file' menu
		_open = new JMenuItem("Open");
		_dl = new JMenuItem("Download");
		_open.setActionCommand("Open File");
		_open.addActionListener(this);
		_dl.setActionCommand("Download File");
		_dl.addActionListener(this);
		_exit = new JMenuItem("Exit");
		_exit.setActionCommand("Exit");
		_exit.addActionListener(this);
		file.add(_open);
		file.add(_dl);
		file.addSeparator();
		file.add(_exit);
		menuBar.add(file);
		menuBar.add(_space);
		
		//setup of menu 'edit'
		edit = new JMenu("Edit");
		edit.setForeground(Color.LIGHT_GRAY);
		//setup of all the items belonging to the 'edit' menu
		_title = new JMenuItem("Add title page(s)");
		_title.setActionCommand("Create title");
		_title.addActionListener(this);
		edit.add(_title);
		_credit = new JMenuItem("Add credit page(s)");
		_credit.setActionCommand("Create credit");
		_credit.addActionListener(this);
		edit.add(_credit);
		edit.addSeparator();
		_rmAudio = new JMenuItem("Remove Audio");
		_rmAudio.setActionCommand("rmAudio");
		_rmAudio.addActionListener(this);
		_exAudio = new JMenuItem("Extract Audio");
		_exAudio.setActionCommand("exAudio");
		_exAudio.addActionListener(this);
		_ovAudio = new JMenuItem("Overlay Audio");
		_ovAudio.setActionCommand("ovAudio");
		_ovAudio.addActionListener(this);
		_rpAudio = new JMenuItem("Replace Audio");
		_rpAudio.setActionCommand("rpAudio");
		_rpAudio.addActionListener(this);
		edit.add(_rmAudio);
		edit.add(_exAudio);
		edit.add(_ovAudio);
		edit.add(_rpAudio);

		menuBar.add(edit);
		menuBar.add(_space2);
		
		//setup of menu 'help'
		help = new JMenu("Help");
		help.setForeground(Color.LIGHT_GRAY);
		_read = new JMenuItem("Open ReadMe");
		_read.setActionCommand("Open readme");
		_read.addActionListener(this);
		help.add(_read);
		menuBar.add(help);
		
		//final menu bar is returned at the end of the setup 
		return menuBar;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Menu().setVisible(true);
				int r = JOptionPane.showConfirmDialog(null, "New to this? Would you like to open a helper?");
				if (r == JOptionPane.YES_OPTION) {
					openHelpFrame();
				} else {
					//nothing happens
				}
			}
		});
	}
	
	private int checkAudioSignal() {
		if (_mediaFile != null) {
			boolean isVideo = false;
			boolean isAudio = false;
		
			//bash command to 'grep' to verify file as media
			String audCmd = "avconv -i " + _mediaFile.getAbsolutePath() + " 2>&1 | grep Audio:";
			String vidCmd = "avconv -i " + _mediaFile.getAbsolutePath() + " 2>&1 | grep Video:";
			
			ProcessBuilder audCheckBuilder = new ProcessBuilder("/bin/bash","-c",audCmd);
			ProcessBuilder vidCheckBuilder = new ProcessBuilder("/bin/bash","-c",vidCmd);
			try {
				//process run
				Process audCheck = audCheckBuilder.start();
				int audTerm = audCheck.waitFor();
				Process vidCheck = vidCheckBuilder.start();
				int vidTerm = vidCheck.waitFor();
				//a correct termination indicates it is a media file
				if (audTerm == 0) {
					isAudio = true;
				} 
				if (vidTerm == 0){
					isVideo = true;
				}
				//only video files with audio signals are checked correct (0 for success)
				if (isAudio && !isVideo) {
					JOptionPane.showMessageDialog(null, "Opened file must be of video type");
					return 3;
				} else if (isAudio && isVideo) {
					return 0;
				}
			} catch (Exception ex) {
				//if exception occurs nothing extra happens
			}
		} else {
			JOptionPane.showMessageDialog(null, "Open a file before attempting any audio operation.");
			return 1;
		}
		return 2;
	}
	
	//method to start playing a video given a file
	public void startPlayVideo(File mediaF) {
		_mediaFile = mediaF;
		_mediaPath = _mediaFile.getAbsolutePath();
		
		//booleans to decide whether selected file is of a media file
		boolean isVideo = false;
		boolean isAudio = false;
	
		//bash command to 'grep' to verify file as media
		String audCmd = "avconv -i " + _mediaFile.getAbsolutePath() + " 2>&1 | grep Audio:";
		String vidCmd = "avconv -i " + _mediaFile.getAbsolutePath() + " 2>&1 | grep Video:";
		
		ProcessBuilder audCheckBuilder = new ProcessBuilder("/bin/bash","-c",audCmd);
		ProcessBuilder vidCheckBuilder = new ProcessBuilder("/bin/bash","-c",vidCmd);
		try {
			//process run
			Process audCheck = audCheckBuilder.start();
			int audTerm = audCheck.waitFor();
			Process vidCheck = vidCheckBuilder.start();
			int vidTerm = vidCheck.waitFor();
			//a correct termination indicates it is a media file
			if (audTerm == 0) {
				isAudio = true;
			} 
			if (vidTerm == 0){
				isVideo = true;
			}
		} catch (Exception ex) {
			//if exception occurs nothing extra happens
		}
		//when media file is selected
		if (isVideo || isAudio) {
			//current video is instantiated and paused immediately when it starts playing
			currentVideo = ourMediaPlayer.getMediaPlayer();
			currentVideo.startMedia(_mediaPath);
			currentVideo.pause();
			//video is set in the main panel
			container.setCurrentVid(currentVideo,_mediaFile);
			
			if (isVideo) {
				//media buttons (play, fast-forward, etc) are enabled
				container.setMediaButtonOn();
			} else if (isAudio) {
				//audio buttons (play, etc) are enabled
				container.setAudioButtonOn();
			}
		//warning message if file is not media
		} else {
			JOptionPane.showMessageDialog(this, "File is not an audio or video type!");
		}
	}
	
	//method to extract (method used as it is used more than once)
	//input string to know if 'ex' or 'rm&ex'
	public void extractAudio(String eORr) {
		//file chooser to direct save file name
		JFileChooser dirChooser = new JFileChooser(_mediaFile);
		int response = dirChooser.showSaveDialog(null);
		//when save is clicked
		if (response == JFileChooser.APPROVE_OPTION) {
			//file retrieved and its path
			File file = dirChooser.getSelectedFile();
			String saveDir = file.getAbsolutePath();
			AudioProcessor aP;
			if (eORr.equals("ex")) {
				//processor with 'ex' input for just extracting
				aP = new AudioProcessor("ex",_mediaFile);
			} else {
				//processor with 'rm&ex' input for extract -> removal
				aP = new AudioProcessor("rm&ex",_mediaFile);
			}
			//set the save directory and execute worker
			aP.setSaveDir(saveDir);
			aP.execute();
		} else {
			//nothing happens when cancelled
		}
	}
	
	public int setUpSaveFile() {
		JFileChooser saveChooser = new JFileChooser(_mediaFile);
		saveChooser.setDialogTitle("Select directory, enter file name to save as...");
		int response = saveChooser.showSaveDialog(null);
		if (response == JFileChooser.APPROVE_OPTION) {
			savePath = saveChooser.getSelectedFile().getAbsolutePath();
		}
		return response;
	}
	
	public static void openHelpFrame() {
		HelpFrame help = new HelpFrame();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Open File")) {
			//when item is selected, a File chooser opens to select a file
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = fileChooser.showOpenDialog(this);
			//for when cancel/exit is pressed in the file chooser
			if (result == JFileChooser.CANCEL_OPTION) {
				//nothing is to be done
			//for when a file is selected
			} else if (result == JFileChooser.OPEN_DIALOG) {
				startPlayVideo(fileChooser.getSelectedFile());
			}
		} else if (e.getActionCommand().equals("Download File")){
			String dlURL;
			//option pane that will take in the URL of download
			dlURL = JOptionPane.showInputDialog(this, "Please Enter URL:", "Download", 
					JOptionPane.DEFAULT_OPTION);
			//if cancelled do nothing
			if (dlURL == null) {
				//download cancelled before beginning
			} else {
				String msg = "Is this an open source file?";
				int reply = JOptionPane.showConfirmDialog(null, msg);
				//Whether its open source is confirmed, if yes
				if (reply == JOptionPane.YES_OPTION) {
					int rep = setUpSaveFile();
					if (rep == JFileChooser.APPROVE_OPTION) {
						//download frame opened and download commences
						DownloadFrame downloadFrame = new DownloadFrame(dlURL);
						downloadFrame.setSaveDir(savePath);
						downloadFrame.startDownload();
					}
				} else if (reply == JOptionPane.NO_OPTION){
					String warning = "Please only download from open source files\n"
							+ "Downloading non-open source files are illegal!";
					JOptionPane.showMessageDialog(null, warning);
				}
			}
		} else if (e.getActionCommand().equals("Edit")) {
			//pop up edit frame when edit button or item is selected
			EditFrame editFrame = new EditFrame(this);
		} else if (e.getActionCommand().equals("Exit")) {
			//exit when exit item is pressed
			System.exit(0);
		} else if (e.getActionCommand().equals("rmAudio")) {
			//attain return value from checking audio signal
			int audioCheck = checkAudioSignal();
			//if signal is successful....
			if (audioCheck == 0) {
				//ask if audio getting removed should be saved (extracted)
				String msg = "Would you like to save the audio into another file?";
				int response = JOptionPane.showConfirmDialog(null, msg);
				if (response == JOptionPane.YES_OPTION) {
					//perform extraction based on 'rm&ex' when yes 
					extractAudio("rm&ex");
				} else if (response == JOptionPane.NO_OPTION) {
					int rep = setUpSaveFile();
					if (rep == JFileChooser.APPROVE_OPTION) {
						//if no, begin audio process for removal (rm)
						AudioProcessor aP = new AudioProcessor("rm",_mediaFile);
						aP.setSaveDir(savePath);
						aP.execute();
					}
				}
			//if check returns a 2, no audio signal
			} else if (audioCheck == 2) {
				String msg = "The media contains no audio signal!\n" +
						"There is no audio to be removed.";
				JOptionPane.showMessageDialog(null,msg);
			}
		} else if (e.getActionCommand().equals("exAudio")) {
			//same as above
			int audioCheck = checkAudioSignal();
			if (audioCheck == 0) {
				//perform extraction based on 'ex'
				extractAudio("ex");
			} else if (audioCheck == 2) {
				String msg = "The media contains no audio signal!\n" +
						"There is no audio to be extracted.";
				JOptionPane.showMessageDialog(null,msg);
			}
		} else if (e.getActionCommand().equals("ovAudio")) {
			//same as above
			int audioCheck = checkAudioSignal();
			if (audioCheck == 0) {
				int rep = setUpSaveFile();
				if (rep == JFileChooser.APPROVE_OPTION) {
					//overlay frame pops up after audio is checked
					OverlayFrame ovFrame = new OverlayFrame(currentVideo,_mediaFile, savePath);
				}
			} else if (audioCheck == 2) {
				String msg = "The media contains no audio signal!\n" +
						"There is no audio to be overlayed.";
				JOptionPane.showMessageDialog(null,msg);
			}
		} else if (e.getActionCommand().equals("rpAudio")) {
			//gets checked audio int
			int audioCheck = checkAudioSignal();
			//doesn't matter whether there is an audio signal or not
			if (audioCheck == 0 || audioCheck == 2) {
				int rep = setUpSaveFile();
				if (rep == JFileChooser.APPROVE_OPTION) {
					ReplaceFrame rpFrame = new ReplaceFrame(currentVideo,_mediaFile, savePath);
				}
			}
		//for command create title/credit page, open appropriate frame
		} else if (e.getActionCommand().equals("Create title")){
			CreateTitleCreditFrame titleFrame = new CreateTitleCreditFrame(_mediaPath, "Create Title page(s)");
		} else if (e.getActionCommand().equals("Create credit")){
			CreateTitleCreditFrame creditFrame = new CreateTitleCreditFrame(_mediaPath, "Create Credit page(s)");
		//when help item is pressed, open the readme file in a scrollpane
		} else if (e.getActionCommand().equals("Open readme")) {
			openHelpFrame();
		}
	}
}
