package a03;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TitleCreditFrame extends JFrame implements ActionListener {

	private JTextArea _textArea;
	private JTextField _textField1;
	private JTextField _textField2;
	private JLabel _label1, _label2, _label3;
	private JButton _generateButton;
	private JButton _browseButton1;
	private JButton _browseButton2;
	String iconPath = "./icons";
	
	//computer screen dimensions
	private static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	private static final int _screenHeight = (int)d.getHeight();
	private static final int _screenWidth = (int)d.getWidth();
	
	public TitleCreditFrame(){
		
		super("Create Title/Credit page(s)");
		
		ImageIcon openFile = new ImageIcon(iconPath + "/open_button.gif");
		_browseButton.setIcon(openFile);
		_browseButton.setOpaque(false);
		_browseButton.setContentAreaFilled(false);
		_browseButton.setBorderPainted(false);
		_browseButton.setFocusPainted(false);
		add(openButton);
		
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
		this.add(_label3 = new JLabel("select background picture"));
		_label3.setBounds(25, 180, 200,20 );
		this.add(_textField2 = new JTextField());
		_textField2.setBounds(25,200,500,20);
		this.add(_generateButton = new JButton("Generate!"));
		_generateButton.setBounds(200, 250, 200,50);
		_generateButton.addActionListener(this);
		
		
		
		
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _generateButton){
			
		}
		
	}

}
