package com.edian.www.base;

import com.edian.www.util.AppUtil;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class BaseHandler extends Handler {
	
	protected BaseApp app;
	
	public BaseHandler (BaseApp app) {
		this.app = app;
	}
	
	public BaseHandler (Looper looper) {
		super(looper);
	}
	
	@Override
	public void handleMessage(Message msg) {
		try {
			int taskId;
			String result;
			switch (msg.what) {
				case BaseTask.TASK_COMPLETE:
					app.hideLoadBar();
					taskId = msg.getData().getInt("task");
					result = msg.getData().getString("data");
					if (result != null) {
						Log.i("TRACE-TASKID", Integer.toString(taskId));
						app.onTaskComplete(taskId, AppUtil.getMessage(result));
					} else {
						app.toast(C.err.message);
					}
					break;
				case BaseTask.NETWORK_ERROR:
					app.hideLoadBar();
					taskId = msg.getData().getInt("task");
					app.onNetworkError(taskId);
					break;
				case BaseTask.SHOW_LOADBAR:
					app.showLoadBar("test");
					break;
				case BaseTask.HIDE_LOADBAR:
					app.hideLoadBar();
					break;
				case BaseTask.SHOW_TOAST:
					app.hideLoadBar();
					result = msg.getData().getString("data");
					app.toast(result);
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//app.toast(e.getMessage());
		}
	}
	
}