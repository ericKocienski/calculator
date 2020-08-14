/* Semester 2 Project -Windows Calculator
 * 
 * Author: Eric Kocienski
 * finished May 5, 2019
 *  
 * make the equivalent of the windows 10 programming calculator in java
 * 
 * my calculator is able the follow the order of operations as well as perform real-time conversions between dec, bin, hex, and oct numbers
 * signed numbers are represented in 2s complement notation
 * possible operations are: add, subtract, multiply, divide, mod
 * you are able to use parenthesis 
 * 
 * QWORD, DWORD, WORD, and BYTE buttons and modes are implemented, the mode will restrict what you can enter in the calculator mainField, 
 * pressing QWORD, DWORD, WORD, and BYTE will change the number in mainField to fit the constraints. Math results in these modes will fir constraints
 * 
 * input validation is in place so that you cannot break the calculator. e.g: cant place more ) than (, cant put two operators next to each together without 
 * any operands, etc
 * 
 * font will change size as number in mainfield gets bigger, binary text will wrap in binField
 * 
 * includes custom icons to maintain the proper look and feel
 * 
 */

import javax.swing.JFrame;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.swing.*;

public class WindowsCalculator extends JFrame implements ActionListener{
	
	/** declare all buttons for constructor*/
	JFrame f ;
	JButton[] numPadButtons;
	JButton[] alphabetPadButtons;
	JButton menuButton;
	JButton hexButton;
	JButton decButton;
	JButton octButton;
	JButton binButton;
	JButton fullKeypadButton;
	JButton bitTogglingKeypadButton;
	JButton qwordButton;
	JButton dwordButton;
	JButton wordButton;
	JButton byteButton;
	JButton memoryStoreButton;
	JButton memoryButton;
	JButton lshButton;
	JButton rshButton;
	JButton orButton;
	JButton xorButton;
	JButton notButton;
	JButton andButton;
	JButton upArrowButton;
	JButton modButton;
	JButton clearEntryButton; //ce button, clears the most recent entry
	JButton clearAllButton; //c button
	JButton backspaceButton;
	JButton divideButton;
	JButton	multiplyButton;
	JButton minusButton;
	JButton plusButton;
	JButton equalsButton;
	JButton openParenButton;
	JButton closeParenButton;
	JButton decimalPointButton;
	JButton plusMinusButton;

	/** declare all fonts for constructor*/
	Font font1 = new Font("Segoe UI", Font.PLAIN, 31);
	Font font2 = new Font("Segoe UI", Font.PLAIN, 16);
	Font font3 = new Font("Segoe UI", Font.PLAIN, 15);
	Font font4 = new Font("Segoe UI", Font.PLAIN, 11);
	
	/** declare all labels for constructor*/
	JLabel label1;
	JLabel label2;
	
	/** declare all text fields for constructor*/
	JTextField mainField;
	JTextField calcField;
	JTextField hexField;
	JTextField decField;
	JTextField octField;
	
	/** declare all text areas for constructor. text areas allow fro wrapping*/
	JTextArea binField;
	
	/** only one panel used*/
	JPanel calculator;
	
	/** These strings are unicode for various symbols used on calculator */
	static final String menu = "\u2630"; // the menu button, trigram for heaven (U+2630)
	static final String plusMinus =  "\u00B1"; //plus-minus sign (U+00B1)
	static final String upArrow = "\u2191"; // an upwards arrow (U+2191)
	static final String backspace = "\u232B"; //the backspace icon (U+232B)
	static final String divide = "\u00F7"; //the division symbol	(U+00F7)
	static final String multiply = "\u00D7"; //the multiply symbol	(U+00D7)
	static final String minus = "\u2212"; // U+2212
	static final String qwordString = "QWORD";
	static final String dwordString  = "DWORD";
	static final String wordString  = "WORD";
	static final String byteString  = "BYTE";
	static final String[] subScript = {"(","("+"\u2081","("+"\u2082","("+"\u2083","("+"\u2084","("+"\u2085","("+"\u2086","("+"\u2087","("+"\u2088","("+"\u2089"}; //used to mark when you use a parenthesis

	static final char[] alphabetArray = {'A','B','C','D','E','F'};//array for alphabet buttons
	static final char divideChar = '\u00F7'; //the division symbol	(U+00F7)
	static final char multiplyChar = '\u00D7'; //the multiply symbol	(U+00D7)
	static final char minusChar = '\u2212'; // this minus symbol, different from the negative symbol: '-'(U+2212)
	
	/** icons used in calculator are located in resources folder*/
	ImageIcon frameIcon = new ImageIcon("resources/frameIcon.png");
	ImageIcon menuIcon = new ImageIcon("resources/menu.png");
	ImageIcon fullkeypadIcon = new ImageIcon("resources/mainkeypad.png");
	ImageIcon bitkeypadIcon = new ImageIcon("resources/otherkeypad.png");
	
	Color lightBlue= new Color(0,150,255);	// custom color for windows calclator
	
	int mainFieldSize = 19; //declaring default size for mainField
	int parenCounter = 0; //initializing counter that controls how many parenthesis you can input
	
	String equation; //global variable that holds your equation for calculation
	String answer; //global variable that holds your answer after calculation
	//String numberString;
	String lastButtonPressed;// global variable keeps track of last button pressed for input validation
	String inputType; // input type will either be hex, dec, bin, oct

	String beforeNegate = ""; //value before negating is kept in case you want to reverse it
	
	boolean isNegative = false; //kept for input validation reasons
	boolean operatorPressed = false; //kept for input validation reasons
	 
	GridBagConstraints c; //constraints for all elements in the calculator, controls placement,etc

