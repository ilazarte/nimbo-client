package com.blm.nimboclient.period;

import java.util.Comparator;

public class PeriodDateComparator implements Comparator<Period> {
	
	public static final Comparator<Period> INSTANCE = new PeriodDateComparator();
	
	@Override
	public int compare(Period p0, Period p1) {
		Integer res = p0.getYear().compareTo(p1.getYear());
		if (res != 0) {
			return res;
		}
		res = p0.getMonth().compareTo(p1.getMonth());
		if (res != 0) {
			return res;
		}
		
		return res;
	}
}