package com.workbench.kato_system.admin.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageNumberUtils {

	public static Pageable getPageable(Integer pageNumber, Integer size, String sortColumn) {

		if (pageNumber == null || size == null) {
			return null;
		}

		revisePageNumber(pageNumber);

		return PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.ASC, sortColumn));
	}

	public static Integer revisePageNumber(Integer pageNumber) {

		if (pageNumber > 0) {
			pageNumber = pageNumber - 1;
		}
		return pageNumber;
	}
}
