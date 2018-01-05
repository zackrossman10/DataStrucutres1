package calc;

import java.awt.event.*;

/**
 * Class representing buttons with numbers on them, listens to mouse clicks 
 * on these buttons and adds these values to the calcState when clicked
 * 
 * @author Kim B. Bruce
 * @version 1/97
 */
public class DigitButtonListener implements ActionListener {
	public State calcState;
	public int value;
	
	/**
	 * Button knows own value and the state so can communicate with it.
	 * 
	 * @param newValue the value of the button
	 * @param state the current state of the calculator, including the stack
	 * @post set up DigitalButtonListener that contains the value of its button and
	 * the state of the calculator
	 */
	public DigitButtonListener(int newValue, State state) {
		calcState = state;
		value = newValue;
	}
	
	/**
	 * @param evt a mouseClick on the button
	 * @pre:  User clicked on the button.
	 * @post:  Informed state that it was clicked on and what its value is, 
	 * and value is added to calcState
	 * 
	 */
	public void actionPerformed(ActionEvent evt) {
		calcState.addDigit(value);
		
	}
}
