package com.blm.nimboclient;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class DateUtils {

	private DateFormatSymbols dfs = new DateFormatSymbols();
	
	public static void main(String[] args) {
		
		DateUtils du = new DateUtils();
		System.out.println(du.getMonth(1389255325));
		System.out.println(du.getYear(1389255325));
	}
	
	/**
	 * Turn the month 0-11 into a name.
	 * @param month
	 * @return
	 */
	public String getMonthName(Integer month) {
		String name = dfs.getMonths()[month];
		return name;
	}
	
	/**
	 * Return the year from the millis
	 * @param millis
	 * @return
	 */
	public Integer getYear(long millis) {
		return getField(Calendar.YEAR, millis);
	}
	
	/**
	 * Get the month from millis
	 * @param millis
	 * @return
	 */
	public Integer getMonth(long millis) {
		return getField(Calendar.MONTH, millis);
	}
	
	private Integer getField(int field, long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		int value = cal.get(field);
		return value;
	}
}
