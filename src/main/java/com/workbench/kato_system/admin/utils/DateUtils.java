package com.workbench.kato_system.admin.utils;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {

	public static LocalDate getStartFiscalDate(Integer year) {

		LocalDate fiscalDate;

		if (year == null) {
			int targetYear = Year.now().getValue();
			int targetMonth = YearMonth.now().getMonthValue();

			if (targetMonth <= 3) {
				fiscalDate = LocalDate.of(targetYear - 1, 4, 1);
			} else {
				fiscalDate = LocalDate.of(targetYear, 4, 1);
			}

			return fiscalDate;
		}

		fiscalDate = LocalDate.of(year, 4, 1);

		return fiscalDate;
	}

	public static LocalDate getEndFiscalDate(Integer year) {

		LocalDate fiscalDate;

		if (year == null) {
			int targetYear = Year.now().getValue();
			int targetMonth = YearMonth.now().getMonthValue();

			if (targetMonth <= 3) {
				fiscalDate = LocalDate.of(targetYear, 3, 31);
			} else {
				fiscalDate = LocalDate.of(targetYear + 1, 3, 31);
			}

			return fiscalDate;
		}

		fiscalDate = LocalDate.of(year + 1, 3, 31);

		return fiscalDate;
	}

	/**
	 * 入社年作成
	 */
	public static List<Integer> createYearList() {

		int startYear = 1980;

		YearMonth currentYearMonth = YearMonth.now();
		int currentYear = currentYearMonth.getYear();

		List<Integer> yearList = new ArrayList<>();

		while (startYear <= currentYear) {
			yearList.add(startYear);
			startYear++;
		}

		return yearList;

	}

	/**
	 * 入社月作成
	 */
	public static List<Integer> createMonthList() {

		int startMonth = 1;

		List<Integer> monthList = new ArrayList<>();

		while (startMonth <= 12) {
			monthList.add(startMonth);
			startMonth++;
		}

		return monthList;
	}
}
