package util;

import static org.junit.Assert.*;

import org.junit.Test;

public class FetchGroupTest {

	@Test
	public void test() {
		
		boolean success = GroupFetcher.getGroupsFromAuthority("http://localhost:10000/group");
		
		assertEquals(success,true);
		}

}
