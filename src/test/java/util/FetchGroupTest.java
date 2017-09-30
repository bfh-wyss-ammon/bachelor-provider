package util;

import static org.junit.Assert.*;

import org.junit.Test;

public class FetchGroupTest {

	@Test
	public void test() {
		
		//NOTE: This test only works when the authority is running
		boolean success = GroupFetcher.getGroupsFromAuthority("http://localhost:10000/group");
		
		assertEquals(success,true);
		}

}
