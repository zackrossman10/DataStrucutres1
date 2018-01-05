package calc;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Application for a simple postfix calculator
 * 
 *  
 * @version 1/97 revised 2/2011
 * @author Kim B. Bruce
 */
@SuppressWarnings("serial") // We do not need our class to be Serializable.
public class Calculator extends JFrame {
	private JTextField display; // Display window for calculator
	private JButton[] digitButton; // Array of buttons representing 10 digits
	private JButton clearButton, // Button to clear display
			enterButton, popButton, // Buttons to enter #'s and pop off
			multButton, divButton, addButton, subButton, factorialButton; // Arith. operation
															// buttons
	private State calcState; // Object w/memory of computation in progress
	private JPanel calcPanel; // Panel holding buttons and display

	private JButton exchButton;

	/**
	 *Add JButtons representing 0-9 digits and operations
	 *clear, exchange, divide, multiply, subtract, add,
	 *factorial, pop, and enter
	 * 
	 * @post: Set up calculator panel w/buttons and added it to applet
	 **/
	public Calculator() {
		calcPanel = new JPanel();
		calcPanel.setLayout(new GridLayout(6, 1, 5, 5)); // lay out 6 rows w/1
															// column each

		display = new JTextField("0"); // display is right-justified
		display.setEditable(false);
		display.setHorizontalAlignment(JTextField.RIGHT);
		calcPanel.add(display); // display occupies first row

		calcState = new State(display);

		JPanel topPanel = new JPanel(); // Panel for top two rows of calculator
		topPanel.setLayout(new GridLayout(1, 2, 5, 5)); // lay out 1 row w/2
														// cols
		clearButton = new JButton("Clear");
		clearButton.addActionListener((ActionEvent evt) -> {calcState.clear();});
		topPanel.add(clearButton);

		exchButton = new JButton("Exch");
		exchButton.addActionListener((ActionEvent evt) -> {calcState.exchange();});
		topPanel.add(exchButton);

		divButton = new JButton("/");
		divButton.addActionListener(new OpButtonListener('/', calcState));
		topPanel.add(divButton);
		
		factorialButton = new JButton("!");
		factorialButton.addActionListener((ActionEvent evt) -> {calcState.factorial();});
		topPanel.add(factorialButton);

		calcPanel.add(topPanel);

		JPanel midPanel;
		digitButton = new JButton[10]; // Set up all buttons to enter digits

		for (int row = 0; row < 3; row++) {
			midPanel = new JPanel();
			midPanel.setLayout(new GridLayout(1, 4, 5, 5));

			for (int col = 0; col < 3; col++) {
				int digit = (2 - row) * 3 + col + 1;
				digitButton[digit] = new JButton("" + digit);
				digitButton[digit].addActionListener(new DigitButtonListener(
						digit, calcState));
				// digitButton[digit].setBackground(Color.cyan);
				midPanel.add(digitButton[digit]);
			}
			switch (row) { // Put a different operator at end of each row
			case 0:
				multButton = new JButton("*");
				multButton.addActionListener(new OpButtonListener('*',
						calcState));
				// multButton.setBackground(Color.green);
				midPanel.add(multButton);
				break;
			case 1:
				addButton = new JButton("+");
				addButton
						.addActionListener(new OpButtonListener('+', calcState));
				midPanel.add(addButton);
				break;
			case 2:
				subButton = new JButton("-");
				subButton
						.addActionListener(new OpButtonListener('-', calcState));
				// subButton.setBackground(Color.green);
				midPanel.add(subButton);
				break;
			}

			calcPanel.add(midPanel);

			setSize(210, 300);
		}

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 3, 5, 5));

		popButton = new JButton("Pop");
		popButton.addActionListener((ActionEvent evt) ->{calcState.pop();});
		bottomPanel.add(popButton);

		digitButton[0] = new JButton("" + 0);
		digitButton[0].addActionListener(new DigitButtonListener(0, calcState));
		bottomPanel.add(digitButton[0]);

		enterButton = new JButton("Enter");
		enterButton.addActionListener((ActionEvent evt) -> {calcState.enter();});
		bottomPanel.add(enterButton);

		calcPanel.add("South", bottomPanel);

		// Default layout for Frames is BorderLayout.
		getContentPane().add(BorderLayout.CENTER, calcPanel); // Adds calcPanel
																// in center of
																// Frame.

	}

	/**
	 * Post: Put border around calcPanel in applet This is a rather bizarre
	 * procedure inherited from Container. Overriding this method provides new
	 * inset values when components are added to this container. Makes sure to
	 * leave the specified amount of space along each edge of Frame.
	 **/
	public Insets getInsets() {
		return new Insets(10, 10, 10, 10);
	}

	/**
	 * Create and show Calculator so it can respond to events
	 * 
	 * @param args
	 *            -- ignored
	 */
	public static void main(String[] args) {
		Calculator myCalc = new Calculator();
		myCalc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myCalc.setSize(250, 350);
		myCalc.setVisible(true);

	}
}
