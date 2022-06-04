package com.charts.general.entity.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SellType {

	CARD("Čipová karta"),
	COUPON("Papierový kupón"),
	ESHOP("EShop");

	private String value;

	private SellType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static List<String> sellTypeValues() {
		return Arrays.stream(values())
				.map(SellType::getValue)
				.collect(Collectors.toList());
	}

	public static SellType sellTypeValue(String label) {
		if (label == null) {
			return null;
		}

		return Stream.of(SellType.values())
				.filter(c -> c.getValue().equals(label))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
