/**
 * 
 */
package com.edian.www.app;

import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.edian.www.util.AppCache;
import com.edian.www.base.BaseApp;
import com.edian.www.base.BaseHandler;
import com.edian.www.base.BaseMessage;
import com.edian.www.base.BaseTask;
import com.edian.www.R;
import com.edian.www.auth.AuthApp;
import com.edian.www.base.C;
import com.edian.www.model.User;
import com.edian.www.util.ExitApplication;

/**
 * @author walker
 *
 */
public class UpdateInfo extends AuthApp {
	private final String TAG = "UserDetail";
	private String ms_Uid = "";
	private String faceurl = "";
	private String ms_nickname = "";
	private String ms_sex = "";

	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        setContentView(R.layout.update_profile);
        this.setHandler(new IndexHandler(this));
        
		/*Intent intent = this.getIntent();
		Bundle data = intent.getExtras();
        ms_Uid = data.getString("uid");
        HashMap<String,String> para = new HashMap<String,String>();
        para.put("uid", ms_Uid);
        para.put("type", "0");
        doTaskAsync(C.task.userview,C.api.userview,para);*/
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
	}
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		Log.i(TAG, "get userview complete");
		switch (taskId){
		case C.task.userview:
			try{
				String ret = message.getCode();
				if(ret.equals(C.errcode.SUCCESS)){

				}else if(ret.equals(C.errcode.USER_BANED)){
					toast(C.err.userbaned);
				}else{
					toast(C.err.getuserfailed);
				}
			}catch (Exception e){
				e.printStackTrace();
				//Log.i("JSONERR", e.getMessage());
			}
		}
	}

    @Override
    public void onNetworkError(int taskId){
    	super.onNetworkError(taskId);
    	toast("NetworkError!");
    }
    
	private class IndexHandler extends BaseHandler {
		public IndexHandler(BaseApp app) {
			super(app);
		}
		@Override
		public void handleMessage(Message msg) {
			Log.v(TAG, "handleMessage");
			super.handleMessage(msg);
			try {
				switch (msg.what) {
					case BaseTask.LOAD_IMAGE:
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				app.toast(e.getMessage());
			}
		}
	}
}
