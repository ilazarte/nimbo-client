package com.blm.nimboclient;

import com.google.common.net.MediaType;

/**
 * Equality is id and type.
 */
public class MediaVO {

	private int id;
	
	private MediaType type;
	
	private String data;
	
	private Long dateModified;
	
	public MediaVO() {
		super();
	}

	public MediaVO(int id, MediaType type, String data, Long dateAdded) {
		super();
		this.id = id;
		this.type = type;
		this.data = data;
		this.dateModified = dateAdded;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MediaType getType() {
		return type;
	}

	public void setType(MediaType type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String path) {
		this.data = path;
	}

	public Long getDateModified() {
		return dateModified;
	}

	public void setDateModified(Long dateAdded) {
		this.dateModified = dateAdded;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		MediaVO other = (MediaVO) obj;
		if (id != other.id)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MediaVO [id=" + id + ", type=" + type + ", data=" + data
				+ ", dateModified=" + dateModified + "]";
	}
}
