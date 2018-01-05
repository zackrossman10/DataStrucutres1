package compression;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

/**
 * Class representing the panel displaying the two dimensional grid of colored
 * rectangles.
 * 
 * @author kim
 * @version Mar 2, 2004, revised 2/2011
 * 
 */

public class DrawingPanel extends JPanel {

	private static final double LEFT_EDGE = 100.0; // left and top edges of grid
	private static final double TOP_EDGE = 50.0;

	private static final int RECT_SIDE = 20; // Dimensions of grid rects

	private static final double SWATCH_X = 30.0; // location and size of
	private static final double SWATCH_Y = 140.0; // rect showing current color
	private static final double SWATCH_SIZE = 20.0;

	private Rectangle2D coloredRect, // template rectangle to create all in grid
			infoSwatch; // rectangle for color swatch on left

	private TwoDTable<Color> rectColors; // table holding colors for grid

	private int numOfRows, numOfCols; // number of rows and columns in grid
	private Color currentColor; // current color selected

	/**
	 * Initialize variables and set up listener for mouse to be a
	 * GridMouseListener
	 * 
	 * @param colors
	 *            Table providing color for each grid square
	 * @param rows
	 *            Number of rows in grid
	 * @param cols
	 *            Number of columns in grid
	 * @param startColor
	 *            Color grid is initially painted
	 */
	public DrawingPanel(TwoDTable<Color> colors, int rows, int cols,
			Color startColor) {
		rectColors = colors;
		currentColor = startColor;
		numOfRows = rows;
		numOfCols = cols;
		coloredRect = new Rectangle2D.Double(0.0, 0.0, RECT_SIDE, RECT_SIDE);
		infoSwatch = new Rectangle2D.Double(SWATCH_X, SWATCH_Y, SWATCH_SIZE,
				SWATCH_SIZE);
		addMouseListener(new GridMouseListener());

	}

	/**
	 * @post: currentColor is updated to newColor and canvas redrawn
	 * 
	 * @param newColor
	 */
	public void setCurrentColor(Color newColor) {
		currentColor = newColor;
		repaint();
	}

	/**
	 * @post: Canvas is repainted, displaying grid and color swatch
	 * 
	 * @param g
	 *            Current graphics context for drawing
	 */
	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		// draw color swatch
		g2.setColor(currentColor);
		g2.fill(infoSwatch);
		g2.setColor(Color.BLACK);
		g2.draw(infoSwatch);

		// draw grid of rectangles in appropriate colors
		for (int rowNum = 0; rowNum < numOfRows; rowNum++) {
			for (int colNum = 0; colNum < numOfCols; colNum++) {
				g2.setPaint(rectColors.getInfo(rowNum, colNum));
				// move rectangle to its desired position before drawing it
				coloredRect.setRect(LEFT_EDGE + RECT_SIDE * colNum, TOP_EDGE
						+ RECT_SIDE * rowNum, RECT_SIDE, RECT_SIDE);
				g2.fill(coloredRect);
				g2.setColor(Color.BLACK);
				g2.draw(coloredRect);
			}
		}
	}

	/**
	 * Class to respond to mouse clicks in canvas
	 * 
	 * @author kim
	 * @version 2/2011
	 * 
	 */
	public class GridMouseListener extends MouseAdapter {
		/**
		 * @post: if pt in square in grid, its color is now currentColor
		 * otherwise nothing was changed
		 * 
		 * @param evt
		 *            the event triggered by a mouse click
		 */
		public void mouseClicked(MouseEvent evt) {
			RowOrderedPosn whereClicked = findRect(evt.getPoint());
			if (whereClicked != null) { // clicked on square in grid
				rectColors.updateInfo(whereClicked.getRow(), whereClicked
						.getCol(), currentColor);
			}
			repaint();
		}

		/**
		 * @param pt
		 *            location where user clicked
		 * @return the position (row,col) of square in grid containing pt, 
		 *         if pt not in a square then return null
		 */
		private RowOrderedPosn findRect(Point pt) {
			int x = (int) pt.getX();
			int y = (int) pt.getY();
			if (y >= TOP_EDGE && x >= LEFT_EDGE) { // clicked below top and to
													// right of left edge
				int rowNum = (int) ((y - TOP_EDGE) / RECT_SIDE);
				int colNum = (int) ((x - LEFT_EDGE) / RECT_SIDE);
				if (colNum < numOfCols && rowNum < numOfRows) { // inside grid
					return new RowOrderedPosn(rowNum, colNum,numOfRows,numOfCols);
				} else { // clicked too far to right or down to be on grid
					return null;
				}
			} else
				// clicked above or to left of grid.
				return null;
		}

	}

}
