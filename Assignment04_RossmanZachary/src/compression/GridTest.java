package compression;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Program that draws 2-dimensional grid of squares on canvas.  User
 * can change colors by clicking on cells.  Colors are chosen from a
 * list of colored buttons on the right edge of the screen.  The current
 * color being used is shown in a square on the left edge of the screen.

 * @author kim
 * Created on Feb 28, 2004, revised 2/2011
 *
 */


public class GridTest extends JFrame {
	private static final int HGAP = 10; // gap between panels
	private static final int VGAP = 10; // and btn source buttons

	private static final int NUM_COLORS = 6;	// Number of colors available
	private static final int NUM_ROWS = 20; // # rows in grid of squares
	private static final int NUM_COLS = 20; // # columns in grid of squares

	// Text area where compressed list is displayed
	protected JTextArea outArea = new JTextArea(8,40);

	// array of buttons for getting colors
	protected JButton[] sourceButtons = new JButton[NUM_COLORS + 1];

	
	// Table holding the colors displayed in the grid
	protected TwoDTable<Color> rects;
	
	private DrawingPanel canvas;
	
	/**
	 * The main method simply creates the ColorFrame,
	 * initializes it, adds the mouse listeners, and 
	 * waits for user clicks.
	 * @param args command-line arguments (none
	 *        in this case)
	 */
	public static void main(String[] args) {
		GridTest frame = new GridTest();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(650,650);
		frame.setVisible(true);
	}

	/**
	 *  @post:  Drew grid of rectangles on screen, buttons on right,
	 *         and color swatch on left showing current color used.
	 */
	public GridTest() {
		JPanel sourcePanel, listPanel;
		// set up panel of buttons as one long column
		sourcePanel = new JPanel(new GridLayout(NUM_COLORS + 1, 1, HGAP, VGAP));

		listPanel = new JPanel();
		// set up panel for list text area
		JScrollPane scrollableTextArea = new JScrollPane( outArea );
		listPanel.add( scrollableTextArea );
		Container contentPane = getContentPane();
		contentPane.add(listPanel, BorderLayout.SOUTH);
		
		// Create and add new buttons to panel
		for (int index = 0; index < NUM_COLORS; index++) {
			sourceButtons[index] = new JButton();
			sourcePanel.add(sourceButtons[index]);
			sourceButtons[index].addActionListener(new ButtonListener());
		}
		sourceButtons[NUM_COLORS] = new JButton("Display list");
		sourcePanel.add(sourceButtons[NUM_COLORS]);
		sourceButtons[NUM_COLORS].addActionListener(new ButtonListener());

		// Set buttons the appropriate colors, both with text and the color of the text
		sourceButtons[0].setForeground(Color.RED);
		sourceButtons[0].setText("Red");
		sourceButtons[1].setForeground(Color.MAGENTA);
		sourceButtons[1].setText("Magenta");
		sourceButtons[2].setForeground(Color.YELLOW);
		sourceButtons[2].setText("Yellow");
		sourceButtons[3].setForeground(Color.GREEN);
		sourceButtons[3].setText("Green");
		sourceButtons[4].setForeground(Color.BLUE);
		sourceButtons[4].setText("Blue");
		sourceButtons[5].setForeground(Color.CYAN);
		sourceButtons[5].setText("Cyan");
		
		contentPane.add(sourcePanel, BorderLayout.EAST);
		
		rects = new CompressedTable<Color>(NUM_ROWS,NUM_COLS,Color.green);		
		canvas = new DrawingPanel(rects,NUM_ROWS,NUM_COLS,Color.RED);
		contentPane.add(canvas,BorderLayout.CENTER);
	}
	
	/**
	 * Class representing actions to be taken when the user clicks on one of the buttons
	 * on the right margin of the output.
	 * @author kim
	 * @version 2/2011
	 */
	public class ButtonListener implements ActionListener {
		/**
		 * Post: If user clicked on a color button, current color is set to that color
		 *       If user clicked on display list, implementation is displayed at bottom of window
		 * @param evt
		 * 			The event representing the action, e.g., a button press.
		 */
		public void actionPerformed(ActionEvent evt) {
			JButton clicked = (JButton) evt.getSource();
			// Cast allows treating source of event as button

			if (clicked.getText() == "Display list") {// If clicked on display button
				outArea.setText(rects.toString());
			} else {  											// clicked on color button
				// then update current color based on the color showing on the button
				canvas.setCurrentColor(clicked.getForeground());
			}
		}
	
	}
	
}

