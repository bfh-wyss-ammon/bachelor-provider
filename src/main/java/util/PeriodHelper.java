/**
 *   Copyright 2018 Pascal Ammon, Gabriel Wyss
 * 
 * 	 Implementation eines anonymen Mobility Pricing Systems auf Basis eines Gruppensignaturschemas
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This class static helper methods in relation to the toll calculation periods. It performing the check if the payment of the period per group were equal to the costs. It also has some formatting and parsing methods for the period (date objects).
 */

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
import data.CommonSettings;

public class PeriodHelper {

	public static void checkPeriod(Date periode) {
		Date after = PeriodHelper.getAfterLimit(periode);
		Date before = PeriodHelper.getBeforeLimit(periode);

		List<DbGroup> groups = DatabaseHelper.Get(DbGroup.class);

		for (DbGroup group : groups) {

			int sumOfCosts = 0;

			// get the cost for this group
			List<DbVTuple> tuples = DatabaseHelper.Get(DbVTuple.class, "groupId= '" + group.getGroupId() + "'",
					"created", after, before);

			if (tuples.size() > 0) {
				for (DbVTuple tuple : tuples) {
					sumOfCosts += tuple.getPrice();
				}
			}

			// get the payment total for this group
			String strPeriod = PeriodHelper.dbFormat(periode);

			if (DatabaseHelper.Exists(DbVPayment.class,
					"period= '" + strPeriod + "' AND groupId= '" + group.getGroupId() + "'")) {
				DbVPayment payment = DatabaseHelper.Get(DbVPayment.class,
						"period= '" + strPeriod + "' AND groupId= '" + group.getGroupId() + "'");

				if (sumOfCosts > payment.getTotal())
					DisputeResolveHelper.createResolveRequest(periode, group);
			} else {
				DisputeResolveHelper.createResolveRequest(periode, group);
			}
		}
	}

	public static void checkClosedPeriod() {

		ProviderSettings settings = SettingsHelper.getSettings(ProviderSettings.class);
		int gracePeriods = settings.getGracePeriods();
		int periodLength = settings.getPeriodLengthDays();
		LocalDate closedPeriod = LocalDate.now().minusDays(gracePeriods * periodLength + 1);
		Date period = Date.from(closedPeriod.atStartOfDay(ZoneId.systemDefault()).toInstant());

		checkPeriod(period);
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
