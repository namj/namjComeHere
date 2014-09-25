package a03;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class TextManager extends PlainDocument {

	//variable that will contain max number of characters.
	private int limit;

	//constructor with int parameter
	public TextManager(int limit) {
		super();
		this.limit = limit;
	}

	//method to insert string, sets a restriction that will only use parents insert string method when char limit has not been reached.
	public void insertString (int count, String  text, AttributeSet attribute) throws BadLocationException {
		if (text == null) {
			return;
		}
		if ((getLength() + text.length()) <= limit) {
			super.insertString(count, text, attribute);
		}
	}
	
}
