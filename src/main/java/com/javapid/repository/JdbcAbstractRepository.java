package com.javapid.repository;

import java.util.List;
import java.util.stream.Collectors;

public abstract class JdbcAbstractRepository {

	final String COUPON_TABLE = "data_pid";
	final String TICKET_TABLE = "pid_jizdenky";
	final String VALIDITY_COLUMN = "platnost";
	final String YEAR_COLUMN = "rok";
	final String SELL_TYPE_COLUMN = "typ_predaja";
	final String MONTH_COLUMN = "mesiac";
	final String CODE_COLUMN = "kod";
	final String DISCOUNTED_COLUMN = "zlavneny";

	<T> String arrayToSqlString(String columnName, List<T> inputStrings) {
		List<String> convertedString = inputStrings.stream()
				.map(String::valueOf)
				.map(element -> "'" + element + "'")
				.collect(Collectors.toList());
		return columnName + " IN (" + String.join(",", convertedString) + ")";
	}

	<T> String singleToSqlString(String columnName, T inputStrings) {
		return columnName + " = " + inputStrings;
	}

}