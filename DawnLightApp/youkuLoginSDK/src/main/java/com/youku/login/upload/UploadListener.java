package com.youku.login.upload;

interface UploadListener {
	void onStart();
	void onPause();
	void onException();
	void onFinish();
	void onCancel();
	void onWait();
	void onProgressChange();
	void onUploadSpeedChange();
}