	/** winddowsCalculator main method*/
	public static void main(String[] args) {
		
		// makes the gui look like windows
		try { 
		    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		    e.printStackTrace();
		}
	
		
		WindowsCalculator window = new WindowsCalculator();
		window.setTitle("Calculator");
		window.setVisible(true);
		window.setResizable(false); //you dont want the user to be able to resize the calculator
		window.pack(); //sets size of window to be determined by its components
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}
	/** winddowsCalculator constructor*/
	WindowsCalculator(){	
		setIconImage(frameIcon.getImage());
		
		calculator= new JPanel();
		calculator.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		getContentPane().add(calculator);

		// your labels, args are: (String name, Font font, int x, int y, int z, int dimensionx, int dimensiony)
		label1 					= addLabel("  Programmer", font2, 1, 0, 2, 100, 30);	 
		
		// your text fields, args are: (int size, Font font, int x, int y, int z, int dimensionx, int dimensiony)
		mainField 				= addTextField(11, font1, 0, 2, 6, 0, 50);// will show your number
		calcField 				= addTextField(20, font3, 0, 1, 6, 0, 25);// will show the expression
		hexField				= addTextField(24, font4, 1, 3, 6, 0, 25);// will show your number in hex
		decField				= addTextField(24, font4, 1, 4, 6, 0, 25);// will show your number in decimal
		octField				= addTextField(24, font4, 1, 5, 6, 0, 25);// will show your number in oct
		binField				= addTextArea(24, 2, font4, 1, 6, 6, 0, 25);// will show your number in binary
		
		mainField.setHorizontalAlignment(SwingConstants.RIGHT); //makes it so that numbers are displayed on the right
		calcField.setHorizontalAlignment(SwingConstants.RIGHT);
		
		calcField.setText("");
		mainField.setText("0");
		decField.setText("0");
		binField.setText("0");
		hexField.setText("0");
		octField.setText("0");

		
		// your buttons, args: (String buttonText,     		 int x, int y, int z, int dimensionx, int dimensiony)
		menuButton 				= addButton(menuIcon, 				0, 0,  1, 55,   35);
		hexButton 		 		= addButton("HEX",					0, 3,  1, 55,   35);		
		decButton 		 		= addButton("DEC", 					0, 4,  1, 55,   35);		
		octButton 		 		= addButton("OCT", 					0, 5,  1, 55,   35);		
		binButton 		 		= addButton("BIN",					0, 6,  1, 55,   35);		
		fullKeypadButton 		= addButton(fullkeypadIcon,			0, 7,  1, 55,   35);		
		bitTogglingKeypadButton = addButton(bitkeypadIcon, 			1, 7,  1, 55,   35);		
		qwordButton				= addButton(qwordString, 			2, 7,  2, 102,  35);
		memoryStoreButton		= addButton("MS", 					4, 7,  1, 55,   35);
		memoryButton 			= addButton("M", 					5, 7,  1, 55,   35);
		lshButton 				= addButton("Lsh", 					0, 8,  1, 55,   35);
		rshButton				= addButton("Rsh", 					1, 8,  1, 55,   35);
		orButton 				= addButton("Or", 					2, 8,  1, 55,   35);
		xorButton 				= addButton("Xor", 					3, 8,  1, 55,   35);
		notButton				= addButton("Not", 					4, 8,  1, 55,   35);
		andButton 				= addButton("And", 					5, 8,  1, 55,   35);
		upArrowButton			= addButton(upArrow, 				0, 9,  1, 55,   35);
		modButton 				= addButton("Mod", 					1, 9,  1, 55,   35);	
		clearEntryButton 		= addButton( "CE", 					2, 9,  1, 55,   35);
		clearAllButton 			= addButton("C", 					3, 9,  1, 55,   35);
		backspaceButton			= addButton( backspace, 			4, 9,  1, 55,   35);
		divideButton 			= addButton(divide, 				5, 9,  1, 55,   35);
		multiplyButton 			= addButton(multiply, 				5, 10, 1, 55,   35);	
		minusButton				= addButton( minus, 				5, 11, 1, 55,   35);		
		plusButton 				= addButton( "+", 					5, 12, 1, 55,   35);		
		openParenButton 		= addButton( "(", 					0, 13, 1, 55,   35);
		closeParenButton 		= addButton(")", 					1, 13, 1, 55,   35);
		plusMinusButton 		= addButton( plusMinus, 			2, 13, 1, 55,   35);
		decimalPointButton 		= addButton( ".", 					4, 13, 1, 55,   35);
		equalsButton 			= addButton("=", 					5, 13, 1, 55,   35);
		
		/* these buttons do not have borders in windows calculator*/
		hexButton.setBorderPainted(false);
		decButton.setBorderPainted(false);
		octButton.setBorderPainted(false);
		binButton.setBorderPainted(false);
		menuButton.setBorderPainted(false);
		fullKeypadButton.setBorderPainted(false);
		bitTogglingKeypadButton.setBorderPainted(false);
		qwordButton.setBorderPainted(false);
		memoryStoreButton.setBorderPainted(false);
		memoryButton.setBorderPainted(false);
		
		//these buttons have different colored text
		qwordButton.setForeground(lightBlue);
		
		//these buttons are disabled and cannot be used on the programming calculator
		decimalPointButton.setEnabled(false);
		
		// this for loop will add the number buttons to the calculator panel with the necessary constraints
		numPadButtons = new JButton[10];
		for(int i = 0; i < 10; i++) {
			numPadButtons[i] = new JButton(""+i);
			numPadButtons[i].setPreferredSize(new Dimension(55,35));
			
			if(i <= 9 && i >= 7) {
				if (i == 7) {
					c.gridx = 2;
				}
				else if (i == 8) {
					c.gridx = 3;
				}
				else if (i == 9) {
					c.gridx = 4;
				}
				c.gridy = 10;	
			}
			else if(i <= 6 && i >= 4) {
				if (i == 4) {
					c.gridx = 2;
				}
				else if (i == 5) {
					c.gridx = 3;
				}
				else if (i == 6) {
					c.gridx = 4;
				}
				c.gridy = 11;
			}
			else if(i <= 3 && i >= 1){
				if (i == 1) {
					c.gridx = 2;
				}
				else if (i == 2) {
					c.gridx = 3;
				}
				else if (i == 3) {
					c.gridx = 4;
				}
				c.gridy = 12;				
			}
			else {
				c.gridx = 3;
				c.gridy = 13;				
			}
			calculator.add(numPadButtons[i],c);
			numPadButtons[i].addActionListener(this);
		}// end of number pad for loop
		
		alphabetPadButtons = new JButton[6]; //adding the a-f buttons
		for(int i = 0; i < 6; i++) {
			alphabetPadButtons[i] = new JButton(""+alphabetArray[i]);
			alphabetPadButtons[i].setPreferredSize(new Dimension(55,35));
			
			if( i>=0 && i <=1) {
				if(i == 0) {
					c.gridx = 0; 
				}
				else if(i == 1) {
					c.gridx = 1;
				}
				c.gridy = 10;
			}
			else if( i>=2 && i <=3) {
				if(i == 2) {
					c.gridx = 0; 
				}
				else if(i == 3) {
					c.gridx = 1;
				}
				c.gridy = 11;
			}
			else if( i>=4 && i <=5) {
				if(i == 4) {
					c.gridx = 0; 
				}
				else if(i == 5) {
					c.gridx = 1;
				}
				c.gridy = 12;
			}
			calculator.add(alphabetPadButtons[i],c);
			alphabetPadButtons[i].addActionListener(this);
		}// end of alphabet for loop
		
		//alphabet buttons are disabled unless hex button is pressed, dec is default
		inputType = "dec";
		decButton.setForeground(lightBlue);
		
		for(int i=0; i < 6; i++) {
			alphabetPadButtons[i].setEnabled(false);
		}
		mainField.setText("0");		
	} // end of constructor


	/** this method will build and add labels with necessary size and constraints */
	public JLabel addLabel(String name, Font font, int x, int y, int z, int dimensionx, int dimensiony) {
		JLabel label= new JLabel(name);
		label.setFont(font);
		label.setPreferredSize(new Dimension(dimensionx,dimensiony));
		c = setConstraints(x , y, z);
		calculator.add(label,c);
		
		return label;
	}
	
	/** this method will build and add buttons with necessary size and constraints */
	public JButton addButton(String buttonText, int x, int y, int z, int dimensionx, int dimensiony) {
		JButton button = new JButton(buttonText);
		button.setPreferredSize(new Dimension(dimensionx,dimensiony));
		button.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		c = setConstraints(x,y,z);
		calculator.add(button,c);
		
		button.addActionListener(this);
		return button;
	}
	
	/** this is a seperate method for buttons that sue icons*/
	public JButton addButton(ImageIcon icon, int x, int y, int z, int dimensionx, int dimensiony) {
		JButton button = new JButton(icon);
		button.setPreferredSize(new Dimension(dimensionx,dimensiony));
		button.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		c = setConstraints(x,y,z);
		calculator.add(button,c);
		
		button.addActionListener(this);
		return button;
	}
		
