package com.blm.nimboclient.period;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.blm.nimboclient.DateUtils;
import com.blm.nimboclient.MediaVO;
import com.blm.nimboclient.content.ContentQuery;
import com.blm.nimboclient.content.ContentService;

/**
 * TODO add sample images for thumbnails
 * @author perico
 */
public class PeriodIndex implements ContentQuery.Walker<MediaVO> {
	
	private static final String TAG = PeriodIndex.class.getSimpleName();
	
	private DateUtils dateUtils = new DateUtils();

	private Map<Period, Period> counts = new HashMap<Period, Period>();

	private Set<Integer> years = new HashSet<Integer>();
	
	/**
	 * Count all the files and break them up by Period (year, month, count)
	 * @return
	 */
	public static PeriodIndex create(Context context) {
		
		PeriodIndex index = new PeriodIndex();
		
		ContentService cs = new ContentService();
		
		long t1 = System.currentTimeMillis();
		
		System.out.println();
		Log.d(TAG, "start walk");
		cs.walk(context, index);
		long t2 = System.currentTimeMillis();
		Log.d(TAG, "end walk, total millis: " + (t2 - t1));

		return index;
	}
	
	@Override
	public void walk(MediaVO mvo) {

		Long seconds = mvo.getDateModified();
		Long millis = seconds * 1000l;
		
		Integer year = dateUtils.getYear(millis);
		Integer month = dateUtils.getMonth(millis);
		
		/*
		 * Log.d(TAG, "seconds:" + seconds + ", millis: " + millis);
		 * Log.d(TAG, "year, month:" + year + ", " + month + mvo);
		 */
		years.add(year);
		
		/*
		 * Arg mutability.
		 */
		Period vo = new Period(year, month);
		Period value = null;
		if (counts.containsKey(vo)) {
			value = counts.get(vo);
		} else {
			value = vo;
			counts.put(value, value);
		}
		value.increment(mvo);
	}

	/**
	 * Return a new list of counts in ascending order.
	 * This is the ideal order since it gives us the older files first.
	 * @return
	 */
	public ArrayList<Period> getCounts() {
		ArrayList<Period> list = new ArrayList<Period>(counts.keySet());
		Collections.sort(list, PeriodDateComparator.INSTANCE);
		return list;
	}
	
	/**
	 * Get the total media from the period
	 * @return
	 */
	public Integer getTotal() {
		Integer total = 0;
		for (Period vo : counts.values()) {
			total += vo.getCount();
		}
		return total;
	}
	
	/**
	 * Get the count by year and month
	 * @param year
	 * @param month
	 * @return
	 */
	public Integer get(Integer year, Integer month) {
		Period vo = new Period(year, month);
		if (counts.containsKey(vo)) {
			return counts.get(vo).getCount();
		}
		return 0;
	}
	
	/**
	 * Get all the years in this set.
	 * @return
	 */
	public ArrayList<Integer> getYears() {
		ArrayList<Integer> yearlist = new ArrayList<Integer>(this.years);
		Collections.sort(yearlist);
		return yearlist;
	}
	
	/**
	 * Return a new list of month periods by year
	 * @param year
	 * @return
	 */
	public ArrayList<Period> getMonths(Integer year) {
		
		ArrayList<Period> filteredcounts = this.getCounts();
		Iterator<Period> iterator = filteredcounts.iterator();
		
		while (iterator.hasNext()) {
			Period next = iterator.next();
			if (!next.getYear().equals(year)) {
				iterator.remove();
			}
		}
		
		return filteredcounts;
	}
}
