package com.edian.www.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.edian.www.R;
import com.edian.www.auth.AuthApp;
import com.edian.www.base.BaseApp;
import com.edian.www.base.BaseHandler;
import com.edian.www.base.BaseMessage;
import com.edian.www.base.BaseTask;
import com.edian.www.base.C;
import com.edian.www.list.ShareList;
import com.edian.www.list.UserList;
import com.edian.www.model.Share;
import com.edian.www.model.User;
import com.edian.www.sqlite.ShareSqlite;
import com.edian.www.util.AppCache;
import com.edian.www.util.ExitApplication;
import com.edian.www.util.StringUtils;
import com.edian.www.util.shareSortByCreateTime;
import com.edian.www.widget.PullToRefreshListView;

public class ShareIndex extends AuthApp implements OnScrollListener {
	private final String TAG = "ShareIndex";
	private PullToRefreshListView sharelistview;
	private List<Share> sharelist = new ArrayList<Share>();
	private ShareList sharelistadapter;
	private ShareSqlite sharesqlite;
	private boolean isfirstload = true;
	private int pageindex = 1;
	private int visibleItemCount = 0;
	private int visibleLastIndex = 0;
	
    private View lvShare_footer;
    private TextView lvShare_foot_more;
    private ProgressBar lvShare_foot_progress;
	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        this.setContentView(R.layout.share_index);
        //top button
		ImageButton ib = (ImageButton) this.findViewById(R.id.top_share);
		ib.setImageResource(R.drawable.share_2);
		this.setHandler(new ShareHandler(this));
		sharesqlite = new ShareSqlite(this);
		sharelistview = (PullToRefreshListView)findViewById(R.id.share_listview);
		lvShare_footer = this.getLayoutInflater().inflate(R.layout.listview_footer, null);
		lvShare_foot_more = (TextView)lvShare_footer.findViewById(R.id.listview_foot_more);
		lvShare_foot_progress = (ProgressBar)lvShare_footer.findViewById(R.id.listview_foot_progress);
		sharelistview.addFooterView(lvShare_footer);
		sharelistadapter = new ShareList(this,(ArrayList)sharelist);
		sharelistview.setAdapter(sharelistadapter);
		sharelistview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
				Bundle bd = new Bundle();
				Log.v(TAG, "pos:"+pos);
				bd.putString("shareid",sharelist.get(pos-1).getId());
				overlay(ShareContent.class,bd);
			}
		});
		sharelistview.setOnScrollListener(this);
		sharelistview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				isfirstload = true;
				pageindex = 1;
				loadShare("1");
			}
		});
		loadShare("1");
	}
	
	@Override
	public void onTaskComplete(int taskId,BaseMessage message){
    	super.onTaskComplete(taskId, message);
    	Log.v(TAG,"getshare task completed");
    	switch(taskId){
    	case C.task.sharelist:
    		try{
    			lvShare_foot_progress.setVisibility(View.GONE);
    			lvShare_footer.setVisibility(View.GONE);
    			@SuppressWarnings("unchecked")
    			String retcode = message.getCode();
    			if(retcode.equals(C.errcode.NOT_LOGIN)){
    				forward(AppLogin.class);
    			}
    			if(retcode.equals(C.errcode.EMPTY)){
					Log.v(TAG, "sharelist is empty!");
					lvShare_foot_more.setText(R.string.load_empty);
					lvShare_foot_more.setVisibility(View.GONE);
					sharelistview.setTag(C.list.LISTVIEW_DATA_FULL);
    			}else{
    				this.pageindex++;
	    			final ArrayList<Share> nsharelist = (ArrayList<Share>)message.getResultList("Share");
	    			for(Share share:nsharelist){
	    				Log.i("share",share.getId()+"|"+share.getAuthor()+"|"+share.getFace()+"|"+share.getContent()+"|"+share.getCreatetime()+"|"+share.getClick());
	    				//loadImage(share.getFace());
	    				sharesqlite.updateShare(share);
	    			}
	    			if(isfirstload){
	    				sharelistview.onRefreshComplete();
	    				sharelist.clear();
	    				Collections.sort(sharelist,new shareSortByCreateTime());
	    				sharelist.addAll(nsharelist);
	    				sharelistview.setTag(C.list.LISTVIEW_DATA_MORE);
	    				isfirstload = false;
	    			}else{
	    				lvShare_foot_more.setVisibility(View.GONE);
	    				sharelistview.setTag(C.list.LISTVIEW_DATA_MORE);
	    				Collections.sort(nsharelist,new shareSortByCreateTime());
	    				sharelist.addAll(nsharelist);
	    				sharelistadapter.notifyDataSetChanged();
	    			}
    			}
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		this.visibleLastIndex = firstVisibleItem+totalItemCount-1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		Log.v(TAG, "scroll!");
		sharelistview.onScrollStateChanged(view, scrollState);
		//数据为空--不用继续下面代码了
		if(sharelistadapter.getCount()<=0) return;
		
		//判断是否滚动到底部
		boolean scrollEnd = false;
		int lastelem = view.getLastVisiblePosition();
		Log.v(TAG, "lastelem:"+lastelem);
		try {
			if(view.getPositionForView(lvShare_footer) == lastelem)
				scrollEnd = true;
		} catch (Exception e) {
			scrollEnd = false;
		}
		int lvDataState = StringUtils.toInt(sharelistview.getTag());
		Log.v(TAG, "scrollEnd:"+scrollEnd+",lvDataState:"+lvDataState);
		if(scrollEnd && lvDataState==C.list.LISTVIEW_DATA_MORE)
		{
			int pos = sharelistadapter.getCount()-1;
			if(pos>=C.list.LIST_MAX-1)return;
			Share share = sharelistadapter.getItem(pos);
			sharelistview.setTag(C.list.LISTVIEW_DATA_LOADING);
			lvShare_foot_more.setText(R.string.load_ing);
			lvShare_foot_progress.setVisibility(View.VISIBLE);
			loadShare(Integer.toString(this.pageindex));
		}
	}
	
    @Override
    public void onNetworkError(int taskId){
    	super.onNetworkError(taskId);
    	toast("网络错误，请重试");
    }
	
	public void loadShare(String page){
		this.loadShare("0", "20", page);
	}
	
	public void loadShare(String uid,String num,String page){
		Log.i(TAG, "load shares");
        HashMap<String,String> para = new HashMap<String,String>(2);
        para.put("uid", uid);
        para.put("num", num);
        para.put("page", page);
        Log.i(TAG, "[EdianIndex]taskArg1:"+para.get("cat")+"|taskArg2:"+para.get("timeline"));
        doTaskAsync(C.task.sharelist, C.api.sharelist, para);
	}
	
	public void pubShare(View v){
		Bundle type = new Bundle();
		type.putString("pubtype", C.intent.action.PUBSHARE);
		this.overlay(Publish.class, type);
	}
	
	public ListView getListView(){
		return sharelistview;
	}
	private class ShareHandler extends BaseHandler {
		public ShareHandler(BaseApp app) {
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
						ImageView iv = (ImageView)sharelistview.findViewWithTag(url);
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
