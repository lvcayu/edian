package com.edian.www.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import com.edian.www.R;
import com.edian.www.util.AppCache;
import com.edian.www.util.UpdateManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

public class BaseApp extends Activity {
	private final String TAG = "BaseApp";
	protected BaseHandler handler;
	protected BaseTaskPool taskPool;
	protected boolean showLoadBar = false;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// async task handler
		this.handler = new BaseHandler(this);
		// init task pool
		this.taskPool = new BaseTaskPool(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// util method
	
	public void toast (String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
	
	public void overlay (Class<?> classObj) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setClass(this, classObj);
        startActivity(intent);
	}
	
	public void overlay (Class<?> classObj, Bundle params) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setClass(this, classObj);
        intent.putExtras(params);
        startActivity(intent);
	}
	
	public void forward (Class<?> classObj) {
		Intent intent = new Intent();
		intent.setClass(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.startActivity(intent);
		this.finish();
	}
	
	public void forward (Class<?> classObj, Bundle params) {
		Intent intent = new Intent();
		intent.setClass(this, classObj);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtras(params);
		this.startActivity(intent);
		this.finish();
	}
	
	public Context getContext () {
		return this;
	}
	
	public BaseHandler getHandler () {
		return this.handler;
	}
	
	public void setHandler (BaseHandler handler) {
		this.handler = handler;
	}
	
	public LayoutInflater getLayout () {
		return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getLayout (int layoutId) {
		return getLayout().inflate(layoutId, null);
	}
	
	public View getLayout (int layoutId, int itemId) {
		return getLayout(layoutId).findViewById(itemId);
	}
	
	public BaseTaskPool getTaskPool () {
		return this.taskPool;
	}
	
	public void showLoadBar (String url) {
		this.findViewById(R.id.load_bar).setVisibility(View.VISIBLE);
		this.findViewById(R.id.load_bar).bringToFront();
		showLoadBar = true;
	}
	
	public void hideLoadBar () {
		if (showLoadBar) {
			this.findViewById(R.id.load_bar).setVisibility(View.GONE);
			showLoadBar = false;
		}
	}
	
	public void openDialog(Bundle params) {
		new BaseDialog(this, params).show();
	}
	
	public void loadImage (final String url) {
		if(url!=null && !url.isEmpty()){
			taskPool.addTask(C.task.loadimage, new BaseTask(){
				@Override
				public void onComplete(){
					Log.w(TAG, "finish image loading");
					AppCache.getCachedImage(getContext(), url);
					sendMessage(BaseTask.LOAD_IMAGE,url);
				}
			}, 0);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// logic method
	
	public void doFinish () {
		this.finish();
	}
	
	public void doLogout () {
		BaseAuth.setLogin(false);
	}
	/*
	public void doEditText () {
		Intent intent = new Intent();
		intent.setAction(C.intent.action.EDITTEXT);
		this.startActivity(intent);
	}
	
	public void doEditText (Bundle data) {
		Intent intent = new Intent();
		intent.setAction(C.intent.action.EDITTEXT);
		intent.putExtras(data);
		this.startActivity(intent);
	}
	
	public void doEditBlog () {
		Intent intent = new Intent();
		intent.setAction(C.intent.action.EDITBLOG);
		this.startActivity(intent);
	}
	
	public void doEditBlog (Bundle data) {
		Intent intent = new Intent();
		intent.setAction(C.intent.action.EDITBLOG);
		intent.putExtras(data);
		this.startActivity(intent);
	}
	*/
	public void sendMessage (int what) {
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
	}
	
	public void sendMessage (int what, String data) {
		Bundle b = new Bundle();
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
	}
	
	public void sendMessage (int what, int taskId, String data) {
		Bundle b = new Bundle();
		b.putInt("task", taskId);
		b.putString("data", data);
		Message m = new Message();
		m.what = what;
		m.setData(b);
		handler.sendMessage(m);
	}
	
	public void doTaskAsync (int taskId, int delayTime) {
		taskPool.addTask(taskId, new BaseTask(){
			@Override
			public void onComplete () {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), null);
			}
			@Override
			public void onError (String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
			}
		}, delayTime);
	}
	
	public void doTaskAsync (int taskId, BaseTask baseTask, int delayTime) {
		taskPool.addTask(taskId, baseTask, delayTime);
	}
	
	public void doTaskAsync (int taskId, String taskUrl) {
		showLoadBar(taskUrl);
		taskPool.addTask(taskId, taskUrl, new BaseTask(){
			@Override
			public void onComplete (String httpResult) {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
			}
			@Override
			public void onError (String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
			}
		}, 0);
	}
	
	public void doTaskAsync (int taskId, String taskUrl, HashMap<String, String> taskArgs) {
		
		showLoadBar(taskUrl);
		Log.i(TAG, "[BaseApp]taskArg1:"+taskArgs.toString());
		taskPool.addTask(taskId, taskUrl, taskArgs, new BaseTask(){
			@Override
			public void onComplete (String httpResult) {
				sendMessage(BaseTask.TASK_COMPLETE, this.getId(), httpResult);
			}
			@Override
			public void onError (String error) {
				sendMessage(BaseTask.NETWORK_ERROR, this.getId(), null);
			}
		}, 0);
		
	}
	
	public void onTaskComplete (int taskId, BaseMessage message) {
	}
	
	public void onNetworkError (int taskId) {
		toast(C.err.network);
	}
	
	/**
	 * 把城市代码转换成中文名称
	 * @param citycode
	 * @return String
	 */
	public String getCity(int citycode){
		String[] citys = getResources().getStringArray(R.array.city);
		return citys[citycode];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// common classes
	
	public class BitmapViewBinder implements ViewBinder {
		// 
		@Override
		public boolean setViewValue(View view, Object data, String textRepresentation) {
			if ((view instanceof ImageView) & (data instanceof Bitmap)) {
				ImageView iv = (ImageView) view;
				Bitmap bm = (Bitmap) data;
				iv.setImageBitmap(bm);
				return true;
			}
			return false;
		}
	}
}