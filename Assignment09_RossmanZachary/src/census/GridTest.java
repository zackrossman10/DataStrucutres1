package census;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Test that the grid class operates correctly
 * @author zackr elewis
 *
 */
public class GridTest {
	float one = (float)1;
	float two = (float)2;
	float three = (float) 3;
	float four = (float) 4;
	float five = (float) 5;
	protected Grid test1;
	protected Grid test2;
	protected Grid test3;
	
	@Before 
	public void setUp(){
		test1 = new Grid(one, two, three, four);
		test2 = new Grid(two, three, four, five);
		test3 = new Grid(one, three, four, two);
	}
	
	@Test
	public void testGrid() {
		
		assertTrue(test1.toString().equals("[left=1.0 right=2.0 top=3.0 bottom=4.0]"));
		assertTrue(test2.toString().equals("[left=2.0 right=3.0 top=4.0 bottom=5.0]"));
	}
	
	@Test
	public void testConstructGrid(){
		Rectangle[][] results = test3.constructGrid(2);
		for(int i= results.length-1; i>=0; i--){
			for(int j = 0; j<results.length; j++){
				System.out.print(results[i][j].toString());
			}
			System.out.println();
		}
		System.out.println();
		Rectangle[][] results1 = test3.constructGrid(10);
		for(int i= results1.length-1; i>=0; i--){
			for(int j = 0; j<results1.length; j++){
				System.out.print(results1[i][j].toString());
			}
			System.out.println();
		}
	}
}
