package util;

import static org.junit.Assert.*;

import org.junit.Test;


public class PeriodeHelperTest {
	@Test
	public void testGetCheatingGroupsInClosedPeriod() {		
		assertTrue(PeriodHelper.getCheatingGroupsInClosedPeriod().size() > 0);
		
		
	}
	
}
