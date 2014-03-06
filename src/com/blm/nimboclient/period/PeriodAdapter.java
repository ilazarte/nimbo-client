package com.blm.nimboclient.period;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blm.nimboclient.DateUtils;
import com.blm.nimboclient.MediaVO;
import com.blm.nimboclient.R;
import com.blm.nimboclient.content.ContentService;
import com.google.common.net.MediaType;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class PeriodAdapter extends ArrayAdapter<Period> {

	private static final String TAG = PeriodAdapter.class.getSimpleName();
	
	private static final String COMMA = ",";
	
	private static final String SPACE = " ";

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private AnimateFirstDisplayListener animateFirstListener = new AnimateFirstDisplayListener();
	
	private DateUtils dateUtils = new DateUtils();
	
	private ContentService contentService = new ContentService();
	
	private Context context;
		
	static class ViewHolder {
		TextView month;
		TextView count;
		ImageView image1;
		ImageView image2;
		ImageView image3;
	}

	private LayoutInflater inflater;

	private ArrayList<Period> items;

	private DisplayImageOptions options;
	
	public PeriodAdapter(Context context, PeriodIndex index) {

		super(context, 0, index.getCounts());
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
    	this.options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_stub)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.ic_error)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.build();
		
		this.items = index.getCounts();
		
		this.context = context;
		/*
		 * TODO use only while we have to debug the ability of the app to find/load images
		 */
		for (int i = 0; i < this.items.size(); i++) {
			Period period = this.items.get(i);
			Log.d(TAG, "item:" + period);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
				
		ViewHolder holder;
		
		if (convertView == null) {
			
			convertView = inflater.inflate(R.layout.media_by_period_list_item, parent, false);
			
			holder = new ViewHolder();
			holder.month = (TextView) convertView.findViewById(R.id.media_by_period_list_item_month);
			holder.count = (TextView) convertView.findViewById(R.id.media_by_period_list_item_count);
			holder.image1 = (ImageView) convertView.findViewById(R.id.media_by_period_list_item_image_1);
			holder.image2 = (ImageView) convertView.findViewById(R.id.media_by_period_list_item_image_2);
			holder.image3 = (ImageView) convertView.findViewById(R.id.media_by_period_list_item_image_3);
			
			convertView.setTag(holder);
			
		} else {
			
			holder = (ViewHolder) convertView.getTag();
		}

		Period period = items.get(position);
		String month = dateUtils.getMonthName(period.getMonth()) + COMMA + SPACE + period.getYear();
		
		holder.month.setText(month);
		holder.count.setText(period.getCount().toString() + " images");

		imageLoader.displayImage(path(period.sample(0)), holder.image1, options, animateFirstListener);
		imageLoader.displayImage(path(period.sample(1)), holder.image2, options, animateFirstListener);
		imageLoader.displayImage(path(period.sample(2)), holder.image3, options, animateFirstListener);
		
		return convertView;
	}
	
	/**
	 * Convert the item to a uri.
	 * @param mvo
	 * @return
	 */
	private String path(MediaVO mvo) {
		
		String path = null;
		
		if (mvo == null) {
			return path;
		}
		
		if (MediaType.ANY_VIDEO_TYPE.equals(mvo.getType())) {
			path = contentService.getVideoThumbnailPath(context, mvo.getId());
			Log.i(TAG, "loading video tn uri: " + path);
		} else {
			path = mvo.getData();
			Log.i(TAG, "loading image uri: " + path);
		}

		
				
		return "file://" + path;
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