	/** this method will build and add text fields with necessary size and constraints */
	public JTextField addTextField(int size, Font font, int x, int y, int z, int dimensionx, int dimensiony) {
		JTextField textField= new JTextField(size); 
		textField.setPreferredSize(new Dimension(dimensionx,dimensiony));
		textField.setFont(font);
		textField.setEditable(false);
		textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		
		c = setConstraints(x,y,z);
		calculator.add(textField,c);
		
		return textField;
	}
	/** this method will build and add text fields with necessary size and constraints */
	public JTextArea addTextArea(int columns, int rows, Font font, int x, int y, int z, int dimensionx, int dimensiony) {
		JTextArea textArea= new JTextArea(); 
		JScrollPane jsp = new JScrollPane(textArea);
		textArea.setColumns(columns);
		textArea.setRows(rows);
		textArea.setFont(font);
		textArea.setEditable(false);
		jsp.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		textArea.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		textArea.setOpaque(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		c = setConstraints(x,y,z);
		calculator.add(jsp,c);
		
		return textArea;
	}
	/** this method will set your constraints */
	public GridBagConstraints setConstraints(int x, int y, int z ) {
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = z;
		return c;
	}

	@Override
	/** this method has the actions that will be performed on button press */
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == qwordButton){
			lastButtonPressed = "qwordButton";
			if(qwordButton.getText().equals(qwordString)) {
				qwordButton.setText(dwordString);
			}
			else if(qwordButton.getText().equals(dwordString)) {
				qwordButton.setText(wordString);
			}
			else if(qwordButton.getText().equals(wordString)) {
				qwordButton.setText(byteString);
			}
			else if(qwordButton.getText().equals(byteString)) {
				qwordButton.setText(qwordString);
			}
			qWordButtonConvert();
	    }//end of qword button
	    if (e.getSource() == plusMinusButton){
	      if (mainField.getText().contentEquals("0")){
	    	  isNegative = false;
	    	  setConvertFields(mainField.getText());
	      }
	      else if(inputType.equals("hex")) {
	    	  if(!isNegative) {
	    		  beforeNegate =  mainField.getText();
	    		  mainField.setText(negateHex(mainField.getText()));
		    	  isNegative = true;    
	    	  }
	    	  else {
	    		  mainField.setText(beforeNegate);
	    		  isNegative = false;
	    		  setConvertFields(beforeNegate);
	    	  }
	      }
	      else if(inputType.equals("bin")) {
	    	  if(!isNegative) {
	    		  beforeNegate =  mainField.getText();
	    		  mainField.setText(negateBin(binField.getText()));
		    	  isNegative = true;  
	    	  }
	    	  else {
	    		  mainField.setText(beforeNegate);
	    		  isNegative = false;
	    		  setConvertFields(beforeNegate);
	    	  }
	      }
	      else if(inputType.equals("oct")) {
	    	  if(!isNegative) {
	    		  beforeNegate =  mainField.getText();
	    		  mainField.setText(negateOct(mainField.getText()));
		    	  isNegative = true;  
	    	  }
	    	  else {
	    		  mainField.setText(beforeNegate);
	    		  isNegative = false;
	    		  setConvertFields(beforeNegate);
	    	  }
	      }
	      else if(inputType.equals("dec")) {
	    	  if (!isNegative){
	    		  StringBuilder sb = new StringBuilder(mainField.getText());
	    		  sb.insert(0, '-');
	    		  mainField.setText(""+sb);	        
	    		  isNegative = true;
	    		  setConvertFields(mainField.getText());
	    	  }
	    	  else{
	    		  mainField.setText(mainField.getText().replace("-", ""));
	    		  isNegative = false;
	    		  setConvertFields(mainField.getText());
	    	  }
	      }
	      lastButtonPressed = "plusMinusButton";
	    }// end of plusMinusButton
	    if (e.getSource() == hexButton){
	    	setFontSize("hex");
	    	decButton.setForeground(Color.BLACK);
	    	octButton.setForeground(Color.BLACK);
	    	binButton.setForeground(Color.BLACK);
	    	hexButton.setForeground(lightBlue);
	    	mainField.setText(hexField.getText());
	    	if(inputType.equals("hex")== false ) {
	    		calcField.setText(convertCalcFieldtoHex(calcField.getText()));  
	    	}
	    	for (int i = 0; i < 10; i++) {
	    		numPadButtons[i].setEnabled(true);
	    	}
			for(int i=0; i < 6; i++) {
				alphabetPadButtons[i].setEnabled(true);
			}
			
			inputType = "hex";
	    }//end of hex button
	    if (e.getSource() ==  decButton){
	    	setFontSize("dec");
	    	//lastButtonPressed = "decButton";
	    	hexButton.setForeground(Color.BLACK);
	    	octButton.setForeground(Color.BLACK);
	    	binButton.setForeground(Color.BLACK);
	    	decButton.setForeground(lightBlue);
	    	mainField.setText(decField.getText());
	    	if(inputType.equals("dec")== false ) {
		    	  calcField.setText(convertCalcFieldtoDec(calcField.getText()));  
		      }
	    	for (int i = 0; i < 10; i++) {
	    		numPadButtons[i].setEnabled(true);
	    	}
			for(int i=0; i < 6; i++) {
				alphabetPadButtons[i].setEnabled(false);
			}
			
			inputType = "dec";
	    }//end of dec button
	    if (e.getSource() == octButton){
	    	setFontSize("oct");
	    	//lastButtonPressed = "octButton";
	    	hexButton.setForeground(Color.BLACK);
	    	decButton.setForeground(Color.BLACK);
	    	binButton.setForeground(Color.BLACK);
	    	octButton.setForeground(lightBlue);
	    	mainField.setText(octField.getText());
	    	if(inputType.equals("oct")== false ) {
	    		calcField.setText(convertCalcFieldtoOct(calcField.getText()));  
	    	}
	    	for(int i=0; i < 6; i++) {
	    		alphabetPadButtons[i].setEnabled(false);
	    	}
	    	for (int i = 0; i < 10; i++) {
	    		if ((i == 8) || (i == 9)) {
	    			numPadButtons[i].setEnabled(false);
	    		} else {
	    			numPadButtons[i].setEnabled(true);
	    		}
	    	}
			
			inputType = "oct";
	    }//end of oct button
	    if (e.getSource() == binButton){
	    	setFontSize("bin");
	    	//lastButtonPressed = "binButton";
	    	hexButton.setForeground(Color.BLACK);
	    	octButton.setForeground(Color.BLACK);
	    	decButton.setForeground(Color.BLACK);
	    	binButton.setForeground(lightBlue);
	    	mainField.setText(binField.getText());
	    	if(inputType.equals("bin")== false ) {
	    		calcField.setText(convertCalcFieldtoBin(calcField.getText()));  
	    	}
	    	for (int i = 0; i < 10; i++) {
	    		if ((i == 0) || (i == 1)) {
	    			numPadButtons[i].setEnabled(true);
	    		} else {
	    			numPadButtons[i].setEnabled(false);
	    		}
	    	}
	    	for(int i=0; i < 6; i++) {
	    		alphabetPadButtons[i].setEnabled(false);
	    	}
			
			inputType = "bin";
	    }//end of bin button
	    if (e.getSource() == clearAllButton){
	    	calcField.setText("");
	    	mainField.setText("0");
	    	decField.setText("0");
	    	binField.setText("0");
	    	hexField.setText("0");
	    	octField.setText("0");
	    	mainField.setFont(font1);
	    	parenCounter = 0;
	    	isNegative = false;
	    }//end of clear all button
	    if (e.getSource() == clearEntryButton){
	    	mainField.setText("0");
	    	decField.setText("0");
	    	binField.setText("0");
	    	hexField.setText("0");
	    	octField.setText("0");
	    	mainField.setFont(font1);
	    	isNegative = false;
	    }//end of clear entry button
	    if (e.getSource() == backspaceButton){
	    	if(isNegative && mainField.getText().length() == 2) {
	    		mainField.setText("0");
	    		binField.setText("0");
	    		hexField.setText("0");
	    		decField.setText("0");
	    		octField.setText("0");
	    		isNegative = false;
	    	}
	    	if (mainField.getText().length() > 1) {
	    		mainField.setText(mainField.getText().substring(0, mainField.getText().length() - 1));
		    	setConvertFields(mainField.getText());
	    	} 
	    	else {
	    		mainField.setText("0");
	    		binField.setText("0");
	    		hexField.setText("0");
	    		decField.setText("0");
	    		octField.setText("0");
	    	}
	    }//end of backspace button
	    if ((e.getSource() == minusButton)){ 
	    	if (lastButtonPressed == "operatorButton" || lastButtonPressed == "modButton") {//you can change the operator before hitting the next operand
	    		
	    		if(lastButtonPressed == "operatorButton") {
	    			calcField.setText(calcField.getText().substring(0, calcField.getText().length() - 3));
	    		}
	    		else {
	    			calcField.setText(calcField.getText().substring(0, calcField.getText().length() - 5));
	    		}
	    		calcField.setText(calcField.getText() + " " + minus + " ");
  		  	}
	    	else {
	    		calcField.setText(calcField.getText() + mainField.getText() + " " + minus + " ");
	    	}
	    	if (mainField.getText().length() > mainFieldSize){
	    		mainField.setText("0");
	    		mainField.setFont(font1);
	    	}
	    	isNegative = false;
	    	operatorPressed = true;
	    	lastButtonPressed = "operatorButton";
	    }//end of minus button
	    if ((e.getSource() == plusButton)){
	    	if (lastButtonPressed == "operatorButton" || lastButtonPressed == "modButton") { //you can change the operator before hitting the next operand
	    		if(lastButtonPressed == "operatorButton") {
	    			calcField.setText(calcField.getText().substring(0, calcField.getText().length() - 3));
	    		}
	    		else {
	    			calcField.setText(calcField.getText().substring(0, calcField.getText().length() - 5));
	    		}
	    		calcField.setText(calcField.getText() + " + ");
  		  	}
	    	else {
	    		calcField.setText(calcField.getText() + mainField.getText() + " + ");
	    	}
	    	if (mainField.getText().length() > mainFieldSize){
	    		mainField.setText("0");
	    		mainField.setFont(font1);
	    	}
	    	isNegative = false;
	    	operatorPressed = true;
	    	lastButtonPressed = "operatorButton";
	    }//end of plus button
	    if ((e.getSource() == divideButton)){
	    	if (lastButtonPressed == "operatorButton" || lastButtonPressed == "modButton") { //you can change the operator before hitting the next operand
	    		if(lastButtonPressed == "operatorButton") {
	    			calcField.setText(calcField.getText().substring(0, calcField.getText().length() - 3));
	    		}
	    		else {
	    			calcField.setText(calcField.getText().substring(0, calcField.getText().length() - 5));
	    		}
	    		calcField.setText(calcField.getText() + " "+ divide + " ");
  		  	}
	    	else {
	    		calcField.setText(calcField.getText() + mainField.getText() + " " + divide + " ");
	    	}
	    	if (mainField.getText().length() > mainFieldSize){
	    		mainField.setText("0");
	    		mainField.setFont(font1);
	    	}
	    	isNegative = false;
	    	operatorPressed = true;
	    	lastButtonPressed = "operatorButton";
	    }//end of divide button
	    if ((e.getSource() == multiplyButton)){
	    	if (lastButtonPressed == "operatorButton" || lastButtonPressed == "modButton" ) { //you can change the operator before hitting the next operand
	    		if(lastButtonPressed == "operatorButton") {
	    			calcField.setText(calcField.getText().substring(0, calcField.getText().length() - 3));
	    		}
	    		else {
	    			calcField.setText(calcField.getText().substring(0, calcField.getText().length() - 5));
	    		}
	    		calcField.setText(calcField.getText() + " "+ multiply + " ");
  		  	}
	    	else {
	    		calcField.setText(calcField.getText() + mainField.getText() + " " + multiply + " ");
	    	}
	    	if (mainField.getText().length() > mainFieldSize){
	    		mainField.setText("0");
	    		mainField.setFont(font1);
	    	}
	    	isNegative = false;
	    	operatorPressed = true;
	    	lastButtonPressed = "operatorButton";
	    }//end of multiply button
	    if ((e.getSource() == modButton)){
	    	if (lastButtonPressed == "operatorButton") { //you can change the operator before hitting the next operand
	    		calcField.setText(calcField.getText().substring(0, calcField.getText().length() - 3));
	    		calcField.setText(calcField.getText() + " Mod ");
  		  	}
	    	else {
	    		calcField.setText(calcField.getText() + mainField.getText() + " Mod ");
	    	}
	    	//calcField.setText(calcField.getText() + mainField.getText() + " Mod ");
	    	if (mainField.getText().length() > mainFieldSize){
	    		mainField.setText("0");
	    		mainField.setFont(new Font("Segoe UI", 0, 30));
	    	}
	    	isNegative = false;
	    	operatorPressed = true;
	    	lastButtonPressed = "modButton";
	    }//end of mod button
	    if (e.getSource() == equalsButton){
	    	lastButtonPressed = "equalsButton";
	    	if (operatorPressed){
	    		calcField.setText(calcField.getText() + mainField.getText());
	    		equation = calcField.getText();
	    		equation = equation.replace(",","");
	         
	    		if(inputType != "dec") {
	    			equation = convertCalcFieldtoDec(equation);
	    		}
	        
	    		mainField.setText("0");// go back here
	    		
	    		calcField.setText("");
	    		parenCounter = 0;
	    		
	    		if(inputType.equals("bin")) {
	    			mainField.setText((decToBin(parseMath(equation))));
	    			decField.setText(""+(parseMath(equation)));
	    			hexField.setText(decToHex(parseMath(equation))); 
	    			octField.setText(decToOct(parseMath(equation))); 
	    			binField.setText(setLeadingZeros(mainField.getText()));
	    		}
	    		else if(inputType.equals("oct")) {
	    			mainField.setText(decToOct(parseMath(equation)));
	    			decField.setText(""+(parseMath(equation)));
	    			hexField.setText(decToHex(parseMath(equation))); 
	    			octField.setText(mainField.getText());
	    			binField.setText(decToBin(parseMath(equation))); 
	    		}
	    		else if(inputType.equals("hex")) {
	    			mainField.setText((decToHex(parseMath(equation)).toUpperCase()));
	    			decField.setText(""+(parseMath(equation)));
	    			hexField.setText(mainField.getText()); 
	    			octField.setText(decToOct(parseMath(equation)));
	    			binField.setText(decToBin(parseMath(equation)));
	    		}
	    		else {
	    			mainField.setText(""+parseMath(equation));
	    			setConvertFields(""+parseMath(equation));
	    			
	    		}

	    		operatorPressed = false;
	        
	    		if (mainField.getText().length() > 16) {
	    			if (mainField.getText().length() > 20) {
	    				mainField.setFont(new Font("Segoe UI", 0, 20));
	    			}
	    			else {
	    				mainField.setFont(new Font("Segoe UI", 0, 28));
	    			}
	    		}
	    		if(mainField.getText().contains("-")) {
	    			isNegative = true;
	    		}
	    	}
	    }//END OF EQUALS BUTTON
	    if (e.getSource() == openParenButton){
	    	openParenButton.setText(subScript[parenCounter+1]);
	    	parenCounter++;
	    	calcField.setText(calcField.getText() + " ( ");
	    }//end of openParenButton
	    if (e.getSource() == closeParenButton){
	    	if(parenCounter!=0) {
	    		openParenButton.setText(subScript[parenCounter-1]);
	    		parenCounter--;
	    		calcField.setText(calcField.getText() + mainField.getText() + " ) ");
	    		mainField.setText("");
	    	}
	    }//end of closed paren button

