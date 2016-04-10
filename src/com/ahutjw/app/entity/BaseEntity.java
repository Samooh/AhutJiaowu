package com.ahutjw.app.entity;

import android.app.Activity;

public class BaseEntity {

	protected Activity mActivity;

	public BaseEntity() {

	};

	public BaseEntity(Activity activity) {
		mActivity = activity;
	}
}
