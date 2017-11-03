package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import data.ProviderSettings;

public class PeriodHelper {

	public static boolean isAllowed(String period) {

		ProviderSettings settings = SettingsHelper.getSettings(ProviderSettings.class);
		int gracePeriods = settings.getGracePeriods();
		int periodLength = settings.getPeriodLengthDays();
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		for (int i = 0; i <= gracePeriods; i++) {
			if (period.equals(formatters.format(LocalDate.now().minusDays(i * periodLength)))) {
				return true;
			}
		}

		return false;

	}

	public static Date getAfterLimit(Date period) {
		int periodLength = SettingsHelper.getSettings(ProviderSettings.class).getPeriodLengthDays();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(period);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR, 0);
		calendar.add(Calendar.DAY_OF_MONTH, -((periodLength - 1) / 2));

		return calendar.getTime();
	}

	public static Date getBeforeLimit(Date period) {
		int periodLength = SettingsHelper.getSettings(ProviderSettings.class).getPeriodLengthDays();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(period);
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.HOUR, 23);
		calendar.add(Calendar.DAY_OF_MONTH, (periodLength - 1) / 2);

		return calendar.getTime();
	}

}
