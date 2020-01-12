package com.javapid.entity.enums;

public enum TicketTypes implements GetterValue, GetterColumn {

	FIFTEEN_MINUTES("15 Minútové", "stvrt_minut", "FifteenMinutes"),
	ONE_DAY("24 Hodinový - Pásmo P", "jeden_den", "OneDay"),
	ONE_DAY_ALL("24 Hodinový - Všetky pásma", "jeden_den_vsetky", "OneDayAll"),
	TWO_ZONES("2 Pásma", "dve_pasma", "TwoZones"),
	THREE_ZONES("3 Pásma", "tri_pasma", "ThreeZones"),
	FOUR_ZONES("4 Pásma", "styri_pasma", "FourZones"),
	FIVE_ZONES("5 Pásiem", "pat_pasem", "FiveZones"),
	SIX_ZONES("6 Pásiem", "sest_pasem", "SixZones"),
	SEVEN_ZONES("7 Pásiem", "sedem_pasem", "SevenZones"),
	EIGHT_ZONES("8 Pásiem", "osem_pasem", "EightZones"),
	NINE_ZONES("9 Pásiem", "devat_pasem", "NineZones"),
	TEN_ZONES("10 Pásiem", "desat_pasem", "TenZones"),
	ELEVEN_ZONES("11 Pásiem", "jedenast_pasem", "ElevenZones");

	public final String value;
	public final String column;
	public final String methodName;

	private TicketTypes(String value, String column, String methodName) {
		this.value = value;
		this.column = column;
		this.methodName = methodName;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getColumn() {
		return column;
	}
}