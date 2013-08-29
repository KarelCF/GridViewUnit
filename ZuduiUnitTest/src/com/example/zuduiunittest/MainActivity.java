package com.example.zuduiunittest;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.app.Activity;

public class MainActivity extends Activity {
	
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
				myPhotosAdapter.addPicToList("drawable://" + R.drawable.pic_avatar);
				myPhotosAdapter.notifyDataSetChanged();
			} 
		}
		
	}
	
	// 禁止GridView滑动
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
	if(ev.getAction() == MotionEvent.ACTION_MOVE){
		return true; //forbid its child(gridview) to scroll
		}
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
