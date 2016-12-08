package com.lenovo.newsclient;

public class WeathBean {
	public Result result;

	class Result {
		public Future future;
		public Today today;
	}

	public class Future {
		public DAY_20161126 day_20161126;
	}

	public class Today {

		public String city;
		public String dressing_advice;

	}

	public class DAY_20161126 {

		public String temperature;
		public String weather;
	}

}
