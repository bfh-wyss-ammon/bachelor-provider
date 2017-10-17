package util;

import static org.junit.Assert.*;

import org.junit.Test;

public class FetchGroupTest {

	@Test
	public void test() {
		boolean success = GroupHelper.getGroupsFromAuthority("http://localhost:10000/groups/" , 2);
		assertEquals(success,true);
		}

}
