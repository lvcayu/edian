package com.edian.www.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.edian.www.util.StringUtils;

import com.edian.www.util.AppCache;
import com.edian.www.base.BaseApp;
import com.edian.www.base.BaseHandler;
import com.edian.www.base.BaseTask;
import com.edian.www.R;
import com.edian.www.auth.AuthApp;
import com.edian.www.base.BaseMessage;
import com.edian.www.base.C;
import com.edian.www.list.UserList;
import com.edian.www.model.User;
import com.edian.www.sqlite.UserSqlite;
import com.edian.www.util.ExitApplication;
import com.edian.www.util.userSortByRegTime;
import com.edian.www.widget.PullToRefreshListView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class EdianIndex extends AuthApp implements OnScrollListener{
	private final String TAG = "EdianIndex";
	private PullToRefreshListView userlistview;
	private UserList userlistadapter;
	private List<User> userlist = new ArrayList<User>();
	private UserSqlite usersqlite;
    private int visibleLastIndex = 0;   //最后的可视项索引
    private int visibleItemCount;       // 当前窗口可见项总数
    private String Sex = "";
    private boolean isfirstload = true;
    private int pageindex = 1;
    
    private View lvNewuser_footer;
    private TextView lvNewuser_foot_more;
    private ProgressBar lvNewuser_foot_progress;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        
        setContentView(R.layout.edian_index);
		// top button
		ImageButton ib = (ImageButton) this.findViewById(R.id.top_new);
		ib.setImageResource(R.drawable.new_2);
		
        this.setHandler(new IndexHandler(this));
        usersqlite = new UserSqlite(this);
        Log.v(TAG, "create!");
        
        
        if(user.getSex().equalsIgnoreCase(C.con.female))
        	Sex = C.con.male;
        else Sex = C.con.female;
        
		userlistview = (PullToRefreshListView)findViewById(R.id.newuser_listview);
        lvNewuser_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        lvNewuser_foot_more = (TextView)lvNewuser_footer.findViewById(R.id.listview_foot_more);
        lvNewuser_foot_progress = (ProgressBar)lvNewuser_footer.findViewById(R.id.listview_foot_progress);
		userlistadapter = new UserList(this,(ArrayList)userlist);
		userlistview.addFooterView(lvNewuser_footer);
		userlistview.setAdapter(userlistadapter);
		userlistview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
        		//点击头部、底部栏无效
        		if(pos == 0 || view == lvNewuser_footer) return;
				Bundle bd = new Bundle();
				Log.v(TAG, "pos:"+pos);
				bd.putString("uid",userlistadapter.getItem(pos-1).getUid());
				//Log.i(TAG,"newuser uid:"+userlistadapter.getItem(pos).getUid());
				overlay(UserDetail.class,bd);
			}
		});
		userlistview.setOnScrollListener(this);
		userlistview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				loadUsers(Sex,"1");
				isfirstload=true;//下拉刷新
				pageindex = 1;
			}
		});        
        loadUsers(Sex,"1");
    }
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	@Override
	public void onScroll(AbsListView view,int firstVisiableItem,int visibleItemCount,int totalItemCount){
		userlistview.onScroll(view, firstVisiableItem, visibleItemCount, totalItemCount);
		this.visibleItemCount = visibleItemCount;
		this.visibleLastIndex = firstVisiableItem+totalItemCount-1;
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view,int scrollState){
		Log.v(TAG, "scroll!");
		userlistview.onScrollStateChanged(view, scrollState);
		//数据为空--不用继续下面代码了
		if(userlistadapter.getCount()<=0) return;
		
		//判断是否滚动到底部
		boolean scrollEnd = false;
		int lastelem = view.getLastVisiblePosition();
		Log.v(TAG, "lastelem:"+lastelem);
		try {
			if(view.getPositionForView(lvNewuser_footer) == lastelem)
				scrollEnd = true;
		} catch (Exception e) {
			scrollEnd = false;
		}
		int lvDataState = StringUtils.toInt(userlistview.getTag());
		Log.v(TAG, "scrollEnd:"+scrollEnd+",lvDataState:"+lvDataState);
		if(scrollEnd && lvDataState==C.list.LISTVIEW_DATA_MORE)
		{
			int pos = userlistadapter.getCount()-1;
			if(pos>=C.list.LIST_MAX-1)return;
			User user = userlistadapter.getItem(pos);
			userlistview.setTag(C.list.LISTVIEW_DATA_LOADING);
			lvNewuser_foot_more.setText(R.string.load_ing);
			lvNewuser_foot_progress.setVisibility(View.VISIBLE);
			loadUsers(Sex, Integer.toString(this.pageindex));
		}
	}
	
    @Override
    public void onTaskComplete(int taskId,BaseMessage message){
    	super.onTaskComplete(taskId, message);
    	Log.v(TAG,"getuser task completed");
    	switch(taskId){
    	case C.task.userlist:
    		try{
    			lvNewuser_foot_progress.setVisibility(ProgressBar.GONE);
    			//userlistview.onRefreshComplete();
    			@SuppressWarnings("unchecked")
    			String retcode = message.getCode();
    			if(retcode.equals(C.errcode.NOT_LOGIN)){
    				forward(AppLogin.class);
    			}
    			if(retcode.equals(C.errcode.EMPTY)){
					Log.v(TAG, "nuserlist is empty!");
					lvNewuser_foot_more.setText(R.string.load_empty);
					lvNewuser_foot_more.setVisibility(View.GONE);
					userlistview.setTag(C.list.LISTVIEW_DATA_FULL);
    			}else{
    				this.pageindex++;
	    			List<User> nuserlist = (ArrayList<User>)message.getResultList("User");
	    			for(User user:nuserlist){
	    				Log.i("user",user.getNickname()+"|"+user.getAge()+"|"+user.getSex()+"|"+user.getLastlogintime()+"|"+user.getHeartsay());
	    				//loadImage(user.getFaceurl());
	    				usersqlite.updateUser(user);
	    			}
	    			if(isfirstload){//第一次加载
	    				userlistview.onRefreshComplete(/*getString(R.string.pull_to_refresh_update)+new Date().toLocaleString()*/);
	    				userlist.clear();
	    				Collections.sort(nuserlist,new userSortByRegTime());
	    				userlist.addAll(nuserlist);
		    			userlistview.setTag(C.list.LISTVIEW_DATA_MORE);
		    			isfirstload = false;
		    			
	    			}else{
	    				Log.v(TAG, "加载更多完成");
    					Log.v(TAG, "nuserlistE:"+nuserlist.get(0).getNickname());
    					lvNewuser_foot_more.setVisibility(View.GONE);
    					userlistview.setTag(C.list.LISTVIEW_DATA_MORE);
        				Collections.sort(nuserlist, new userSortByRegTime());
        				userlist.addAll(nuserlist);
        				userlistadapter.notifyDataSetChanged();
	    			
	    			}
    			}
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }
    
    @Override
    public void onNetworkError(int taskId){
    	super.onNetworkError(taskId);
    	userlistview.onRefreshComplete(C.err.network);
    	toast("NetworkError!");
    }
    
    public void loadUsers(String sex,String index){
        HashMap<String,String> para = new HashMap<String,String>(2);
        para.put("cat", sex);
        para.put("page", index);
        //Log.i(TAG, "[onRefresh]taskArg1:"+para.get("cat")+"|taskArg2:"+para.get("page"));
        doTaskAsync(C.task.userlist, C.api.userlist, para);
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
						Bundle bd = msg.getData();
						String url = bd.getString("url");
						ImageView iv = (ImageView)userlistview.findViewWithTag(url);
						Bitmap face = AppCache.getImage(url);
						iv.setImageBitmap(face);
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				app.toast(e.getMessage());
			}
		}
	}
}