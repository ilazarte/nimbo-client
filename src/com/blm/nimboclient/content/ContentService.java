package com.blm.nimboclient.content;

import java.util.ArrayList;

import android.content.Context;
import android.provider.MediaStore;
import android.util.Log;

import com.blm.nimboclient.Constants;
import com.blm.nimboclient.MediaVO;
import com.blm.nimboclient.content.ContentQuery.CursorFields;
import com.blm.nimboclient.content.ContentQuery.Filter;
import com.blm.nimboclient.content.ContentQuery.Mapper;
import com.google.common.net.MediaType;

/**
 * For downsampled files see resources:
 * http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
 * 
 * :Lots of other crazy stuff:
 *  http://stackoverflow.com/questions/10080940/get-image-from-thumbnails-query-on-android
 * 
 * @author perico
 *
 */
public class ContentService {

	private static final String TAG = ContentService.class.getSimpleName();
	
	public boolean isVideo(String mediaPath) {
		return mediaPath != null && mediaPath.endsWith(Constants.EXT_MP4);
	}
		
	/**
	 * Walk all images followed by all videos.
	 * @param context
	 * @param walker
	 */
	public void walk(Context context, ContentQuery.Walker<MediaVO> walker) {

		Filter<MediaVO> filter = new Filter<MediaVO>() {
			@Override
			public boolean filter(MediaVO vo) {
				return !vo.getData().contains(Constants.DIRECTORY_DCIM);
			}
		};
		
		Log.d(TAG, "walking images");
		
		ContentQuery.create(context, MediaVO.class)
			.uri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
			.projection(
				MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.DATE_MODIFIED)
			.filter(filter)
			.mapper(new Mapper<MediaVO>() {
				@Override
				public MediaVO map(CursorFields fields) {
					MediaVO vo = new MediaVO();
					vo.setType(MediaType.ANY_IMAGE_TYPE);
					vo.setId(fields.getInt(MediaStore.Images.Media._ID));
					vo.setData(fields.getString(MediaStore.Images.Media.DATA));
					vo.setDateModified(fields.getLong(MediaStore.Images.Media.DATE_MODIFIED));
					return vo;
				}
			})
			.walker(walker)
			.walk();
		
		Log.d(TAG, "walking images");
		
		ContentQuery.create(context, MediaVO.class)
			.uri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
			.projection(
				MediaStore.Video.Media._ID,
				MediaStore.Video.Media.DATA,
				MediaStore.Video.Media.DATE_MODIFIED)
			.filter(filter)
			.mapper(new Mapper<MediaVO>() {
				@Override
				public MediaVO map(CursorFields fields) {
					MediaVO vo = new MediaVO();
					vo.setType(MediaType.ANY_VIDEO_TYPE);
					vo.setId(fields.getInt(MediaStore.Video.Media._ID));
					vo.setData(fields.getString(MediaStore.Video.Media.DATA));
					vo.setDateModified(fields.getLong(MediaStore.Video.Media.DATE_MODIFIED));
					return vo;
				}
			})
			.walker(walker)
			.walk();
	}
	
	/**
	 * Get the video thumbnail.
	 * Using a new api for retrieving based on content query and provider.
	 * 
	 * Get the video thumbnail, not sure why it doesn't need a unique uri also.
	 * @see http://stackoverflow.com/questions/20464434/load-thumbnails-with-universal-image-loader-for-android
	 * @param context
	 * @param id
	 * @return
	 */
	public String getVideoThumbnailPath(Context context, int id) {
		
		MediaVO thumb = 
				ContentQuery.create(context, MediaVO.class)
				.uri(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI)
				.projection(
					MediaStore.Video.Thumbnails.DATA,
					MediaStore.Video.Thumbnails.VIDEO_ID)
				.selection("%s = %s",
					MediaStore.Video.Thumbnails.VIDEO_ID,
					id)
				.mapper(new Mapper<MediaVO>() {
					@Override
					public MediaVO map(CursorFields fields) {
						MediaVO vo = new MediaVO();
						vo.setData(fields.getString(MediaStore.Video.Thumbnails.DATA));
						return vo;
					}
				})
				.one();
		
		return thumb.getData();
	}

	/**
	 * Get all images, here more for research
	 * @param context
	 * @return
	 */
	public ArrayList<MediaVO> getAllImages(Context context) {
		
		ArrayList<MediaVO> images = 
				ContentQuery.create(context, MediaVO.class)
				.uri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
				.projection(
					MediaStore.Images.Media._ID,
					MediaStore.Images.Media.DATA,
					MediaStore.Images.Media.DATE_ADDED)
				.filter(new Filter<MediaVO>() {
					@Override
					public boolean filter(MediaVO obj) {
						return !obj.getData().contains(Constants.DIRECTORY_DCIM);
					}
				})
				.mapper(new Mapper<MediaVO>() {
					@Override
					public MediaVO map(CursorFields fields) {
						MediaVO vo = new MediaVO();
						vo.setId(fields.getInt(MediaStore.Images.Media._ID));
						vo.setData(fields.getString(MediaStore.Images.Media.DATA));
						vo.setDateModified(fields.getLong(MediaStore.Images.Media.DATE_ADDED));
						return vo;
					}
				})
				.list();
		
		return images;
	}
	
	/**
	 * Get all videos, here more for research
	 * @param context
	 * @return
	 */
	public ArrayList<MediaVO> getAllVideos(Context context) {
		ArrayList<MediaVO> videos = 
				ContentQuery.create(context, MediaVO.class)
				.uri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
				.projection(
					MediaStore.Images.Media._ID,
					MediaStore.Images.Media.DATA,
					MediaStore.Images.Media.DATE_ADDED)
				.filter(new Filter<MediaVO>() {
					@Override
					public boolean filter(MediaVO obj) {
						return !obj.getData().contains(Constants.DIRECTORY_DCIM);
					}
				})
				.mapper(new Mapper<MediaVO>() {
					@Override
					public MediaVO map(CursorFields fields) {
						MediaVO vo = new MediaVO();
						vo.setId(fields.getInt(MediaStore.Images.Media._ID));
						vo.setData(fields.getString(MediaStore.Images.Media.DATA));
						vo.setDateModified(fields.getLong(MediaStore.Images.Media.DATE_ADDED));
						return vo;
					}
				})
				.list();
		return videos;
	}
}
