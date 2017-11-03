package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.junit.Test;

import data.ProviderSettings;

public class PeriodTest {
	
	@Test
	public void test() {
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate d = LocalDate.now();
		System.out.println(formatters.format(d));
		
	}
	


}
