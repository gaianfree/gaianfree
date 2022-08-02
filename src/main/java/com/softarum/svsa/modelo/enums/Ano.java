package com.softarum.svsa.modelo.enums;

public abstract class Ano {
	
	private static String[] anos = {"2019", "2020","2021", "2022","2023", "2024", "2025"};

	
	
	public static String[] getAnos() {
		return anos;
	}
	public static void setAnos(String[] anos) {
		Ano.anos = anos;
	}

}
