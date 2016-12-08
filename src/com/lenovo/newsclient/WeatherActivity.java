package com.lenovo.newsclient;

import org.apache.http.HttpException;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class WeatherActivity extends Activity {
	String url = "http://v.juhe.cn/weather/index?cityname=%E6%BD%8D%E5%9D%8A&dtype=&format=&key=9032c559b2f391a68f3db9d6fc263899";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				Log.e("eeeeeeeeee", result);
				parseData(result);
			}

			@Override
			public void onFailure(
					com.lidroid.xutils.exception.HttpException arg0, String arg1) {

			}

		});
	}

	protected void parseData(String result) {
		Gson gson = new Gson();
		WeathBean fromJson = gson.fromJson(result, WeathBean.class);
		String dressing_advice = fromJson.result.today.dressing_advice;
		String weather = fromJson.result.future.day_20161126.weather;
		Toast.makeText(this, dressing_advice, 0).show();
		Toast.makeText(this, weather, 0).show();

	}

}
