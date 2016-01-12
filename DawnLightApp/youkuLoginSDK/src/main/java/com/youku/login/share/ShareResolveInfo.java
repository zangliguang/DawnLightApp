package com.youku.login.share;

import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;


/**
 * 自定义的数据模型，
 * 
 * 和系统分享数据模型ResolveInfo类似的
 * @author afei
 *
 */
public class ShareResolveInfo {
	public Drawable resolveInfoDrawable;//分享列表中的icon
	public String resolveInfoTitle;//分享列表的标题
	public String resolvePackageName;//识别分享中的包名
	public ResolveInfo resolveInfo;//系统分享中的数据模型
}
