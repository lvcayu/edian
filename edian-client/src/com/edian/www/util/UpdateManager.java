package com.edian.www.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.edian.www.R;
import com.edian.www.app.AppLogin;
import com.edian.www.base.C;
import com.edian.www.service.ParseXmlService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 *@author coolszy
 *@date 2012-4-26
 *@blog http://blog.92coding.com
 */

public class UpdateManager
{
	private final String TAG = "UpdateManager";
	
	private SharedPreferences settings = null;
	//“一周内不再提醒”是否被选中
	private boolean isnotip = false;
	//更新内容
	private String upmsg = "";
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context,SharedPreferences setting)
	{
		this.mContext = context;
		this.settings = setting;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate()
	{
		if(this.isShouldUpdate()){
			if (isUpdate())
			{
				// 显示提示对话框
				showNoticeDialog();
			} else
			{
				//Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 检查软件是否有更新版本
	 * 
	 * @return
	 */
	private boolean isUpdate()
	{
		// 获取当前软件版本
		int versionCode = getVersionCode(mContext);
		// 把version.xml放到网络上，然后获取文件信息
		URL url;
		try
		{
			url = new URL(C.api.updateversion);
			// 创建连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			InputStream inStream = conn.getInputStream();
			//String sxml = StreamToString.ConvertToString(inStream);
			//Log.v("versionxml", StreamToString.ConvertToString(inStream));
			// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
			ParseXmlService service = new ParseXmlService();
			mHashMap = service.parseXml(inStream);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (null != mHashMap)
		{
			//Toast.makeText(mContext, "ServerVersion:"+mHashMap.get("version"), Toast.LENGTH_LONG).show();
			int serviceCode = Integer.valueOf(mHashMap.get("version"));
			// 版本判断
			if (serviceCode > versionCode)
			{
				upmsg = (String)mHashMap.get("message");
				//Log.v("message", upmsg);
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context)
	{
		int versionCode = 0;
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo("com.edian.www", 0).versionCode;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog()
	{
		final boolean[] flags=new boolean[]{false};//初始复选情况
		// 构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_update_title);
		//builder.setMessage(R.string.soft_update_info);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View updateView = inflater.inflate(R.layout.updatetip, null);
		CheckBox isdelay = (CheckBox)updateView.findViewById(R.id.update_delay);
        builder.setView(updateView);
        CheckBox CB_isdelay = (CheckBox)updateView.findViewById(R.id.update_delay);
        CB_isdelay.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
        	@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        		Log.v(TAG, "don't pop update tip|isChecked"+isChecked);
				isnotip = isChecked;
			}
        });
        TextView tv_msg = (TextView)updateView.findViewById(R.id.update_msg);
        tv_msg.setText(upmsg);
		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//setNotip();
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
			}
		});
		// 稍后更新
		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				setNotip();
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 *@date 2012-4-26
	 *@blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("url"));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("name"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
	/*
	 * 是否需要去检查更新
	 */
	private boolean isShouldUpdate(){
		if(settings.getBoolean("isdelay", true)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			try {
				Date lastupdate = sdf.parse(settings.getString("lastupdate", "1970-01-01 08:00:00"));
				Log.v(TAG, "lastupdate:"+lastupdate.toLocaleString());
				long div = now.getTime()-lastupdate.getTime();
				if(div/(1000*60*60*24)<=7)
					return false;
			} catch (ParseException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	/*
	 * 一周内不再提醒
	 */
	private void setNotip(){
		if(isnotip){
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("isdelay", true);
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			String ds = sf.format(now);
			Log.v(TAG, "ds:"+ds);
			editor.putString("lastupdate", ds);
			editor.commit();
		}
	}
}
