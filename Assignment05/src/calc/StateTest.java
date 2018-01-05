package calc;

import static org.junit.Assert.*;
import javax.swing.JTextField;
import org.junit.Test;
import org.junit.Before;

public class StateTest {
    private JTextField display; // dummy display for testing
    private State st; // sample state object

    // set up state with 2 on top of stack and 9 below it.
    @Before
    public void setUp() {
        display = new JTextField("0");
        st = new State(display);
        st.addDigit(9);
        st.enter();
        st.addDigit(2);
        st.enter();
    }

    // check that if 2 on stack and add digits 7, 5, and then
    // hit enter that display will show "75"
    @Test
    public void testAddDigit() {
        assertEquals("2", display.getText());
        st.addDigit(7);
        assertEquals("7", display.getText());
        st.addDigit(5);
        assertEquals("75", display.getText());
        st.enter();
        assertEquals("75", display.getText());
    }

    // check that if stack has 4, 2, and 9, then executing plus twice
    // gives "6" and then "15".
    @Test
    public void testPlus() {
        st.addDigit(4);
        st.enter();
        st.doOp('+');
        assertEquals("6", display.getText());
        st.doOp('+');
        assertEquals("15", display.getText());
        st.clear();
        st.addDigit(9);
        st.enter();
        st.addDigit(0);
        st.doOp('+');
        assertEquals("9", display.getText());
    }

    //check that if the stack has 4, 2, and 9, then executing multiplication twice
    //gives "8" and "72"
    @Test
    public void testMult() {
        st.addDigit(4);
        st.enter();
        st.doOp('*');
        assertEquals("8", display.getText());
        st.doOp('*');
        assertEquals("72", display.getText());
    }
   
    //check that if the stack has 12, 2, and 9, then executing subtraction twice
    //gives "-10" and "19"
    @Test
    public void testMinus() {
    	 st.addDigit(12);
         st.doOp('-');
         assertEquals("-10", display.getText());
         st.doOp('-');
         assertEquals("19", display.getText());
    }

    //check that if the stack has -2, 2, and 9, then executing division twice
    //gives "-1" and "-9"
    @Test
    public void testDiv() {
	   	 st.addDigit(-2);
	     st.doOp('/');
	     assertEquals("-1", display.getText());
	     st.doOp('/');
	     assertEquals("-9", display.getText());
    }

    @Test
    public void errorTestDiv() { 
    	st.addDigit(0);
    	st.doOp('/');
    	assertEquals("Error", display.getText());
    	assertTrue(st.stack.size() == 0);
    }
    
    //check that if the stack has 18, 2, and 9, then pressing enter
    //twice in a row doesn't change anything, and executing multiplication twice
    //gives "8" and "72"
    @Test
    public void testEnter() { 
    	 st.enter();
    	 assertEquals("2", display.getText());
    	 st.addDigit(18);
    	 st.enter();
    	 assertEquals("18", display.getText());
    	 st.doOp('-');
    	 assertEquals("-16", display.getText());
    	 st.enter();
    	 assertEquals("-16", display.getText());
    }

    //check if clearing a populated stack will result in "0" being
    //displayed, as well as when we clear an empty stack
    @Test
    public void testClear() {
        st.clear();
        assertEquals("0", display.getText());
        st.clear();
        assertEquals("0", display.getText());
    }

    // check if 2 and 9 on stack that if pop once get "9" in display
    // and if pop again have "0" on stack and remains if pop once more.
    @Test
    public void testPop() {
        st.pop();
        assertEquals("9", display.getText());
        st.pop();
        assertEquals("0", display.getText());
        st.pop();
        assertEquals("0", display.getText());
    }

    // Method exchange should swap top two values so after
    // exchange have 9 on top with 2 underneath.
    @Test
    public void testExchange(){
        st.exchange();
        assertEquals("9", display.getText());
        st.pop();
        assertEquals("2", display.getText());
    }
    
    //Method factorial should calculate and push the factorial
    //of the top value of the stack
    @Test
    public void testFactorial(){
    	st.factorial();
    	assertEquals("2", display.getText());
    	st.addDigit(4);
    	st.factorial();
    	assertEquals("24", display.getText());
    }
 
}
