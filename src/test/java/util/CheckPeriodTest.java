package util;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

public class CheckPeriodTest {

	@Test
	public void TestResolvePeriode() throws ParseException {
		Date d = PeriodHelper.parse("17-11-2017");
		PeriodHelper.checkPeriod(d);
	}
}
