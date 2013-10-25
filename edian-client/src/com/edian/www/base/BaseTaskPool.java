package com.edian.www.base;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.util.Log;
import com.edian.www.util.HttpUtil;

import com.edian.www.util.AppClient;

public class BaseTaskPool {
	private final String TAG = "BaseTaskPool";
	static private ExecutorService taskPool;
	private Context context; // used for HttpUtil.getNetType
	
	public BaseTaskPool (BaseApp app) {
		this.context = app.getContext();
		taskPool = Executors.newCachedThreadPool();
	}
	
	// http post task with params
	public void addTask (int taskId, String taskUrl, HashMap<String, String> taskArg, BaseTask baseTask, int delayTime) {
		baseTask.setId(taskId);
		try {
			taskPool.execute(new TaskThread(context, taskUrl, taskArg, baseTask, delayTime));
		} catch (Exception e) {
			Log.w("BaseTaskPool", "taskpool.shutdown|taskId="+taskId);
			taskPool.shutdown();
		}
	}
	
	// http post task without params
	public void addTask (int taskId, String taskUrl, BaseTask baseTask, int delayTime) {
		baseTask.setId(taskId);
		try {
			taskPool.execute(new TaskThread(context, taskUrl, null, baseTask, delayTime));
		} catch (Exception e) {
			Log.w("BaseTaskPool", "taskpool.shutdown|taskId="+taskId);
			taskPool.shutdown();
		}
	}
	
	// custom task
	public void addTask (int taskId, BaseTask baseTask, int delayTime) {
		baseTask.setId(taskId);
		try {
			taskPool.execute(new TaskThread(context, null, null, baseTask, delayTime));
		} catch (Exception e) {
			Log.w("BaseTaskPool", "taskpool.shutdown|taskId="+taskId);
			taskPool.shutdown();
		}
	}
	
	public static class TaskThread implements Runnable {
		private Context context;
		private String taskUrl;
		private HashMap<String, String> taskArg;
		private BaseTask baseTask;
		private int delayTime = 0;
		public TaskThread(Context context, String taskUrl, HashMap<String, String> taskArg, BaseTask baseTask, int delayTime) {
			this.context = context;
			this.taskUrl = taskUrl;
			this.taskArg = taskArg;
			this.baseTask = baseTask;
			this.delayTime = delayTime;
			Log.i("taskthread", "taskUrl:"+taskUrl+" taskId:"+baseTask.getId());
		}
		@Override
		public void run() {
			try {
//				Looper.prepare();
				Log.i("taskthread", "run()|"+baseTask.getId());
				baseTask.onStart();
				String httpResult = null;
				// set timeout
				if (this.delayTime > 0) {
					Thread.sleep(this.delayTime);
				}
				try {
					// remote task
					if (this.taskUrl != null) {
						// init app client
						Log.i("BaseTaskPool", "taskUrl:"+this.taskUrl);
						AppClient client = new AppClient(this.taskUrl);
						if (HttpUtil.WAP_INT == HttpUtil.getNetType(context)) {
							client.useWap();
						}
						// http get
						if (taskArg == null) {
							httpResult = client.get();
						// http post
						} else {
							httpResult = client.post(this.taskArg);
						}
					}
					// remote task
					if (httpResult != null) {
						baseTask.onComplete(httpResult);
					// local task
					} else {
						baseTask.onComplete();
					}
				} catch (Exception e) {
					baseTask.onError(e.getMessage());
				}
				baseTask.onStop();
			} catch (Exception e) {
				Log.w("BaseTaskPool", "run exception"+e.getMessage());
				e.printStackTrace();
			} finally {
//				Looper.loop();
			}
		}
	}
	
}