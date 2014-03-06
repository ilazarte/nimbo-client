package com.blm.nimboclient.period;

import java.util.ArrayList;

import com.blm.nimboclient.MediaVO;

/**
 * TODO add method to obtain list of images for thumbnails
 * Period is unique by year/month
 * @author perico
 */
public class Period {
	
	private static final int MAX_SAMPLES = 3;
	
	private Integer year;
	
	private Integer month;
	
	private Integer count = 0;
	
	private ArrayList<MediaVO> samples = new ArrayList<MediaVO>();
	
	public Period() {
		super();
	}

	public Period(Integer year, Integer month) {
		super();
		this.year = year;
		this.month = month;
	}

	public Period(Integer year, Integer month, Integer count) {
		super();
		this.year = year;
		this.month = month;
		this.count = count;
	}

	/**
	 * Return the samples found the pictures as file URI.
	 * @return
	 */
	public MediaVO sample(int idx) {
		if (idx >= samples.size()) {
			return null;
		}
		
		return samples.get(idx);
	}

	/**
	 * Add a counter and optionally do something with the file.
	 * @param file
	 * @return
	 */
	public Period increment(MediaVO mediaVO) {
		
		samples.add(mediaVO);
		if (samples.size() > MAX_SAMPLES) {
			samples.remove(0);
		}
		count++;
		return this;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Period copy() {
		return new Period(year, month, count);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Period other = (Period) obj;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PeriodVO [year=" + year + ", month=" + month + ", count="
				+ count + "]";
	}
}
