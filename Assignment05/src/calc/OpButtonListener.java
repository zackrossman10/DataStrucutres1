package calc;

import java.awt.event.*;

/**
 *	Class notified when operator buttons are pressed.
 *
 *  @version 1/97, revised 2/2011
 *  @author Kim B. Bruce
 */
public class OpButtonListener implements ActionListener
{
	protected State calcState;	// Does all the computation - buttons inform it of events.
	protected char opCode;		// label of button
	
	/**
	 *	Button knows its operation and the state so can communicate with it.
	 *
	 *@param op the character associated with each operation
	 *@param state the current state of the calculator, so that the operation
	 *can operate on previous values
	 *@post up OPButtonListener that contains the operation of its button and
	 * the state of the calculator
	 */
	public OpButtonListener(char op, State state)
	{
		calcState = state;	// Remember the state so can communicate with it.
		opCode = op;
	}
	
	/**
	 * @param evt a mouseClick on the button
	 *	@pre:  Button was pushed.
	 *	@post:  Inform state that operation corresponding to button should be performed.
	 **/
	public void actionPerformed(ActionEvent evt)
	{
		calcState.doOp(opCode); 
	}
}
