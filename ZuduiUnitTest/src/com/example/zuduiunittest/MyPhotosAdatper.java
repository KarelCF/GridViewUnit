package com.example.zuduiunittest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.zuduiunittest.Constants.Extra;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class MyPhotosAdatper extends BaseAdapter {
	
	private static MyPhotosAdatper myPhotosAdapter = null;
	private Context context;
	private List<String> photoUrlList = new ArrayList<String>();  
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	public MyPhotosAdatper(Context context) {
		this.context = context;
		photoUrlList.add("drawable://" + R.drawable.pic_add_photo);
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		initLoaderOptions();
		myPhotosAdapter = this;
	}
	
	public static MyPhotosAdatper getInstance() {
		return myPhotosAdapter;
	}
	
	private void initLoaderOptions() {
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisc(false)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
	}
	
	public void addPicToList(String url) {
		photoUrlList.add(url);
		moveButtonToLast();
	}
	
	private void moveButtonToLast() {
		Collections.swap(photoUrlList, photoUrlList.size() - 1, photoUrlList.size() - 2);
	}
	
	public void deletePicFromList(int position) {
		photoUrlList.remove(position);
	}
	
	@Override
	public int getCount() {
		return photoUrlList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int index = position;
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_photos_item, null, false);
			holder.photosImageView = (ImageView) convertView.findViewById(R.id.myspace_activity_imageview_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		imageLoader.displayImage(photoUrlList.get(index), holder.photosImageView, options);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (index == photoUrlList.size() - 1 ) {
					// TODO 调用本机相册(相机拍照)
					addPicToList("drawable://" + R.drawable.pic_avatar);
					notifyDataSetChanged();
				} else {
					startPhotosPagerActivity(index);
				}
			}
		});
		
		return convertView; 
	}
	
	private void startPhotosPagerActivity(int position) {
		String[] photoUrls = (String[]) photoUrlList.toArray(new String[photoUrlList.size() - 1]); 
		Intent intent = new Intent(context, ImagePagerActivity.class);
		intent.putExtra(Extra.IMAGES, photoUrls);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		context.startActivity(intent);
	}
	
	private static class ViewHolder {
		ImageView photosImageView;
	}
	
}
