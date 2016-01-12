package com.youku.login.vo;

import java.util.ArrayList;


/**
 * Android 3.0 个人中心接口对应数据对象
 * 
 * @author 张宇
 * @create-time Oct 23, 2012 5:22:19 PM
 * @version $Id
 * 
 */
public class UserProfile {
	/**
	 * 收藏 1 
     * 上传 2 
     * 追剧 3 
     * 播放历史 4 
     * 播放列表 5 
     * 粉丝 6 
     * 关注 7 
     * 付费 8 
     * 某URL url地址 
	 */
	public static final int LINK_TYPE_FAVORITE  =1;
	public static final int LINK_TYPE_UPLOAD  =2;
	public static final int LINK_TYPE_AFTERSHOW  =3;
	public static final int LINK_TYPE_HISTORY  =4;
	public static final int LINK_TYPE_PLAYLIST  =5;
	public static final int LINK_TYPE_FANS  =6;
	public static final int LINK_TYPE_FOLLOW  =7;
	public static final int LINK_TYPE_PAY  =8;
	

	public String status;
	public UserProfileResult results = new UserProfileResult();

	public class UserProfileResult {
		public String userid;
		public String username;
		public String userdesc;
		public String avatar;
		public ArrayList<UserProfileItems> items = new ArrayList<UserProfile.UserProfileItems>();

	}

	public class UserProfileItems {

		/**
		 * 按钮id
		 */
		public int id;

		/**
		 * 颜色
		 */
		public int color;

		/**
		 * 标题
		 */
		public String title;

		/**
		 * 图标URL
		 */
		public String icon;

		/**
		 * 下标数
		 */
		public String bottom_count;

		/**
		 * 上标数
		 */
		public int top_count;

		/**
		 * 跳转去的页面
		 */
		public String link_url;
		
		/**
		 * 跳转去的页面
		 */
		public int link_type;
	}

}