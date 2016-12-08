package com.lenovo.newsclient;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.loopj.android.image.SmartImageView;

public class MainActivity extends SlidingActivity {
	String path = "http://v.juhe.cn/toutiao/index?type=&key=ee7df40d01c7d8a3a65b4ce6932ce34d";
	LinkedList<NewsBean> list = new LinkedList<NewsBean>();
	private PullToRefreshListView listView;
	Intent intent;
	TextView textView;
	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			listView.setAdapter(new madapter());
			newsClick();
		};
	};
	private ImageView leftMenu;
	private SlidingMenu slidingMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		BindID();
		slidingMenu = getSlidingMenu();
		setBehindContentView(R.layout.left_layout);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffset(300);
		getDataFromServer();
		listViewListener();

	}

	private void BindID() {
		listView = (PullToRefreshListView) findViewById(R.id.main_listview);
		leftMenu = (ImageView) findViewById(R.id.left_menu);
		leftMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				slidingMenu.showMenu();
			}
		});
	}

	private void listViewListener() {
		// listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
		//
		// @Override
		// public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		//
		// // }
		//
		// });
	}

	protected void newsClick() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// PullToRefreshListView是从1开始计算位置的
				skip(arg2 - 2);
			}
		});
	}

	protected void skip(int num) {
		intent = new Intent(MainActivity.this, NewsExploreActivity.class);
		intent.putExtra("path", list.get(num).url);
		startActivity(intent);
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				try {
					parse(responseInfo.result);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_LONG)
						.show();
			}

		});
	}

	protected void parse(String jsonData) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonData);
		JSONObject jsonObject2 = jsonObject.getJSONObject("result");
		JSONArray jsonArray = jsonObject2.getJSONArray("data");
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject3;
			NewsBean newsBean = new NewsBean();
			jsonObject3 = jsonArray.getJSONObject(i);
			newsBean.title = jsonObject3.getString("title");
			newsBean.date = jsonObject3.getString("date");
			newsBean.thumbnail_pic_s = jsonObject3.getString("thumbnail_pic_s");
			newsBean.url = jsonObject3.getString("url");
			list.add(newsBean);
		}
		handler.sendEmptyMessage(0);

	}

	class madapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			NewsBean newsBean = list.get(arg0);
			View view;
			viewHold vHold = new viewHold();
			if (arg1 == null) {
				view = View.inflate(MainActivity.this, R.layout.newsitem, null);
				vHold.smartImageView = (SmartImageView) view
						.findViewById(R.id.item_imageView1);
				vHold.textView = (TextView) view
						.findViewById(R.id.item_textView11);
				vHold.textView2 = (TextView) view
						.findViewById(R.id.item_textView22);
				view.setTag(vHold);
			} else {
				view = arg1;
				vHold = (viewHold) view.getTag();
			}
			vHold.smartImageView.setImageUrl(newsBean.thumbnail_pic_s);
			vHold.textView.setText(newsBean.title);
			vHold.textView2.setText(newsBean.date);
			return view;
		}

		class viewHold {
			SmartImageView smartImageView;
			TextView textView;
			TextView textView2;
		}

	}

}
