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
import com.edian.www.base.BaseAuth;
import com.edian.www.base.BaseHandler;
import com.edian.www.base.BaseMessage;
import com.edian.www.base.BaseTask;
import com.edian.www.base.C;
import com.edian.www.list.SquareList;
import com.edian.www.list.UserList;
import com.edian.www.model.Square;
import com.edian.www.model.User;
import com.edian.www.sqlite.SquareSqlite;
import com.edian.www.util.AppCache;
import com.edian.www.util.ExitApplication;
import com.edian.www.util.StringUtils;
import com.edian.www.util.squareSortByCreateTime;
import com.edian.www.widget.PullToRefreshListView;

public class SquareIndex extends AuthApp implements OnScrollListener {
	private final String TAG = "SquareIndex";
	private PullToRefreshListView squarelistview;
	private SquareList squarelistadapter;
	private List<Square> SquareListData = new ArrayList<Square>();
	private SquareSqlite squaresqlite;
	private View loadmoreview;
	private Button loadmorebutton;
	private int visibleItemCount;
	private int visibleLastIndex;
	private boolean isfirstload = true;
	private int pageindex = 1;
	
    private View lvSquare_footer;
    private TextView lvSquare_foot_more;
    private ProgressBar lvSquare_foot_progress;
	@Override
	public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        this.setContentView(R.layout.square_index);
        //top button
		ImageButton ib = (ImageButton) this.findViewById(R.id.top_people);
		ib.setImageResource(R.drawable.people_2);
		this.setHandler(new SquareHandler(this));
		squaresqlite = new SquareSqlite(this);

		squarelistview = (PullToRefreshListView)findViewById(R.id.square_listview);
		lvSquare_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
		lvSquare_foot_more = (TextView)lvSquare_footer.findViewById(R.id.listview_foot_more);
		lvSquare_foot_progress = (ProgressBar)lvSquare_footer.findViewById(R.id.listview_foot_progress);
		squarelistview.addFooterView(lvSquare_footer);
		squarelistadapter = new SquareList(this,(ArrayList)SquareListData);
		squarelistview.setAdapter(squarelistadapter);
		squarelistview.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
			}
		});
		squarelistview.setOnScrollListener(this);
		squarelistview.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				loadSquares("1");
				isfirstload = true;
				pageindex = 1;
			}
		});
		loadSquares("1");
	}
	
	@Override
	public void onTaskComplete(int taskId,BaseMessage message){
    	super.onTaskComplete(taskId, message);
    	Log.v(TAG,"getsquare task completed");
    	switch(taskId){
    	case C.task.squarelist:
    		try{
    			lvSquare_foot_progress.setVisibility(View.GONE);
    			@SuppressWarnings("unchecked")
    			String retcode = message.getCode();
    			if(retcode.equals(C.errcode.NOT_LOGIN)){
    				overlay(AppLogin.class);
    			}
    			if(retcode.equals(C.errcode.EMPTY)){
					Log.v(TAG, "squarelist is empty");
					lvSquare_foot_more.setText(R.string.load_empty);
					lvSquare_foot_more.setVisibility(View.GONE);
					squarelistview.setTag(C.list.LISTVIEW_DATA_FULL);
    			}else{
    				this.pageindex++;
	    			final ArrayList<Square> squarelist = (ArrayList<Square>)message.getResultList("Square");
	    			for(Square square:squarelist){
	    				Log.i("square",square.getId()+"|"+square.getAuthor()+"|"+square.getAuthorFace()+"|"+square.getContent()+"|"+square.getCreatetime()+"|"+square.getClick());
	    				//loadImage(square.getAuthorFace());
	    				squaresqlite.updateSquare(square);
	    			}
	    			if(isfirstload){
	    				squarelistview.onRefreshComplete();
	    				SquareListData.clear();
	    				SquareListData.addAll(squarelist);
	    				squarelistview.setTag(C.list.LISTVIEW_DATA_MORE);
	    				isfirstload = false;
	    			}else{
						lvSquare_foot_more.setVisibility(View.GONE);
						squarelistview.setTag(C.list.LISTVIEW_DATA_MORE);
						Collections.sort(squarelist, new squareSortByCreateTime());
	    				SquareListData.addAll(squarelist);
	    				squarelistadapter.notifyDataSetChanged();
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
		visibleLastIndex = firstVisibleItem+totalItemCount-1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		squarelistview.onScrollStateChanged(view, scrollState);
		//data list is empty,return
		if(squarelistadapter.getCount()<=0)return;
		//if scrolled to the end of list
		boolean scrollEnd = false;
		int lastelem = view.getLastVisiblePosition();
		Log.v(TAG, "lastelem:"+lastelem);
		try{
			if(view.getPositionForView(lvSquare_footer)==lastelem){
				scrollEnd = true;
			}
		}catch(Exception e){
			scrollEnd = false;
		}
		int lvDataState = StringUtils.toInt(squarelistview.getTag());
		Log.v(TAG, "scrollEnd:"+scrollEnd+",lvDataState:"+lvDataState);
		if(scrollEnd && lvDataState==C.list.LISTVIEW_DATA_MORE)
		{
			int pos = squarelistadapter.getCount()-1;
			if(pos>=C.list.LIST_MAX-1)return;
			Square square = squarelistadapter.getItem(pos);
			squarelistview.setTag(C.list.LISTVIEW_DATA_LOADING);
			lvSquare_foot_more.setText(R.string.load_ing);
			lvSquare_foot_progress.setVisibility(View.VISIBLE);
			loadSquares(Integer.toString(pageindex));
		}
	}

    @Override
    public void onNetworkError(int taskId){
    	super.onNetworkError(taskId);
    	toast("网络错误，请重试");
    }
	
	public void pubSquare(View v){
		Bundle type = new Bundle();
		type.putString("pubtype", C.intent.action.PUBSQUARE);
		this.overlay(Publish.class, type);
	}
	
	private void loadSquares(String uid,String num,String page){
		Log.i(TAG, "load more square");
        HashMap<String,String> para = new HashMap<String,String>(2);
        para.put("uid", uid);
        para.put("num", num);
        para.put("page", page);
        Log.i(TAG, "[EdianIndex]taskArg1:"+para.get("cat")+"|taskArg2:"+para.get("timeline"));
        doTaskAsync(C.task.squarelist, C.api.squarelist, para);
	}
	private void loadSquares(String page){
		this.loadSquares("0", "20", page);
	}
	
	public ListView getListView(){
		return squarelistview;
	}
	private class SquareHandler extends BaseHandler {
		public SquareHandler(BaseApp app) {
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
						ImageView iv = (ImageView)squarelistview.findViewWithTag(url);
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
