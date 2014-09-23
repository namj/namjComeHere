package a03;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CreateTitleCreditFrame extends JFrame implements ActionListener {

	//declare variables/components
	private String _selectedVidPath;
	private JTextArea _textArea;
	private JTextField _textField1;
	private JTextField _textField2;
	private JLabel _label1, _label2, _label3;
	private JButton _generateButton;
	private JButton _browseButton1;
	private JButton _browseButton2;
	private String _frameTitle;
	String iconPath = "./icons";
	
	//computer screen dimensions
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	
	public CreateTitleCreditFrame(String mediaPath, String frameTitle){
		
		super(frameTitle);
		
		_selectedVidPath = mediaPath;
		_frameTitle = frameTitle;
		
		//create image icon for browse file button
		ImageIcon openFile = new ImageIcon(iconPath + "/open_button.gif");
		//settings for browse button1
		_browseButton1 = new JButton();
		_browseButton1.setIcon(openFile);
		_browseButton1.setOpaque(false);
		_browseButton1.setContentAreaFilled(false);
		_browseButton1.setBorderPainted(false);
		_browseButton1.setFocusPainted(false);
		//settings for browse button2
		_browseButton2 = new JButton();
		_browseButton2.setIcon(openFile);
		_browseButton2.setOpaque(false);
		_browseButton2.setContentAreaFilled(false);
		_browseButton2.setBorderPainted(false);
		_browseButton2.setFocusPainted(false);
		
		
		setBackground(Color.LIGHT_GRAY);
		setSize(600,350);
		setLayout(null);
		setLocation((_screenWidth-600)/2,(_screenHeight-350)/2);
		
		//add components to this frame
		this.add(_label1 = new JLabel("Enter text"));
		_label1.setBounds(25, 10, 200, 20);
		
		this.add(_textArea = new JTextArea());
		_textArea.setBounds(25, 30, 550, 100);
		
		this.add(_label2 = new JLabel("Select music"));
		_label2.setBounds(25, 140, 200, 20);
		
		this.add(_textField1 = new JTextField());
		_textField1.setBounds(25, 160, 500, 20);
		
		this.add(_browseButton1);
		_browseButton1.setBounds(540 , 160 , 30, 30);
		_browseButton1.addActionListener(this);
		
		this.add(_label3 = new JLabel("select background picture"));
		_label3.setBounds(25, 180, 200,20 );
		
		this.add(_textField2 = new JTextField());
		_textField2.setBounds(25,200,500,20);
		
		this.add(_browseButton2);
		_browseButton2.setBounds(540 , 200 , 30, 30);
		_browseButton2.addActionListener(this);
		
		this.add(_generateButton = new JButton("Generate!"));
		_generateButton.setBounds(200, 250, 200,50);
		_generateButton.addActionListener(this);
		
		//display this frame
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _browseButton1){
			//open Jfilechooser if browse button clicked
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			//store result of filechooser
			int result = fileChooser.showOpenDialog(this);
			
			if (result == JFileChooser.APPROVE_OPTION){
				//the selected file is the video and path is retrieved
				File _mediaFile;
				_mediaFile = fileChooser.getSelectedFile();
			
				//bash command to 'grep' to verify file as audio
				String audCmd = "file " + _mediaFile.getAbsolutePath() + " | grep -i audio";
				ProcessBuilder audCheckBuilder = new ProcessBuilder("/bin/bash","-c",audCmd);
				try {
					//process run
					Process audCheck = audCheckBuilder.start();
					int audTerm = audCheck.waitFor();
					//a correct termination indicates it is a media file
					if (audTerm == 0) {
						//enter this if statement means its audio.
						_textField1.setText(_mediaFile.getAbsolutePath());
					} else {
						JOptionPane.showMessageDialog(this, "File is not an audio type!");
					}
				} catch (Exception ex) {
					//if exception occurs nothing extra happens
				}
			}
			
		} else if (e.getSource() == _browseButton2){
			//open Jfilechooser if browse button clicked
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			//store result of filechooser
			int result = fileChooser.showOpenDialog(this);
			
			if (result == JFileChooser.APPROVE_OPTION){
				//the selected file is the video and path is retrieved
				File _mediaFile;
				_mediaFile = fileChooser.getSelectedFile();
			
				//bash command to 'grep' to verify file as audio
				String imgCmd = "file " + _mediaFile.getAbsolutePath() + " | grep -i image";
				ProcessBuilder imgCheckBuilder = new ProcessBuilder("/bin/bash","-c", imgCmd);
				try {
					//process run
					Process imgCheck = imgCheckBuilder.start();
					int imgTerm = imgCheck.waitFor();
					//a correct termination indicates it is a media file
					if (imgTerm == 0) {
						//enter this if statement means its audio.
						_textField2.setText(_mediaFile.getAbsolutePath());
					} else {
						JOptionPane.showMessageDialog(this, "File is not an image type!");
					}
				} catch (Exception ex) {
					//if exception occurs nothing extra happens
				}
			}
		} else if (e.getSource() == _generateButton){
			//generate title page, or credit page depending on title of frame.
			
			//check that all fields are not blank.
			if (_textArea.getText().length() == 0 || _textField1.getText().length() == 0 || _textField2.getText().length() == 0){
				JOptionPane.showMessageDialog(this, "There are blank fields! Make sure text, music file, image file are specifed");
			} else {
				if (_frameTitle.equals("Create Title page(s)")){
					//pass on true in the constructor to indicate title page generation
					TitleCreditGenerator generator = new TitleCreditGenerator(true, _textArea.getText(), _textField1.getText(), _textField2.getText(), _selectedVidPath);
					generator.execute();
				} else if (_frameTitle.equals("Create Credit page(s)")){
					//pass on false in the constructor to indicate credit page generation
					TitleCreditGenerator generator = new TitleCreditGenerator(false, _textArea.getText(), _textField1.getText(), _textField2.getText(), _selectedVidPath);
					generator.execute();
				}
			}
		}
		
	}

}
