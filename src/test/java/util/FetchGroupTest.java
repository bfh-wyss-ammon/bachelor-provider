package util;

import static org.junit.Assert.*;

import org.junit.Test;

public class FetchGroupTest {

	@Test
	public void test() {
		
		//NOTE: This test only works when the authority is running
		boolean success = GroupHelper.getGroupsFromAuthority("http://localhost:4567/group");
		
		assertEquals(success,true);
		}

}
