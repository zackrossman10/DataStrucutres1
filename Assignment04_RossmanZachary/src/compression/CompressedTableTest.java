package compression;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CompressedTableTest {
	CompressedTable<String> table;
	
	@Before
	public void setUp() throws Exception{
		table =new CompressedTable<String>(5, 6, "x");
	}
	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