	    /** loop for numpad actions*/
	    for (int i = 0; i < 10; i++) {
	    	if(mainField.getText().length() == mainFieldSize && lastButtonPressed == "operatorButton") {
	    		mainField.setText("0");
	    	}
	    	
	    	setMainFieldSize(i);
	    	
	    	if ((mainField.getText().length() < mainFieldSize) || (lastButtonPressed == "equalsButton")) {
	    	  
	    		if (e.getSource() == numPadButtons[i]){
	    			if (lastButtonPressed == "operatorButton" || lastButtonPressed == "modButton" ) {
	    				mainField.setText("0"); 
	    			}
	    			if (lastButtonPressed == "equalsButton" ){
	    				mainField.setText("0");// go back here
	    				
	    				lastButtonPressed = "numpadButtons";
	    			}
	    			if ((mainField.getText() == "") || (mainField.getText().equals("0"))){
	    				lastButtonPressed = "numpadButtons";
	    				mainField.setText("0"); 
	    				mainField.setText(""+ i);
	    			}
	    			else{
	    				lastButtonPressed = "numpadButtons";
	    				mainField.setText(mainField.getText() + i);
	    			}
	    			setConvertFields(mainField.getText());
	    			setFontSize("basic");
	    		}
	      }
	    }//end of numpad loop
	    /** loop for alphabetPad actions*/
	    for (int i = 0; i < 6; i++) {
	    	setMainFieldSize(i);
	    	if ((mainField.getText().length() < mainFieldSize) || (lastButtonPressed == "equalsButton")) {
	    		if (e.getSource() == alphabetPadButtons[i]){
	    			setFontSize("basic");
	    			if (lastButtonPressed == "operatorButton" || lastButtonPressed == "modButton" ) {
	    				mainField.setText("0");
	    			
	    			}
	    			if (lastButtonPressed == "equalsButton"){
	    				mainField.setText("0");
	    				lastButtonPressed = "alphabetPadButtons";
	    			}
	    			if ((mainField.getText() == "") || (mainField.getText().equals("0"))){
	    				lastButtonPressed = "alphabetPadButtons";
	    				mainField.setText("0"); 
	    				mainField.setText(""+ alphabetArray[i]);
	    			}
	    			else{
	    				lastButtonPressed = "alphabetPadButtons";
	    				mainField.setText(mainField.getText() + alphabetArray[i]);
	    			}
	    			setConvertFields(mainField.getText());
		        }
	    	}
	    }//end of alphabet loop
	  }
	/** set textfields for conversion */
	public void setConvertFields(String numberString) { 
		if(numberString.equals("0")){
			decField.setText("0"); 
			binField.setText("0"); 
			hexField.setText("0");
			octField.setText("0");
		}
		else {
		if(inputType == "hex") {
			decField.setText(""+ hexToDec(numberString,numberString.length())); 
			binField.setText(setLeadingZeros(decToBin(hexToDec(numberString,numberString.length())))); 
			hexField.setText(numberString);
			octField.setText(decToOct(hexToDec(numberString,numberString.length()))); 
		}
		else if(inputType == "dec") {
			long numberLong = Long.parseLong(numberString.replaceAll(",", ""));
			decField.setText(numberString);
			binField.setText(setLeadingZeros(decToBin(numberLong)));
			hexField.setText((decToHex(numberLong).toUpperCase()));
			octField.setText(decToOct(numberLong));
		}
		else if(inputType == "oct") {
			decField.setText(""+ octToDec(numberString, numberString.length()));
			binField.setText(setLeadingZeros(decToBin(octToDec(numberString, numberString.length()))));
			hexField.setText(decToHex(octToDec(numberString, numberString.length())).toUpperCase());
			octField.setText(numberString); 			
		}
		else if(inputType == "bin") {
			decField.setText(""+ binToDec(numberString,numberString.length())); 
			binField.setText(setLeadingZeros(numberString));
			hexField.setText(decToHex(binToDec(numberString,numberString.length())).toUpperCase()); 
			octField.setText(decToOct(binToDec(numberString,numberString.length()))); 
		}
		if(binField.getText().charAt(0)== '1' && lastButtonPressed == "qwordButton"||lastButtonPressed == "equalsButtons") {
			String reverseBinaryString = reverseTwosCompliment(binField.getText());
			decField.setText(""+ (binToDec(reverseBinaryString,reverseBinaryString.length())*-1));
			if(inputType == "dec") {
				mainField.setText(decField.getText());
			}
		}
		}
	}//end of setConvertFields
	
	/**converts the calcfield to dec if it is hex, oct, or bin, so that it can be calculated*/
	public String convertCalcFieldtoDec(String equation) {
		String convertedCalcField = "";
		String token = "";
		String operator= "";
		Long numberLong;
		StringTokenizer st = new StringTokenizer(equation);
		StringBuilder sb = new StringBuilder();
		
		while(st.hasMoreTokens()) {
			token = st.nextToken();
			
			if(token.equals("+") ==false  
					&& token.equals(minus)==false  
					&& token.equals(divide)==false  
					&& token.equals(multiply)==false  
					&& token.equals("Mod")==false 
					&& token.equals("(")==false
					&& token.equals(")")==false) {
				
				
				if(inputType == "bin") {
					numberLong = binToDec(token, token.length());
					sb.append(numberLong);
				}
				else if(inputType == "oct") {
					numberLong = octToDec(token, token.length());
					sb.append(numberLong);
				}
				else if(inputType == "hex") {
					numberLong = hexToDec(token,token.length());
					sb.append(numberLong);
				}
			}
			else {
				operator = token;
				sb.append(" "+operator+" ");
			}
		}
		
		convertedCalcField = ""+ sb;
		
		return convertedCalcField;
	}
	/**converts the calcfield to hex if it is hex, oct, or bin, so that it can be calculated*/
	public String convertCalcFieldtoHex(String equation) {
		String convertedCalcField = "";
		String token = "";
		String operator= "";
		String newToken = "";
		StringTokenizer st = new StringTokenizer(equation);
		StringBuilder sb = new StringBuilder();
		
		while(st.hasMoreTokens()) {
			token = st.nextToken();
			
			if(token.equals("+") ==false  
					&& token.equals(minus)==false  
					&& token.equals(divide)==false  
					&& token.equals(multiply)==false  
					&& token.equals("Mod")==false 
					&& token.equals("(")==false
					&& token.equals(")")==false) {
				
				
				if(inputType == "bin") {
					newToken = decToHex(binToDec(token, token.length()));
					sb.append(newToken.toUpperCase());
				}
				else if(inputType == "oct") {
					newToken = decToHex(octToDec(token, token.length()));
					sb.append(newToken.toUpperCase());
				}
				else if(inputType == "dec") {
					newToken = decToHex(Long.parseLong(token));
					sb.append(newToken.toUpperCase());
				}
			}
			else {
				operator = token;
				sb.append(" "+operator+" ");
			}
		}
		convertedCalcField = ""+ sb;
		
		return convertedCalcField;

	}
	public String convertCalcFieldtoBin(String equation) {
		String convertedCalcField = "";
		String token = "";
		String operator= "";
		String newToken = "";
		StringTokenizer st = new StringTokenizer(equation);
		StringBuilder sb = new StringBuilder();
		
		while(st.hasMoreTokens()) {
			token = st.nextToken();
			
			if(token.equals("+") ==false  
					&& token.equals(minus)==false  
					&& token.equals(divide)==false  
					&& token.equals(multiply)==false  
					&& token.equals("Mod")==false
					&& token.equals("(")==false
					&& token.equals(")")==false) {
				
				
				if(inputType == "hex") {
					newToken = decToBin(hexToDec(token, token.length()));
					sb.append(newToken);
				}
				else if(inputType == "oct") {
					newToken = decToBin(octToDec(token, token.length()));
					sb.append(newToken);
				}
				else if(inputType == "dec") {
					newToken = decToBin(Long.parseLong(token));
					sb.append(newToken);
				}
			}
			else {
				operator = token;
				sb.append(" "+operator+" ");
			}
		}
		convertedCalcField = ""+ sb;
		
		return convertedCalcField;

	}
	public String convertCalcFieldtoOct(String equation) {
		String convertedCalcField = "";
		String token = "";
		String operator= "";
		String newToken = "";
		StringTokenizer st = new StringTokenizer(equation);
		StringBuilder sb = new StringBuilder();
		
		while(st.hasMoreTokens()) {
			token = st.nextToken();
			
			if(token.equals("+") ==false  
					&& token.equals(minus)==false  
					&& token.equals(divide)==false  
					&& token.equals(multiply)==false  
					&& token.equals("Mod")==false 
					&& token.equals("(")==false
					&& token.equals(")")==false) {
				
				
				if(inputType == "hex") {
					newToken = decToOct(hexToDec(token, token.length()));
					sb.append(newToken);
				}
				else if(inputType == "bin") {
					newToken = decToOct(binToDec(token, token.length()));
					sb.append(newToken);
				}
				else if(inputType == "dec") {
					newToken = decToOct(Long.parseLong(token));
					sb.append(newToken);
				}
			}
			else {
				operator = token;
				sb.append(" "+operator+" ");
			}
		}
		convertedCalcField = ""+ sb;
		
		return convertedCalcField;
	}
	
	public Long parseMath(String equation) {
	
		char[] tokens = equation.toCharArray(); 

        // Stack for numbers: 'values' 
       Stack<Long> values = new Stack<Long>(); 
 
       // Stack for Operators: 'ops' 
       Stack<Character> ops = new Stack<Character>(); 
 
       for (int i = 0; i < tokens.length; i++) 
       { 
           // skip space
           if (tokens[i] == ' ') 
               continue; 
           // Current token is a number, push it to stack for numbers 
           if (tokens[i] == '-' || (tokens[i] >= '0' && tokens[i] <= '9') ) 
           { 
               StringBuffer sbuf = new StringBuffer(); 
               // There may be more than one digits in number 
               while (i < tokens.length &&  (tokens[i] == '-' || tokens[i] >= '0' && tokens[i] <= '9')) 
                   sbuf.append(tokens[i++]); 
               values.push(Long.parseLong(sbuf.toString())); 
           } 
 
           // Current token is an opening paren, push it to 'ops' 
           else if (tokens[i] == '(') 
               ops.push(tokens[i]); 

           // Closing paren encountered, solve entire parenthesis
           else if (tokens[i] == ')') { 
               while (ops.peek() != '(') 
                 values.push(applyOp(ops.pop(), values.pop(), values.pop())); 
               ops.pop(); 
           } 
           // Current token is an operator. 
           else if (tokens[i] == '+' || tokens[i] == minusChar || 
                    tokens[i] == multiplyChar || tokens[i] == divideChar ||tokens[i] == 'M') { 
               // While top of 'ops' has same or greater precedence to current 
               // token, which is an operator. Apply operator on top of 'ops' 
               // to top two elements in values stack 
               while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) 
                 values.push(applyOp(ops.pop(), values.pop(), values.pop())); 
 
               // Push current token to 'ops'. 
               ops.push(tokens[i]); 
               if(tokens[i] == 'M'){
            	   i+=2;
               }
           } 
       } 
       // solve expression
       while (!ops.empty()) 
           values.push(applyOp(ops.pop(), values.pop(), values.pop())); 
       return values.pop();
    } 
	/** this function will return true if an operator has precendence over the other and false if not*/
    public static boolean hasPrecedence(char op1, char op2) { 
        if (op2 == '(' || op2 == ')') {
            return false; 
        }
        if ((op1 == multiplyChar || op1 == divideChar || op1 == 'M') && (op2 == '+' || op2 == minusChar)) {
            return false; 
        }
        else {
            return true; 
        }
    } 
    /** this function will perform the operations for parseMath*/
    public long applyOp(char op, Long long1, Long long2) { 
    	switch (op) { 
        	case '+': 
        		return long2 + long1; 
        	case minusChar: 
        		return long2 - long1; 
        	case multiplyChar: 
        		return long2 * long1; 
        	case divideChar: 
        		if (long1 == 0) { 
        			mainField.setFont(new Font("Segoe UI", 0, 28));
        			mainField.setText("Cannot divide by zero");
        			hexField.setText("0");
        			decField.setText("0");
        			octField.setText("0");
        			binField.setText("0");
        			throw new UnsupportedOperationException("Cannot divide by zero");
        		}
        		else {
        			return long2 / long1; 
        		}
        	case 'M':
        		if (long1 == 0) { 
        			mainField.setFont(new Font("Segoe UI", 0, 28));
        			mainField.setText("Result is undefined");
        			hexField.setText("0");
        			decField.setText("0");
        			octField.setText("0");
        			binField.setText("0");
        			throw new UnsupportedOperationException("Cannot mod by zero");
        		}
        		else {
        			return long2 % long1;
        		}
        } 
        return 0; 
    }//end of apply op
    /** this function will convert a hex number to a dec number*/
    public static long hexToDec(String value, int numberOfDigits) {
    	long hex2DecIntValue = 0;
    	int hex2Decindex = 0;
    	
    	while(numberOfDigits != 0) {
    		if(value.charAt(numberOfDigits-1) == 'A' || value.charAt(numberOfDigits-1) == 'a' ) {
    			hex2DecIntValue += 10 * Math.pow(16,hex2Decindex); // simply do the reverse of dec to hex: multiply the digits by 16^(index)
    		}
    			
    		else if(value.charAt(numberOfDigits-1) == 'B' || value.charAt(numberOfDigits-1) == 'b') {
    			hex2DecIntValue += 11 * Math.pow(16,hex2Decindex);
    		}
    			
    		else if(value.charAt(numberOfDigits-1) == 'C'|| value.charAt(numberOfDigits-1) == 'c') {
    			hex2DecIntValue += 12 * Math.pow(16,hex2Decindex);
    		}
    			
    		else if(value.charAt(numberOfDigits-1) == 'D'|| value.charAt(numberOfDigits-1) == 'd') {
    			hex2DecIntValue += 13* Math.pow(16,hex2Decindex);
    		}
    			
    		else if(value.charAt(numberOfDigits-1) == 'E'|| value.charAt(numberOfDigits-1) == 'e') {
    			hex2DecIntValue += 14 * Math.pow(16,hex2Decindex);
    		}
    			
    		else if(value.charAt(numberOfDigits-1) == 'F'|| value.charAt(numberOfDigits-1) == 'f') {
    			hex2DecIntValue += 15 * Math.pow(16,hex2Decindex);
    		}
    			
    		else {
    			hex2DecIntValue += Character.getNumericValue(value.charAt(numberOfDigits-1))* Math.pow(16,hex2Decindex);
    		}
    			
    		
    		numberOfDigits--; // decrease the number of digits as a counter 
    		hex2Decindex ++ ; //increase the index for the pow 16
    	}

		return hex2DecIntValue  ;
	}
    /** this function will convert a decimal number to a hexadecimal number*/
    public static String decToHex(long value) {
		String hex = "";
		boolean negative = false;
		if(value < 0) {
    		value *= -1;
    		negative = true;
    	}
		while(value != 0) { 
			
			//10-15 are represented by A - F in hexidecimal
			if(value%16 == 10) {
				hex = 'A' + hex;
			}
			else if(value%16 == 11) {
				hex = 'B' + hex;
			}
			else if(value%16 == 12) {
				hex = 'C' + hex;
			}
			else if(value%16 == 13) {
				hex = 'D' + hex;
			}
			else if(value%16 == 14) {
				hex = 'E' + hex;
			}
			else if(value%16 == 15) {
				hex = 'F' + hex;
			}
			else {
				hex = value%16 + hex;//hex is base 16
			}
		
			value/=16;
		}
		if(negative) {
			hex = getSixteensCompliment(hex);
		}
		return hex;
	}
    /** this function will convert a binary number to a decimal number*/
    public static long binToDec(String value, int numberOfDigits) {
    	long bin2DecValue = 0;
    	int bin2Decindex = 0;
    	while(numberOfDigits != 0) {
    		if(value.charAt(numberOfDigits-1) == '0') {} // do nothing
    		
    		else if(value.charAt(numberOfDigits-1) == '1') {
    			bin2DecValue += Math.pow(2,bin2Decindex); // simply do the reverse of dec to bin: multiply the digits by 2^(index)
    		}
    		
    		numberOfDigits--;
    		bin2Decindex ++;
    	}
		
		return bin2DecValue;
	}
    /** this function will convert a decimal number to a binary number*/
    public static String decToBin(long value) {
    	String binary = "";
    	boolean negative = false;
    	if(value < 0) {
    		value *= -1;
    		negative = true;
    	}
		while(value != 0) {
			binary = value % 2 + binary; //binary = remainder, either 1 or 2, binary is base 2 so you mod 2
			value /= 2; //divide by 2 in order to move on
		}
		if(negative) {
			binary = getTwosCompliment(setLeadingZeros(binary));
		}
		return binary;
		
	}
    /** this function will convert an octal number to a decimal number*/
    public static long octToDec(String value, int numberOfDigits) {
    	long oct2DecValue = 0;
    	int oct2Decindex = 0;
    	
    	while(numberOfDigits != 0) {
    		oct2DecValue += Character.getNumericValue(value.charAt(numberOfDigits-1))* Math.pow(8,oct2Decindex);
   
    		numberOfDigits--;
    		oct2Decindex ++;
    	}
    	return oct2DecValue;
    }
    /** this function will convert a decimal number to an octal number*/
    public static String decToOct(long value) {
    	String octal = "";
    	boolean negative = false;
    	if(value < 0) {
    		value *= -1;
    		negative = true;
    	}
		while(value != 0) {
			octal = value % 8 + octal; //binary = remainder, either 1 or 2, binary is base 2 so you mod 2
			value /= 8; //divide by 2 in order to move on
		}
		if(negative) {
			octal = getEightsCompliment(octal);
		}
		return octal;
    }
    /** this function will set all the fields when you negate a hex number*/
    public String negateHex(String numberString) {
    	long numberLong = 0-hexToDec(numberString, numberString.length());
    	decField.setText(""+(numberLong));
		binField.setText(decToBin(numberLong)); 
		octField.setText(decToOct(numberLong));
    	String negatedHex = getSixteensCompliment(numberString);
    	hexField.setText(negatedHex);
    	return negatedHex;
    }
    /** this function will set all the fields when you negate a binary number*/
    public String negateBin(String numberString) {
    	long numberLong = 0-binToDec(numberString, numberString.length());
    	decField.setText(""+(numberLong));
		hexField.setText(decToHex(numberLong)); 
		octField.setText(decToOct(numberLong));
    	String negatedBin = getTwosCompliment(numberString);
    	binField.setText(negatedBin);
    	return negatedBin;
    }
    /** this function will set all the fields when you negate an oct number*/
    public String negateOct(String numberString) {
    	long numberLong = 0 - octToDec(numberString, numberString.length());
    	decField.setText(""+(numberLong));
		hexField.setText(decToHex(numberLong)); 
		binField.setText(decToBin(numberLong));
    	String negatedOct = getEightsCompliment(numberString);
    	octField.setText(negatedOct);
    	return negatedOct;
    }
    /** this function will give the signed representation of the binary String*/
    public static String getTwosCompliment(String binaryString) {
    	int length = binaryString.length();
    	int i;
        for (i = length-1 ; i >= 0 ; i--) { //get rightmost 1
        	if (binaryString.charAt(i) == '1') {
        		break;
        	}
        }

        if (i == -1) {      // If there exists no '1' concatenate 1 at the starting of string 
            return '1' + binaryString; 
        }
        StringBuilder sb = new StringBuilder(binaryString);

        for (int k = i-1 ; k >= 0; k--)  { 
            if (binaryString.charAt(k) == '1') {
            	sb.setCharAt(k, '0'); 
            }
            else {
            	sb.setCharAt(k, '1');
            }
        } 
        int difference = 64 - (binaryString.length() % 4);
        for(int K = difference; K > 0; K-- ) {
			sb.insert(0, '1');
		}
        
        binaryString = ""+sb;
        
    	return binaryString;
    }
    /** this function will add give the signed representation of the hex string*/
    public static String getSixteensCompliment(String hexString) {
    	char[] hexArray =  hexString.toCharArray();
    	int j = 0; //j is used as a counter for the for loop and for the while loop
    	for(j = 0; j < hexString.length(); j++) {
    		switch (hexArray[j]) {
    			case '0':  //F-0
    				hexArray[j] = 'F';
    				break;
    			case '1': //F-1
    				hexArray[j] = 'E';
    				break;
    			case '2': //F-2
    				hexArray[j] = 'D';
    				break;
    			case '3': 
    				hexArray[j] = 'C';
    				break;
    			case '4': 
    				hexArray[j] = 'B';
    				break;
    			case '5': 
    				hexArray[j] = 'A';
    				break;
    			case '6': 
    				hexArray[j] = '9';
    				break;
    			case '7': 
    				hexArray[j] = '8';
    				break;
    			case '8': 
    				hexArray[j] = '7';
    				break;
    			case '9': 
    				hexArray[j] = '6';
    				break;
    			case 'A': 
    				hexArray[j] = '5';
    				break;
    			case 'B': 
    				hexArray[j] = '4';
    				break;
    			case 'C': 
    				hexArray[j] = '3';
    				break;
    			case 'D': 
    				hexArray[j] = '2';
    				break;
    			case 'E': 
    				hexArray[j] = '1';
    				break;
    			case 'F': 
    				hexArray[j] = '0';
    				break;
    		}
    	}
    	boolean isF = true;
		j = hexString.length()-1;
		//add one after doing f complement
		while(isF){
			switch (hexArray[j]) {
				case '0': 
					hexArray[j] = '1';
					isF = false;
					break;
				case '1': 
					hexArray[j] = '2';
					isF = false;
					break;
				case '2': 
					hexArray[j] = '3';
					isF = false;
					break;
				case '3': 
					hexArray[j] = '4';
					isF = false;
					break;
				case '4': 
					hexArray[j] = '5';
					isF = false;
					break;
				case '5': 
					hexArray[j] = '6';
					isF = false;
					break;
				case '6': 
					hexArray[j] = '7';
					isF = false;
					break;
				case '7': 
					hexArray[j] = '8';
					isF = false;
					break;
				case '8': 
					hexArray[j] = '9';
					isF = false;
					break;
				case '9': 
					hexArray[j] = 'A';
					isF = false;
					break;
				case 'A': 
					hexArray[j] = 'B';
					isF = false;
					break;
				case 'B': 
					hexArray[j] = 'C';
					isF = false;
					break;
				case 'C': 
					hexArray[j] = 'D';
					isF = false;
					break;
				case 'D': 
					hexArray[j] = 'E';
					isF = false;
					break;
				case 'E': 
					hexArray[j] = 'F';
					isF = false;
					break;
				case 'F': 
					hexArray[j] = '0';
					isF = true;
					j--;
					break;
			}
		}
		hexString = new String(hexArray);
    	int difference = 16 - (hexString.length() % 4);
    	StringBuilder sb = new StringBuilder(hexString);
		for(int i = difference; i > 0; i-- ) {
			sb.insert(0, 'F');
		}
		hexString = ""+sb;
    	return hexString;
    }
	public static String getEightsCompliment(String octString) {
	    char[] octArray =  octString.toCharArray();
	   	int j = 0; //j is used as a counter for the for loop and for the while loop
	   	for(j = 0; j < octString.length(); j++) {
	    	switch (octArray[j]) {
	  			case '0':  //7-0
	   				octArray[j] = '7';
	   				break;
	   			case '1': //7-1
	   				octArray[j] = '6';
	   				break;
	   			case '2': //7-2
	   				octArray[j] = '5';
	   				break;
	   			case '3': 
	   				octArray[j] = '4';
	   				break;
	   			case '4': 
	   				octArray[j] = '3';
	   				break;
	   			case '5': 
	   				octArray[j] = '2';
	    			break;
	    		case '6': 
	    			octArray[j] = '1';
	    			break;
	    		case '7': 
	    			octArray[j] = '0';
	    			break;
	    			
	    	}
	   	}
	   	boolean is7 = true;
		j = octString.length()-1;
		//add one after doing f complement
		while(is7){
			switch (octArray[j]) {
				case '0': 
					octArray[j] = '1';
					is7 = false;
					break;
				case '1': 
					octArray[j] = '2';
					is7 = false;
					break;
				case '2': 
					octArray[j] = '3';
					is7 = false;
					break;
				case '3': 
					octArray[j] = '4';
					is7 = false;
					break;
				case '4': 
					octArray[j] = '5';
					is7 = false;
					break;
				case '5': 
					octArray[j] = '6';
					is7 = false;
					break;
				case '6': 
					octArray[j] = '7';
					is7 = false;
					break;
				case '7': 
					octArray[j] = '0';
					is7 = true;
					j--;
					break;
					
			}
		}
		
		octString = new String(octArray);
		int difference = 22 - (octString.length() % 3);
		StringBuilder sb = new StringBuilder(octString);
		for(int i = difference; i > 0; i-- ) {
			sb.insert(0, '7');
		}
		sb.insert(0, '1');
		octString = ""+sb;
		return octString;
    }
    
    
    /** this function will add leading zeros to the binary String*/
    public static String setLeadingZeros(String binaryString) {
    	if(binaryString.length() %4 != 0) { 
			int difference = 4 - (binaryString.length() % 4); //the windows calculator shows bits in groups of 4, so if you have 2 1's it will put 2 0's
			StringBuilder sb = new StringBuilder(binaryString);
			for(int i = difference; i > 0; i-- ) {
				sb.insert(0, '0');
			}
			binaryString = ""+sb;
		}
    	return binaryString;
    }
    
    /** this method will set font size for mainField depending on how many characters are in it*/
    public void setFontSize(String fieldType) {
    	if(fieldType=="basic") {
    		if (mainField.getText().length() > 14) {
    			mainField.setFont(new Font("Segoe UI", 0, 28));
    		}
    		if (mainField.getText().length() > 16) {
    			mainField.setFont(new Font("Segoe UI", 0, 25));
    		}
    		if (mainField.getText().length() > 18) {
				mainField.setFont(new Font("Segoe UI", 0, 22));
    		}
    		if (mainField.getText().length() > 20) {
				mainField.setFont(new Font("Segoe UI", 0, 19));
    		}
    		if (mainField.getText().length() > 28) {
				mainField.setFont(new Font("Segoe UI", 0, 14));
    		}
    	}
    	else if (fieldType == "bin") {
    		if (binField.getText().length() > 14) {
    			mainField.setFont(new Font("Segoe UI", 0, 28));
    		}
    		if (binField.getText().length() > 16) {
    			mainField.setFont(new Font("Segoe UI", 0, 25));
    		}
    		if (binField.getText().length() > 18) {
				mainField.setFont(new Font("Segoe UI", 0, 22));
    		}
    		if (binField.getText().length() > 20) {
				mainField.setFont(new Font("Segoe UI", 0, 19));
    		}
    		if (binField.getText().length() > 28) {
				mainField.setFont(new Font("Segoe UI", 0, 14));
    		}
    		if (binField.getText().length() > 32) {
				mainField.setFont(new Font("Segoe UI", 0, 10));
    		}
    		else {
    			mainField.setFont(new Font("Segoe UI", 0, 31));
    		}
    	}
    	else if (fieldType == "hex") {
    		if (hexField.getText().length() > 14) {
    			mainField.setFont(new Font("Segoe UI", 0, 28));
    		}
    		if (hexField.getText().length() > 16) {
    			mainField.setFont(new Font("Segoe UI", 0, 25));
    		}
    		if (hexField.getText().length() > 18) {
				mainField.setFont(new Font("Segoe UI", 0, 22));
    		}
    		if (hexField.getText().length() > 20) {
				mainField.setFont(new Font("Segoe UI", 0, 19));
    		}
    		if (hexField.getText().length() > 28) {
				mainField.setFont(new Font("Segoe UI", 0, 14));
    		}
    		if (hexField.getText().length() > 32) {
				mainField.setFont(new Font("Segoe UI", 0, 9));
    		}
    		else {
    			mainField.setFont(new Font("Segoe UI", 0, 31));
    		}
    	}
    	else if (fieldType == "dec") {
    		if (decField.getText().length() > 14) {
    			mainField.setFont(new Font("Segoe UI", 0, 28));
    		}
    		if (decField.getText().length() > 16) {
    			mainField.setFont(new Font("Segoe UI", 0, 25));
    		}
    		if (decField.getText().length() > 18) {
				mainField.setFont(new Font("Segoe UI", 0, 22));
    		}
    		if (decField.getText().length() > 20) {
				mainField.setFont(new Font("Segoe UI", 0, 19));
    		}
    		if (decField.getText().length() > 28) {
				mainField.setFont(new Font("Segoe UI", 0, 14));
    		}
    		if (decField.getText().length() > 32) {
				mainField.setFont(new Font("Segoe UI", 0, 9));
    		}
    		else {
    			mainField.setFont(new Font("Segoe UI", 0, 31));
    		}
    	}
    	else if (fieldType == "oct") {
    		if (octField.getText().length() > 14) {
    			mainField.setFont(new Font("Segoe UI", 0, 28));
    		}
    		if (octField.getText().length() > 16) {
    			mainField.setFont(new Font("Segoe UI", 0, 25));
    		}
    		if (octField.getText().length() > 18) {
				mainField.setFont(new Font("Segoe UI", 0, 15));
    		}
    		else {
    			mainField.setFont(new Font("Segoe UI", 0, 31));
    		}
    	}
    }

    /** this method will set mainField size for qword button depending on how many large are in it*/
    public void setMainFieldSize(int i) {
    	long qwordValue = 0;
    	long dwordValue = 0;
    	int wordValue = 0;
    	int byteValue = 0;
    	long value = 0;
    	if(inputType.equals("hex")) {
    		value = hexToDec(mainField.getText(),mainField.getText().length());
    	}
    	else if(inputType.equals("dec")) {
    		qwordValue = 922337203685477580L;
        	dwordValue = 214748364;
        	wordValue = 3276;
        	byteValue = 12;
        	try {
        		value = Long.parseLong(mainField.getText());   
        	}catch(Exception e) {}
        	    		 	
    	}
    	else if(inputType.equals("oct")) {
    		qwordValue = 1152921504606846975L;
        	dwordValue = 536870911;
        	wordValue = 8191;
        	byteValue = 27;
        	try {
        		value = octToDec(mainField.getText(),mainField.getText().length());
        	}catch(Exception e) {}
    	}
    	else if(inputType.equals("bin")) {
    		try {
    		value = binToDec(mainField.getText(),mainField.getText().length());
    		}catch(Exception e) {}
    	}
    	if(qwordButton.getText().equals(qwordString)) {
    		if(value >= qwordValue) {//used to make sure that the number in main field does not exceed long max value
    			
    			if(inputType.equals("hex")) {
    				mainFieldSize = 16;
    	    	}
    	    	else if(inputType.equals("dec")) {
    	    		mainFieldSize = 18;
    	    	}
    	    	else if(inputType.equals("oct")) {
    	    		mainFieldSize = 20;
    	    	}
    	    	else if(inputType.equals("bin")) {
    	    		mainFieldSize = 64;
    	    	}
    			if(i <= 7 && value == qwordValue  ) {
    				if(inputType.equals("hex")) {
    					mainFieldSize = 16;
    		    	}
    		    	else if(inputType.equals("dec")) {
    		    		mainFieldSize = 19;
    		    	}
    		    	else if(inputType.equals("oct")) {
    		    		mainFieldSize = 21;
    		    	}
    		    	else if(inputType.equals("bin")) {
    		    		mainFieldSize = 64;
    		    	}
    			}
    			else {
    				if(inputType.equals("hex")) {
    					mainFieldSize = 16;
    		    	}
    		    	else if(inputType.equals("dec")) {
    		    		mainFieldSize = 18;
    		    	}
    		    	else if(inputType.equals("oct")) {
    		    		mainFieldSize = 20;
    		    	}
    		    	else if(inputType.equals("bin")) {
    		    		mainFieldSize = 64;
    		    	}
    			}
    		}
    	
    		else{
    			if(inputType.equals("hex")) {
    				mainFieldSize = 16;
		    	}
		    	else if(inputType.equals("dec")) {
		    		mainFieldSize = 19;
		    	}
		    	else if(inputType.equals("oct")) {
		    		mainFieldSize = 21;
		    	}
		    	else if(inputType.equals("bin")) {
		    		mainFieldSize = 64;
		    	}
    		}
    	}
    	else if(qwordButton.getText().equals(dwordString)) {
    		if(value >= dwordValue) {//used to make sure that the number in main field does not exceed dword max value
    			mainFieldSize = 9;
    			if(i <= 7 && value == dwordValue) {
    				if(inputType.equals("hex")) {
    					mainFieldSize = 8;
    		    	}
    		    	else if(inputType.equals("dec")) {
    		    		mainFieldSize = 10;
    		    	}
    		    	else if(inputType.equals("oct")) {
    		    		mainFieldSize = 11;
    		    	}
    		    	else if(inputType.equals("bin")) {
    		    		mainFieldSize = 32;
    		    	}
    			}
    			else {
    				if(inputType.equals("hex")) {
    					mainFieldSize = 8;
    		    	}
    		    	else if(inputType.equals("dec")) {
    		    		mainFieldSize = 9;
    		    	}
    		    	else if(inputType.equals("oct")) {
    		    		mainFieldSize = 10;
    		    	}
    		    	else if(inputType.equals("bin")) {
    		    		mainFieldSize = 32;
    		    	}
    			}
    		}
    	
    		else{
    			if(inputType.equals("hex")) {
    				mainFieldSize = 8;
		    	}
		    	else if(inputType.equals("dec")) {
		    		mainFieldSize = 10;
		    	}
		    	else if(inputType.equals("oct")) {
		    		mainFieldSize = 11;
		    	}
		    	else if(inputType.equals("bin")) {
		    		mainFieldSize = 32;
		    	}
    		}
    	}
    	else if(qwordButton.getText().equals(wordString)) {
    		if(value >= wordValue) {//used to make sure that the number in main field does not exceed word max value
    			if(inputType.equals("hex")) {
    				mainFieldSize = 4;
		    	}
		    	else if(inputType.equals("dec")) {
		    		mainFieldSize = 4;
		    	}
		    	else if(inputType.equals("oct")) {
		    		mainFieldSize = 5;
		    	}
		    	else if(inputType.equals("bin")) {
		    		mainFieldSize = 16;
		    	}
    			if(i <= 7 && value == wordValue  ) {
    				if(inputType.equals("hex")) {
    					mainFieldSize = 4;
    		    	}
    		    	else if(inputType.equals("dec")) {
    		    		mainFieldSize = 5;
    		    	}
    		    	else if(inputType.equals("oct")) {
    		    		mainFieldSize = 6;
    		    	}
    		    	else if(inputType.equals("bin")) {
    		    		mainFieldSize = 16;
    		    	} 
    			}
    			else {
    				if(inputType.equals("hex")) {
    					mainFieldSize = 4;
    		    	}
    		    	else if(inputType.equals("dec")) {
    		    		mainFieldSize = 4;
    		    	}
    		    	else if(inputType.equals("oct")) {
    		    		mainFieldSize = 5;
    		    	}
    		    	else if(inputType.equals("bin")) {
    		    		mainFieldSize = 16;
    		    	} 
    			}
    		}
    	
    		else{
    			if(inputType.equals("hex")) {
    				mainFieldSize = 4;
		    	}
		    	else if(inputType.equals("dec")) {
		    		mainFieldSize = 5;
		    	}
		    	else if(inputType.equals("oct")) {
		    		mainFieldSize = 6;
		    	}
		    	else if(inputType.equals("bin")) {
		    		mainFieldSize = 16;
		    	} 
    		}
    	}
    	else if(qwordButton.getText().equals(byteString)) {
    		if(value >= byteValue) {//used to make sure that the number in main field does not exceed byte max value
    			if(inputType.equals("hex")) {
    				mainFieldSize = 2;
		    	}
		    	else if(inputType.equals("dec")) {
		    		mainFieldSize = 2;
		    	}
		    	else if(inputType.equals("oct")) {
		    		mainFieldSize = 2;
		    	}
		    	else if(inputType.equals("bin")) {
		    		mainFieldSize = 8;
		    	}
    			if(i <= 7 && value == byteValue) {
    				if(inputType.equals("hex")) {
    					
    		    	}
    		    	else if(inputType.equals("dec")) {
    		    		mainFieldSize = 3;
    		    	}
    		    	else if(inputType.equals("oct")) {
    		    		mainFieldSize = 3;
    		    	}
    		    	else if(inputType.equals("bin")) {
    		    		mainFieldSize = 8;
    		    	}
    			}
    			else {
    				if(inputType.equals("hex")) {
    					mainFieldSize = 2;
    		    	}
    		    	else if(inputType.equals("dec")) {
    		    		mainFieldSize = 2;
    		    	}
    		    	else if(inputType.equals("oct")) {
    		    		mainFieldSize = 2;
    		    	}
    		    	else if(inputType.equals("bin")) {
    		    		mainFieldSize = 8;
    		    	}
    			}
    		}
    	
    		else{
    			if(inputType.equals("hex")) {
    				mainFieldSize = 2;
		    	}
		    	else if(inputType.equals("dec")) {
		    		mainFieldSize = 3;
		    	}
		    	else if(inputType.equals("oct")) {
		    		mainFieldSize = 3;
		    	}
		    	else if(inputType.equals("bin")) {
		    		mainFieldSize = 8;
		    	}
    		}
    	}
    }
    /** this method will set mainField size for qword button depending on how many large are in it
     * qword conversion are done by taking the leftmost bits off until it equals max bin size for that mode*/
    public void qWordButtonConvert() {
    	String binaryString = binField.getText();
    	boolean negative = false;
    	
    	if(qwordButton.getText().equals(qwordString)) { }
		else if(qwordButton.getText().equals(dwordString)) {
			if(binaryString.length() > 32 ) {
				int difference = binaryString.length() - 32 ; 
				StringBuilder sb = new StringBuilder(binaryString);
				for(int i = difference; i > 0; i-- ) {
					sb.deleteCharAt(0);
				}
				binaryString = ""+sb;
			}
		}
		else if(qwordButton.getText().equals(wordString)) {
			if(binaryString.length() > 16 ) {
				int difference = binaryString.length() - 16 ; //the windows calculator shows bits in groups of 4, so if you have 2 1's it will put 2 0's
				StringBuilder sb = new StringBuilder(binaryString);
				for(int i = difference; i > 0; i-- ) {
					sb.deleteCharAt(0);
				}
				binaryString = ""+sb;
			}
		}
		else if(qwordButton.getText().equals(byteString)) {
			if(binaryString.length() > 8 ) {
				int difference = binaryString.length() - 8 ; //the windows calculator shows bits in groups of 4, so if you have 2 1's it will put 2 0's
				StringBuilder sb = new StringBuilder(binaryString);
				for(int i = difference; i > 0; i-- ) {
					sb.deleteCharAt(0);
				}
				binaryString = ""+sb;
			}
		}
    	if(inputType.equals("hex")) {
    		mainField.setText(decToHex(binToDec(binaryString,binaryString.length())));
    		setFontSize("hex");
    		setConvertFields(mainField.getText());
    		
    	}
    	else if(inputType.equals("dec")) {
    		mainField.setText(""+binToDec(binaryString,binaryString.length()));    	
    		setFontSize("dec");
    		setConvertFields(mainField.getText());   		
    	}
    	else if(inputType.equals("oct")) {
    		mainField.setText(decToOct(binToDec(binaryString,binaryString.length())));
    		setFontSize("oct");
    		setConvertFields(mainField.getText());
    		
    	}
    	else if(inputType.equals("bin")) {
    		mainField.setText(binaryString);
    		setFontSize("bin");
    		setConvertFields(mainField.getText());	
    	}
    	
    	
    }
    /** used to get the absoulte value of a negative number for the qword button (if the number is larger than the data type, its supposed to overflow)*/
    public String reverseTwosCompliment(String binaryString) {
    	char[] binaryCharArray = binaryString.toCharArray();
    	int rightmostOneIndex = 0;
    	for(int i = binaryString.length()-1; i >= 0; i--) { // find rightmost 1
    		if (binaryCharArray[i] == '1') {
    			rightmostOneIndex = i;
    			break;
    		}
    	}
    	for(int j = rightmostOneIndex-1; j >=0 ; j--) { //reverse numbers
    		if (binaryCharArray[j] == '1') {
    			binaryCharArray[j]= '0';
    		}
    		else if (binaryCharArray[j] == '0') {
    			binaryCharArray[j]= '1';
    		}
    	}
    	binaryString = new String(binaryCharArray); 
    	return binaryString;
    }
}