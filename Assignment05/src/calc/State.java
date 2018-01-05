package calc;

import java.util.Deque;
import javax.swing.*;
import java.util.ArrayDeque;

/**
 * Class representing the internal state of the calculator. It is responsible
 * for keeping track of numbers entered and performing operations when buttons
 * are clicked. It tells the display what number to show.
 * 
 * @version 10/5/17
 * @author Zack Rossman
 * 
 */
public class State {
	// Display on which results are written
	protected JTextField calcDisplay;
	
	// The stack to on which to store numbers.
	//
	// Note 1: If you implement the extra credit of adding floating point operations,
	// then you will need to change the type parameter here, otherwise this
	// field should not be changed.
	//
	// Note 2: Deque<Integer> is an interface two reasonable implementing classes to use
	// would be ArrayDeque and LinkedList.
	protected Deque<Integer> stack;

	protected String accumulator = ""; //accumulates values until the "enter" or an operation button is pressed

	/**
	 * @param display the display on which to store numbers
	 * @post initialize a new stack and display with "0" as the default text
	 */
	public State(JTextField display) {
		stack = new ArrayDeque<Integer>();
		calcDisplay = display;
		calcDisplay.setText("0");
	}

	/**
	 * User has clicked on a digit button
	 * 
	 * @param value the value of the button which the mouse clicked
	 * @post concatenate value onto accumulator and display the current
	 * value of accumulator on the display
	 */
	public void addDigit(int value) {
		accumulator += Integer.toString((Integer)value);
		calcDisplay.setText(accumulator);
	}

	/**
	 * User has clicked on operator button ...
	 * 
	 * @pre the stack is >1 if the factorial operation is selcted, and
	 * > 2 if any other operation is selected.
	 * @param op the operation that the user clicked on
	 * @post pop the elements required for the specific operation off the 
	 * stack, perform the operation and push the result back onto the stack
	 */
	public void doOp(char op) throws ArithmeticException{
		//push the accumulator to the stack so that pushing an operation
		//button works without pressing the enter button after pressing a digit
		if(accumulator != ""){
			this.enter();
		}
		//clear the stack, set accumulator equal to empty string, and set the display to "error"	
		//if the stack size is less than 2
		if(stack.size()<2){
			this.clear();
			accumulator = "";
			calcDisplay.setText("Error");
			return;
		}else{
			//otherwise, pop top two elements
			int first = stack.pop();
			int second = stack.pop();
			//create a temp variable to hold the result
			int result = 0;
			//check the different operations and perform the correct operation on first and second
			switch(op){
				case '+':
					result = (second + first);
					break;
				case '-':
					result = (second - first);
					break;
				case '*':
					result = (second * first);
					break;
				case '/':
					try{
	 					result = (second / first);
					}catch (ArithmeticException s){
						//if we encounter an arithmetic exception because
						//first =0, clear the stack, set accumulator = empty string,
						//and print "error" on the display
						this.clear();
						accumulator = "";
						calcDisplay.setText("Error");
						return;
					}
					break;
			}
			stack.push(result);
			calcDisplay.setText(Integer.toString(stack.peek()));
		}	
	}

	/**
	 * User clicked on enter button ...
	 * 
	 * @post do nothing if the stack or accumulator are empty,
	 * otherwise push the integer value of the accumulator onto 
	 * the stack and reset accumulator
	 */
	public void enter() {
		//if there's nothing on the stack and there's nothing
		//in the accumulator when enter is pressed, do nothing
		if((stack.size() == 0)&&(accumulator== "")){
			return;
		}		
		//if we are pressing enter in after adding digits
		//push the value of the accumulator onto the stack and rest it to the empty string
		if(accumulator != ""){
			stack.push(Integer.valueOf(accumulator)); 
			accumulator = "";
		}
		calcDisplay.setText(Integer.toString(stack.peek()));
	}

	/**
	 * User clicked on clear key ...
	 * 
	 * @post clear the stack and set the display to "0"
	 */
	public void clear() {
		stack.clear();
		calcDisplay.setText("0");
	}

	/**
	 * User clicked on pop key ...
	 * 
	 * @post if pressed while accumulator is non-empty, reset accumulator.
	 * Otherwise, pop the top value off of the stack
	 */
	public void pop() {
		//if in the process of building a number,
		//throw the number away and set the display to "0"
		if(accumulator != ""){
			accumulator = "";
		}else{
			//otherwise, throw away the top value of the stack
			//and set the display to the new top of the stack
			if(stack.isEmpty() || stack.size() ==1){
				calcDisplay.setText("0");
			}else{
				stack.pop();
				calcDisplay.setText(Integer.toString(stack.peek()));
			}
		}
	}

	/**
	 * User clicked on exchange key ...
	 * 
	 * @pre there should be more than 2 values on the stack
	 * @post swap the top and 2nd-to-top values of the stack
	 */
	public void exchange() {
		//if there are <2 elements on the stack, do nothing
		if(stack.size()<2){
			return;
		}
		//pop the first two values of the stack
		int first = stack.pop();
		int second = stack.pop();
		//push them on in reverse order
		stack.push(first);
		stack.push(second);
		calcDisplay.setText(Integer.toString(stack.peek()));		
	}
	
	/**
	 * User clicked on the factorial key...
	 * 
	 * @pre stack is non-empty
	 * @post pop the top value of the stack, push the factorial of this value onto the stack
	 */
	public void factorial(){
		//if stack is empty, clear the stack and accumulator, and set display to "Error"
		this.enter();
		if(stack.size()<1){
			this.clear();
			accumulator = "";
			calcDisplay.setText("Error");
			return;
		}
		//if stack is not empty, pop the first element and calculate the factorial
		int first = stack.pop();
		int accumulator1 = 1;
		for(int i=first; i>=2; i--){
			accumulator1 *= i;
		}
		//push the factorial back onto the stack
		stack.push(accumulator1);
		calcDisplay.setText(Integer.toString(stack.peek()));
		return;
	}
}
