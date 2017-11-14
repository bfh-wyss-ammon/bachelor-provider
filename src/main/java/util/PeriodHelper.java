package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import data.DbGroup;
import data.DbVPayment;
import data.DbVTuple;
import data.ProviderSettings;

public class PeriodHelper {

	public static void checkClosedPeriod() {

		ProviderSettings settings = SettingsHelper.getSettings(ProviderSettings.class);
		int gracePeriods = settings.getGracePeriods();
		int periodLength = settings.getPeriodLengthDays();
		LocalDate closedPeriod = LocalDate.now().minusDays(gracePeriods * periodLength + 1);
		Date period = Date.from(closedPeriod.atStartOfDay(ZoneId.systemDefault()).toInstant());

		Date after = PeriodHelper.getAfterLimit(closedPeriod);
		Date before = PeriodHelper.getBeforeLimit(closedPeriod);

		List<DbGroup> groups = DatabaseHelper.Get(DbGroup.class);

		for (DbGroup group : groups) {

			int sumOfCosts = 0;

			// get the cost for this group
			List<DbVTuple> tuples = DatabaseHelper.Get(DbVTuple.class, "groupId= '" + group.getProviderGroupId() + "'",
					"created", after, before);

			if (tuples.size() > 0) {
				for (DbVTuple tuple : tuples) {
					sumOfCosts += tuple.getPrice();
				}
			}

			// get the payment total for this group
			String strPeriod = PeriodHelper.dbFormat(closedPeriod);
			DbVPayment payment = DatabaseHelper.Get(DbVPayment.class,
					"period= '" + strPeriod + "' AND groupId= '" + group.getProviderGroupId() + "'");

			if (sumOfCosts > payment.getTotal())
				DisputeResolveHelper.createResolveRequest(period, group);

		}

		return;
	}

	public static boolean isAllowed(String period) {

		ProviderSettings settings = SettingsHelper.getSettings(ProviderSettings.class);
		int gracePeriods = settings.getGracePeriods();
		int periodLength = settings.getPeriodLengthDays();
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern(settings.getPeriodFormat());

		for (int i = 0; i <= gracePeriods; i++) {
			if (period.equals(formatters.format(LocalDate.now().minusDays(i * periodLength)))) {
				return true;
			}
		}

		return false;

	}

	public static Date parse(String period) throws ParseException {
		DateFormat formatter = new SimpleDateFormat(
				SettingsHelper.getSettings(ProviderSettings.class).getPeriodFormat());
		return formatter.parse(period);
	}

	public static String dbFormat(Date period) {
		DateFormat dbFormat = new SimpleDateFormat(SettingsHelper.getSettings(ProviderSettings.class).getDbFormat());
		return dbFormat.format(period);
	}

	public static String dbFormat(LocalDate period) {
		return dbFormat(Date.from(period.atStartOfDay(ZoneId.systemDefault()).toInstant()));
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

	public static Date getAfterLimit(LocalDate period) {
		return getAfterLimit(Date.from(period.atStartOfDay(ZoneId.systemDefault()).toInstant()));
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

	public static Date getBeforeLimit(LocalDate period) {
		return getBeforeLimit(Date.from(period.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

}
