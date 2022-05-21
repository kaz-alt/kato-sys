package com.workbench.kato_system.admin.utils;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;

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
}
