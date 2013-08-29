package com.example.zuduiunittest;

import java.util.List;

import com.example.zuduiunittest.Constants.Extra;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {
	
	private static final int IMAGE_CODE = 1000;
	
	private GridView photosGridView = null;
	private RelativeLayout photoAreaTextLayout = null;
	private RelativeLayout photoAreaGridLayout = null;
	private MyPhotosAdatper myPhotosAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		photosGridView = (GridView) findViewById(R.id.myspace_gridview_photos);
		photoAreaGridLayout = (RelativeLayout) findViewById(R.id.photo_area_grid_layout);
		photoAreaTextLayout = (RelativeLayout) findViewById(R.id.photo_area_text_layout);
		
		photoAreaTextLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				photoAreaGridLayout.setVisibility(View.VISIBLE);
				photoAreaTextLayout.setVisibility(View.GONE);
				
			}
		});
		myPhotosAdapter = new MyPhotosAdatper(this);
		photosGridView.setAdapter(myPhotosAdapter);
		photosGridView.setOnItemClickListener(new ItemClickListener());
		
	}
	
	
	
	private class ItemClickListener implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position == myPhotosAdapter.getCount() - 1 ) {
				// TODO 调用本机相册(相机拍照)
				getPhotosFromAlbum();
			} else {
				startPhotosPagerActivity(position);
			}
		}
		
	}
	
	private void getPhotosFromAlbum() {
		Intent getAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
		getAlbumIntent.setType("image/*");
		MainActivity.this.startActivityForResult(getAlbumIntent, IMAGE_CODE);
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {    
		    Toast.makeText(this, "未读取照片", Toast.LENGTH_SHORT).show();
		    return;
	    }
		
		if (requestCode == IMAGE_CODE) {
            Uri photoUri = data.getData(); // 获得图片的uri 
            String PhotoUriString = photoUri.toString();
			myPhotosAdapter.addPicToList(PhotoUriString);
			myPhotosAdapter.notifyDataSetChanged();
		}
	}



	private void startPhotosPagerActivity(int position) {
		List<String> photoUrlList = myPhotosAdapter.getPhotoUrlList();
		String[] photoUrls = (String[]) photoUrlList.toArray(new String[photoUrlList.size()]); 
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra(Extra.IMAGES, photoUrls);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		startActivity(intent);
	}
	
	// 禁止GridView滑动
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
	if(ev.getAction() == MotionEvent.ACTION_MOVE)
		return true;
	return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onResume() {
		checkPhotoListSize();
		super.onResume();
	}
	
	private void checkPhotoListSize() {
		if (myPhotosAdapter.getCount() == 1) {
			photoAreaGridLayout.setVisibility(View.GONE);
			photoAreaTextLayout.setVisibility(View.VISIBLE);
		}
	}
	
}
