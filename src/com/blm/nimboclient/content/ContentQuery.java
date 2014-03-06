package com.blm.nimboclient.content;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * A class to simplify content provider query awkwardness.
 * TODO error scenarios for cursors?
 * @author perico
 *
 * @param <T>
 */
public class ContentQuery<T> {

	private Context context;
	
	private Uri uri;
	
	private String[] projection;
	
	private String selection;

	private Filter<T> filter;

	private Mapper<T> mapper;
	
	private Walker<T> walker;
	
	public static interface Mapper<T> {
		/**
		 * Using an instance of the wrapper cursor create an object of type <T>
		 * @param fields
		 * @param idx
		 * @return
		 */
		public T map(CursorFields fields);
	}
	
	public static interface Filter<T> {
		/**
		 * If true, do not add this element to a result..
		 * @param t
		 * @return
		 */
		public boolean filter(T obj);
	}
	
	public static interface Walker<T> {
		/**
		 * Pass the created instance into the callback
		 * @param obj
		 */
		public void walk(T obj);
	}
	
	public static class CursorFields {
		private Cursor cursor;

		public CursorFields(Cursor cursor) {
			super();
			this.cursor = cursor;
		}
		
		public String getString(String columnName) {
			int idx = cursor.getColumnIndex(columnName);
			return cursor.getString(idx);
		}
		
		public Long getLong(String columnName) {
			int idx = cursor.getColumnIndex(columnName);
			return cursor.getLong(idx);
		}
		
		public int getInt(String columnName) {
			int idx = cursor.getColumnIndex(columnName);
			return cursor.getInt(idx);
		}
	}
	
	public ContentQuery(Context context) {
		this.context = context;
	}
	
	public static <T> ContentQuery<T> create(Context context, Class<T> clazz) {
		return new ContentQuery<T>(context);
	}
	
	public ContentQuery<T> uri(Uri uri) {
		this.uri = uri;
		return this;
	} 
	
	public ContentQuery<T> projection(String ... projection) {
		this.projection = projection;
		return this;
	}
	
	public ContentQuery<T> selection(Object ... selection) {
		if (selection.length == 1) {
			this.selection = (String) selection[0];
		} else {
			Object[] arr = Arrays.copyOfRange(selection, 1, selection.length);
			this.selection = String.format((String) selection[0], arr);
		}
		return this;
	}
	
	public ContentQuery<T> filter(Filter<T> filter) {
		this.filter = filter;
		return this;
	}
	
	public ContentQuery<T> mapper(Mapper<T> mapper) {
		this.mapper = mapper;
		return this;
	}
	
	public ContentQuery<T> walker(Walker<T> walker) {
		this.walker = walker;
		return this;
	}
		
	/**
	 * Return the first found item or null if nto found.
	 * @return
	 */
	public T one() {
		ArrayList<T> query = this.query(true);
		if (query.size() == 1) {
			return query.get(0);
		}
		return null;
	}
	
	/**
	 * Return a list
	 * @return
	 */
	public ArrayList<T> list() {
		return this.query(false);
	}

	/**
	 * Do not return anything, simple execute the walker callback against all results.
	 * Filtered if appropriate.
	 */
	public void walk() {
		this.checkWalker();
		this.checkMapper();
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, projection, selection, null, null);
		CursorFields fields = new CursorFields(cursor);
		
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			T item = mapper.map(fields);
			if (filter == null) {
				walker.walk(item);
			} else {
				if (!filter.filter(item)) {
					walker.walk(item);	
				}				
			}
		};
		cursor.close();
	}

	private ArrayList<T> query(boolean single) {
		this.checkMapper();
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, projection, selection, null, null);
		CursorFields fields = new CursorFields(cursor);
		ArrayList<T> list = new ArrayList<T>();
		
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			T item = mapper.map(fields);
			if (filter == null) {
				list.add(item);
			} else {
				if (!filter.filter(item)) {
					list.add(item);	
				}				
			}
			if (single && list.size() == 1) {
				break;
			}
		};
		cursor.close();
		
		return list;
	}
	
	private void checkWalker() {
		if (walker == null) {
			throw new RuntimeException("No valid walker found");
		}
	}

	private void checkMapper() {
		if (mapper == null) {
			throw new RuntimeException("No valid mapper instance found");
		}
	}	
}