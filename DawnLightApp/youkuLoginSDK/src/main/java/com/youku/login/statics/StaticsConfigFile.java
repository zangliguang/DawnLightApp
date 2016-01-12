package com.youku.login.statics;

/**
 * Android SDK统计埋点配置文件
 * @author afei
 *
 */
public class StaticsConfigFile {
	
	
	/********************导航页***********************************/
	//导航页
	public static final String NAVIGATION_PAGE = "导航页";
	/***
	 * 导航页面下的模块点击事件
	 */
	public static final String NAVIGATION_SEARCH_ICON_CLICK = "搜索按钮点击";
	public static final String NAVIGATION_SEARCH_ENCODE_VALUE = "navigate|search";//加码示例
	public static final String NAVIGATION_GAME_CENTER_ICON_CLICK = "游戏按钮点击";
	public static final String NAVIGATION_GAME_CENTER_ENCODE_VALUE = "navigate|game";
	public static final String NAVIGATION_APP_MARKET_ICON_CLICK = "导航换量点击";
	public static final String NAVIGATION_APP_MARKET_ENCODE_VALUE = "navigate|shop";
	public static final String NAVIGATION_CACHE_ICON_CLICK = "导航缓存点击";
	public static final String NAVIGATION_CACHE_ENCODE_VALUE = "navigate|cache";
	public static final String NAVIGATION_PLAYED_HISTORY_ICON_CLICK = "导航播放历史点击";
	public static final String NAVIGATION_PLAYED_HISTORY_ENCODE_VALUE = "navigate|history";
	public static final String NAVIGATION_SCAN_AND_SCAN_ICON_CLICK = "导航扫一扫点击";
	public static final String NAVIGATION_SCAN_AND_SCAN_ENCODE_VALUE = "navigate|scan";
	public static final String NAVIGATION_EDIT_ICON_CLICK = "删除按钮点击";
	public static final String NAVIGATION_EDIT_ENCODE_VALUE = "editBar|deleteClick";
	public static final String NAVIGATION_COMPLETE_ICON_CLICK = "完成按钮点击";
	public static final String NAVIGATION_COMPLETE_ENCODE_VALUE = "editBar|doneClick";
	
	
	
	/********************扫一扫页面**********************************/
	//扫一扫页面
	public static final String SCAN_AND_SCAN_PAGE = "扫一扫页";
	/**
	 * 扫一扫下面的模块点击事件
	 */
	public static final String SCAN_AND_SCAN_VIDEO_PLAY_CLICK = "扫一扫视频播放";
	public static final String SCAN_AND_SCAN_VIDEO_PLAY_ENCODE_VALUE = "scan|videoClick";//加码示例
	
	
	
	
	/********************登录页面**********************************/
	//登录页面
	public static final String LOGIN_PAGE = "登录页";
	/**
	 * 登录界面的模块点击事件
	 */
	public static final String LOGIN_PAGE_REGIST_BUTTON_CLICK = "注册";
	public static final String LOGIN_PAGE_REGIST_BUTTON_ENCODE_VALUE = "login|register";//加码示例
	public static final String LOGIN_PAGE_SCAN_AND_SCAN_CLICK = "扫一扫";
	public static final String LOGIN_PAGE_SCAN_AND_SCAN_ENCODE_VALUE = "login|scan";//加码示例
	public static final String LOGIN_PAGE_FORGET_PASSWORD_CLICK = "忘记密码";
	public static final String LOGIN_PAGE_FORGET_PASSWORD_ENCODE_VALUE = "login|findPassword";//加码示例
	public static final String LOGIN_PAGE_LOGIN_BUTTON_CLICK = "成功登录";
	public static final String LOGIN_PAGE_LOGIN_BUTTON_ENCODE_VALUE = "login|loginClick";//加码示例
	
	/**
	 * 统计中登录中要保存的字段
	 * 
	 * loginType（登录类型=1表示主动登录，2表示被动登录、
	 * loginPath登录方式，1表示优酷或土豆账号，2表示新浪微博，3表示QQ，4表示支付宝，5表示微信）、
	 * loginSource（登录来源=1个人中心用户头像、2更多用户头像、3个人中心登录按钮、4更多登录按钮、5个人中心收藏、6个人中心上传、7个人中心订阅、8个人中心付费、9播放收藏、10播放订阅、11付费登录、12发现、13互动娱乐）、
	 * isMember（1表示会员，2表示非会员）
	 */
	
	
	public static String loginType = "2";// 登录类型
	public static String loginPath = "1";// 登录方式
	public static String loginSource = "0";// 登录来源,默认为0
	public static String isMember = "2";// 是否是会员
	
	public static final String LOGIN_TYPE_DEFAULT_LOGIN = "0";//默认动作为0
	public static final String LOGIN_TYPE_ACTIVE_LOGIN = "1";//主动登录
	public static final String LOGIN_TYPE_AUTO_LOGIN = "2";//自动登录
	
	public static final String LOGIN_PATH_DEFAULT_LOGIN = "0";//默认情况下为0
	public static final String LOGIN_PATH_YOUKU_OR_TUDOU_LOGIN = "1";//表示优酷或土豆账号
	public static final String LOGIN_PATH_SINA_WEIBO_LOGIN = "2";//表示新浪微博
	public static final String LOGIN_PATH_QQ_ACCOUNT_LOGIN = "3";//表示QQ
	public static final String LOGIN_PATH_ALI_PAY_LOGIN = "4";//表示支付宝
	public static final String LOGIN_PATH_WEIXIN_LOGIN = "5";//表示微信
	
	
	public static final String LOGIN_SOURCE_DEFAULT_LOGIN = "0";//默认情况下为0
	public static final String LOGIN_SOURCE_PERSON_CENTER_ICON_LOGIN = "1";//个人中心用户头像
	public static final String LOGIN_SOURCE_MORE_USER_ICON_LOGIN = "2";//更多用户头像
	public static final String LOGIN_SOURCE_PERSON_CENTER_LOGIN_BUTTON_LOGIN = "3";//个人中心登录按钮
	public static final String LOGIN_SOURCE_MORE_LOGIN_BUTTON_LOGIN = "4";//更多登录按钮
	public static final String LOGIN_SOURCE_PERSON_CENTER_FAVERATE_LOGIN = "5";//个人中心收藏
	public static final String LOGIN_SOURCE_PERSON_CENTER_UPLOAD_LOGIN = "6";//个人中心上传
	public static final String LOGIN_SOURCE_PERSON_CENTER_SUBSCRIBE_LOGIN = "7";//个人中心订阅
	public static final String LOGIN_SOURCE_PERSON_CENTER_PAY_LOGIN = "8";//个人中心付费
	public static final String LOGIN_SOURCE_PLAY_FAVORATE_LOGIN = "9";//播放收藏
	public static final String LOGIN_SOURCE_PLAY_SUBSCRIBE_LOGIN = "10";//播放订阅
	public static final String LOGIN_SOURCE_OPEN_VIP_LOGIN = "11";//开通会员付费登录
	public static final String LOGIN_SOURCE_LOOK_FOR_LOGIN = "12";//发现
	public static final String LOGIN_SOURCE_EXCHANGE_FUN_LOGIN = "13";//互动娱乐
	public static final String LOGIN_SOURCE_PRE_ADVER_LOGIN = "14";//前贴广告
	public static final String LOGIN_SOURCE_PLAY_N_LOGIN = "15";//播放N次登录提示
	public static final String LOGIN_SOURCE_GO_PAY_LOGIN = "16";//会员片购买或开通
	public static final String LOGIN_SOURCE_PERSON_LEVEL_LOGIN = "17";//个人中心等级icon
	
	
	//14前贴广告、15播放N次登录提示、16会员片购买或开通、17个人中心等级icon
	public static final String LOGIN_IS_MEMBER = "1";//表示会员
	public static final String LOGIN_NOT_MEMBER = "2";//表示非会员
	

	
	
	
	/************************优酷首页******************/
	//优酷首页
	public static final String YOUKU_HOME_PAGE = "优酷首页";
	/***
	 *优酷首页模块点击事件
	 */
	public static final String YOUKU_HOME_POSTER_VIDEO_CLICK = "轮播图";
	public static final String YOUKU_HOME_POSTER_VIDEO_ENCODE_VALUE = "home|posterVideo";
	public static final String YOUKU_HOME_FOUR_AND_ONE_VIDEO_CLICK = "热点视频";
	public static final String YOUKU_HOME_FOUR_AND_ONE_VIDEO_ENCOD_VALUE = "home|hotVideo|";
	public static final String YOUKU_HOME_CHANNEL_VIDEO_CLICK = "频道";
	public static final String YOUKU_HOME_CHANNEL_VIDEO_ENCOD_VALUE = "home|channelVideoClick|";
	public static final String YOUKU_HOME_CHANNLE_NAME_CLICK = "频道";
	public static final String YOUKU_HOME_CHANNLE_NAME_ENCOD_VALUE = "home|channelVideoClick";//加码示例
	public static final String YOUKU_HOME_RECOMMEND_FOR_ME_ICON_CLICK = "为我推荐点击";
	public static final String YOUKU_HOME_RECOMMEND_FOR_ME_ENCOD_VALUE= "home|recommendForMe";//加码示例
	public static final String YOUKU_HOME_CHANNEL_SORT_TAB_CLICK = "频道分类点击或滑动";
	public static final String YOUKU_HOME_CHANNEL_SORT_TAB_ENCOD_VALUE = "home|channelSelect";//加码示例
	public static final String YOUKU_HOME_PERSONAL_CENTER_TAB_CLICK = "个人中心点击或滑动";
	public static final String YOUKU_HOME_PERSONAL_CENTER_TAB_ENCOD_VALUE = "home|space";//加码示例
	public static final String YOUKU_HOME_PERSONAL_PLAY_HISTORY_CLICK = "历史";
	public static final String YOUKU_HOME_PERSONAL_PLAY_HISTORY_ENCOD_VALUE = "home|historyTipClick|";//加码示例
	
	//为我推荐页
	public static final String RECOMMEND_FOR_ME_PAGE = "为我推荐页";
	/**
	 * 为我推荐模块点击事件
	 */
	public static final String RECOMMEND_FOR_ME_VIDEO_CLICK = "为我推荐视频";
	public static final String RECOMMEND_FOR_ME_VIDEO_ENCOD_VALUE = "recommendForMe|videoClick|";//加码示例
	public static final String RECOMMEND_FOR_ME_CHANGE_CLICK = "换一换";
	public static final String RECOMMEND_FOR_ME_CHANGE_ENCOD_VALUE = "recommendForMe|change";
	
	
	//上传视频列表页
	public static final String UPLOAD_VIDEO_LIST_PAGE = "上传列表页";
	/**
	 * 上传视频列表点击事件
	 */
	public static final String UPLOAD_VIDEO_LIST_VIDEO_CLICK = "上传视频";
	public static final String UPLOAD_VIDEO_LIST_VIDEO_ENCODE_VALUE = "uploadList|uploadVideoClick|";
	
	//收藏列表页
	public static final String FAVORITE_LIST_PAGE = "收藏列表页";
	/**
	 * 上传视频列表点击事件
	 */
	public static final String FAVORITE_LIST_SHOW_CLICK = "收藏节目";
	public static final String FAVORITE_LIST_SHOW_CLICK_ENCODE_VALUE = "favList|favShowClick|";
	public static final String FAVORITE_LIST_VIDEOS_CLICK = "收藏视频";
	public static final String FAVORITE_LIST_VIDEOS_CLICK_ENCODE_VALUE = "favList|favVideoClick|";
	public static final String FAVORITE_LIST_ABLUMLIST_CLICK = "收藏专辑";
	public static final String FAVORITE_LIST_ABLUMLIST_CLICK_ENCODE_VALUE = "favList|favPlaylistClick|";
	
	
	//他人个人中心页
	public static final String OTHER_PERSON_CENTER_PAGE = "他人个人中心页";
	/**
	 * 他人个人中心点击事件
	 */
	public static final String OTHER_PERSON_CENTER_SUSCRIBE_CLICK = "订阅点击";
	public static final String OTHER_PERSON_CENTER_SUSCRIBE_ENCODE_VALUE = "person|rssButtonClick";
	public static final String OTHER_PERSON_CENTER_PLAYLIST_CARD_CLICK = "专辑卡片";
	public static final String OTHER_PERSON_CENTER_PLAYLIST_CARD_ENCODE_VALUE = "person|playlistCardClick|";
	public static final String OTHER_PERSON_CENTER_VIDEO_CARD_CLICK = "视频卡片";
	public static final String OTHER_PERSON_CENTER_VIDEO_CARD_ENCODE_VALUE = "person|videoCardClick|";
	public static final String OTHER_PERSON_CENTER_PLAYLIST_CLICK = "专辑列表";
	public static final String OTHER_PERSON_CENTER_PLAYLIST_ENCODE_VALUE = "person|playlistClick|";
	public static final String OTHER_PERSON_CENTER_VIDEOS_LIST_CLICK = "视频列表";
	public static final String OTHER_PERSON_CENTER_VIDEOS_LIST_ENCODE_VALUE = "person|videolistClick|";
	
	
	/***********************************频道分类页*********************************/
	//频道分类页
	public static final String CHANNEL_CATEGORY_PAGE= "频道分类页";
	/**
	 * 频道分类模块点击事件
	 */
	public static final String CHANNEL_CATEGORY_YOUKU_HOME_TAB_CLICK= "优酷首页滑动或点击";
	public static final String CHANNEL_CATEGORY_YOUKU_HOME_TAB_ENCODE_VALUE= "channelSelect|home";
	public static final String CHANNEL_CATEGORY_PERSONAL_CENTER_TAB_CLICK= "个人中心滑动或点击";
	public static final String CHANNEL_CATEGORY_PERSONAL_CENTER_TAB_ENCODE_VALUE= "channelSelect|space";
	public static final String CHANNEL_CATEGORY_YOUKU_CHANNEL_CLICK= "频道";
	public static final String CHANNEL_CATEGORY_YOUKU_CHANNEL_ENCOD_VALUE= "channelSelect|channelClick|";
	public static final String CHANNEL_CATEGORY_SPECIAL_CHOREOGRAPHY_CLICK= "专题";
	public static final String CHANNEL_CATEGORY_SPECIAL_CHOREOGRAPHY_ENCOD_VALUE= "channelSelect|specialClick|";
	
	//排行榜页
	public static final String LEADER_BORD_PAGE = "排行榜页";
	/**
	 * 排行榜模块点击事件
	 */
	public static final String LEADER_BORD_VIDEO_CLICK = "排行榜视频";
	public static final String LEADER_BORD_VIDEO_ENCOD_VALUE = "top|videoClick|";//加码示例
	
	//已购页
	public static final String HAVE_BUY_PAGE = "付费视频列表页";
	public static final String HAVE_BUY_VIDEO_CLICK = "付费视频";
	public static final String HAVE_BUY_VIDEO_ENCODE_VALUE = "payList|payVideoClick|";
	
	//频道页
	public static final String CHANNEL_PAGE = "频道页";
	/**
	 * 频道页模块点击事件
	 */
	public static final String CHANNEL_VIDEO_CLICK = "编辑类子频道视频点击";
	public static final String CHANNEL_BRAND_CLICK = "编辑类子频道品牌点击";
	public static final String CHANNEL_VIDEO_ENCODE_VALUE = "top|videoClick|";
	public static final String CHANNEL_BRAND_ENCODE_VALUE = "channel|brandClick|";
	public static final String CHANNEL_VIDEO_FILTER_CLICK = "筛选项点击";
	public static final String CHANNEL_VIDEO_FILTER_ENCODE_VALUE = "channel|videoSelect";
	public static final String CHANNEL_FILTERED_VIDEO_CLICK = "筛选视频";
	public static final String CHANNEL_FILTERED_VIDEO_ENCODE_VALUE = "channel|selectVideoClick|";
	public static final String CHANNEL_CHANGE_CLICK = "频道切换";
	public static final String CHANNEL_CHANGE_ENCODE_VALUE = "channel|changeChannel";
	
	//全部视频页
	public static final String ALL_VIDEOS_PAGE = "全部视频页";
	/**
	 * 视频点击
	 */
	public static final String ALL_VIDEOS_VIDEO_CLICK = "全部视频点击";
	public static final String ALL_VIDEOS_VIDEO_ENCODE_VALUE = "all|videoClick|";
	
	
	
	
	
	/***************************************搜索页***********************************/
	//搜索标签页
	public static final String SEARCH_TAB_PAGE = "搜索页";
	/**
	 * 搜索标签页点击事件
	 */
	public static final String SEARCH_TAB_VOICE_ICON_CLICK = "语音搜索点击";
	public static final String SEARCH_TAB_VOICE_ICON_ECOND_VALUE = "search|sound";
	public static final String SEARCH_TAB_QR_CODE_ICON_CLICK = "扫一扫点击";
	public static final String SEARCH_TAB_QR_CODE_ICON_ECOND_VALUE = "search|rqcode";
	public static final String SEARCH_TAB_VOICE_RECOGNISE_ICON_CLICK = "语音搜索启用";
	public static final String SEARCH_TAB_VOICE_RECOGNISE_ICON_ECOND_VALUE = "search|soundsearch";
	public static final String SEARCH_TAB_KEY_WORD_SEARCH_CLICK = "关键词搜索";
	public static final String SEARCH_TAB_KEY_WORD_SEARCH_ECOND_VALUE = "search|wordsearch";
	public static final String SEARCH_TAB_GUIDE_WORD_CLICK = "kubox点击搜索";
	public static final String SEARCH_TAB_GUIDE_WORD_ECOND_VALUE = "search|hintsearch|";
	public static final String SEARCH_TAB_HISTORY_WORD_CLICK = "历史词搜索";
	public static final String SEARCH_TAB_HISTORY_WORD_ECOND_VALUE = "search|hissearch|";
	public static final String SEARCH_TAB_HOT_WORD_RECOMMEND_CLICK = "热词搜索";
	public static final String SEARCH_TAB_HOT_WORD_RECOMMEND_ECOND_VALUE = "search|hotsearch|";
	
	//搜索结果页
	public static final String SEARCH_RESULT_PAGE = "搜索结果页";
	/**
	 * 搜索结果页点击事件
	 */
	public static final String SEARCH_RESULT_NOSTOP_AREA_PROGRAM_CLICK = "直达区点击";
	public static final String SEARCH_RESULT_NOSTOP_AREA_PROGRAM_ENCODE_VALUE = "search|directVideoClick|";
	public static final String SEARCH_RESULT_UGC_VIDEO_CLICK = "UGC视频点击";
	public static final String SEARCH_RESULT_UGC_VIDEO_ENCODE_VALUE = "search|ugcVideoClick|";
	public static final String SEARCH_RESULT_RESULT_FILTER_CLICK = "搜索筛选";
	public static final String SEARCH_RESULT_RESULT_FILTER_ENCODE_VALUE = "search|ugcVideoClick";
	public static final String SEARCH_RESULT_FILTERED_NOSTOP_VIDEO_CLICK = "筛选直区节目点击";
	public static final String SEARCH_RESULT_FILTERED_NOSTOP_VIDEO_ENCODE_VALUE = "search|selectShowClick|1";
	public static final String SEARCH_RESULT_FILTERED_UGC_VIDEO_CLICK = "UGC筛选结果视频点击";
	public static final String SEARCH_RESULT_FILTERED_UGC_VIDEO_ENCODE_VALUE = "search|selectVideoClick|";
	
	
	
	
	/***********************************************************详情页***********************/
	//视频详情页
	public static final String VIDEO_DETAIL_PAGE = "视频详情页";
	/**
	 * 视频详情点击事件
	 */
	public static final String VIDEO_DETAIL_VIDEO_DETAIL_TAB_CLICK = "视频详情tab点击";
	public static final String VIDEO_DETAIL_VIDEO_DETAIL_TAB_ENCOD_VALUE = "detail|profile";
	public static final String VIDEO_DETAIL_VIDEO_COMMENT_TAB_CLICK = "评论tab点击";
	public static final String VIDEO_DETAIL_VIDEO_COMMENT_TAB_ENCOD_VALUE = "detail|comment";
	public static final String VIDEO_DETAIL_VIDEO_FOCUS_TAB_CLICK = "看点tab点击";
	public static final String VIDEO_DETAIL_VIDEO_FOCUS_TAB_ENCOD_VALUE = "detail|point";
	public static final String VIDEO_DETAIL_VIDEO_RELATE_TAB_CLICK = "相关视频tab点击";
	public static final String VIDEO_DETAIL_VIDEO_RELATE_TAB_ENCOD_VALUE = "detail|recommend";
	public static final String VIDEO_DETAIL_PROGRAM_SELECTED_TAB_CLICK = "选集tab点击";
	public static final String VIDEO_DETAIL_PROGRAM_SELECTED_TAB_ENCOD_VALUE = "detail|showlist";
	public static final String VIDEO_DETAIL_PROGRAM_CACHE_TAB_CLICK = "缓存tab点击";
	public static final String VIDEO_DETAIL_PROGRAM_CACHE_TAB_ENCOD_VALUE = "detail|showcache";
	public static final String VIDEO_DETAIL_PROGRAM_DETAIL_TAB_CLICK = "节目详情tab点击";
	public static final String VIDEO_DETAIL_PROGRAM_DETAIL_TAB_ENCOD_VALUE = "detail|showprofile";
	public static final String VIDEO_DETAIL_PROGRAM_COMMENT_TAB_CLICK = "节目评论tab点击";
	public static final String VIDEO_DETAIL_PROGRAM_COMMENT_TAB_ENCOD_VALUE = "detail|showcomment";
	public static final String VIDEO_DETAIL_PROGRAM_STORY_TAB_CLICK = "节目剧情tab点击";
	public static final String VIDEO_DETAIL_PROGRAM_STORY_TAB_ENCOD_VALUE = "detail|showplot";
	
	
	public static final String VIDEO_DETAIL_SELECTED_TAB_TYPE = "选集";
	public static final String VIDEO_DETAIL_CACHE_TAB_TYPE = "缓存";
	public static final String VIDEO_DETAIL_DETAIL_TAB_TYPE = "详情";
	public static final String VIDEO_DETAIL_COMMENT_TAB_TYPE = "评论";
	public static final String VIDEO_DETAIL_STORY_TAB_TYPE = "剧情";
	public static final String VIDEO_DETAIL_FOCUS_TAB_TYPE = "看点";
	public static final String VIDEO_DETAIL_RELATE_TAB_TYPE = "相关";
	
	
	//视频相关页
	public static final String VIDEO_RELETE_PAGE = "视频相关页";
	/**
	 * 视频相关页面点击事件
	 */
	public static final String VIDEO_RELETE_VIDEOS_CLICK = "相关视频点击";
	public static final String VIDEO_RELETE_VIDEOS_ENCODE_VALUE = "recommend|videoClick|";
	
	//节目选集页
	public static final String PROGRAM_CHOOSE_PAGE = "节目选集页";
	/**
	 * 节目选集页点击事件
	 */
	public static final String PROGRAM_CHOOSE_VIDEO_CLICK = "选集视频点击";
	public static final String PROGRAM_CHOOSE_VIDEO_ENCODE_VALUE = "showlist|videoClick|";
	
	//节目缓存页
	public static final String PROGRAM_CACHE_PAGE = "节目缓存页";
	/**
	 * 节目缓存页点击事件
	 */
	public static final String PROGRAM_CACHE_VIDEO_CLICK = "缓存点击";
	public static final String PROGRAM_CACHE_VIDEO_ENCODE_VALUE = "showcache|videoClick";
	
	
	
	
	/***************************************播放器页**************************/
	//播放器页
	public static final String PLAYER_PAGE = "大屏播放";
	/**
	 * 播放器页点击事件
	 */
	public static final String PLAYER_AFTER_PLAY_CLICK = "播后推荐点击";
	public static final String PLAYER_AFTER_PLAY_ENCODE_VALUE = "player|next|";
	
	
	
	
	/**********************************************push页***********************/
	//push页
	public static final String PUSH_PAGE = "push页";
	/**
	 * push页点击事件
	 */
	public static final String PUSH_VIDEO_CLICK = "push视频播放点击";
	public static final String PUSH_VIDEO_ENCODE_VALUE = "pushBar|pushVideo|1";
	
	public static final String PUSH_APP_INSTALL = "push唤起安装完成";
	public static final String PUSH_APP_ENCODE_VALUE = "pushBar|pushAPP";
	
	
	
	
	
	
	
	/******************************************页面事件加载定义***************************************/
	public static final String PAGE_LOAD_APP_LOAD_EVENT = "app启动";
	public static final String PAGE_LOAD_APP_LOAD_CODE = "appload";//事件代码
	public static final String PAGE_LOAD_HOME_LOAD_EVENT = "首页加载";
	public static final String PAGE_LOAD_HOME_LOAD_CODE = "homeload";//事件代码
	public static final String PAGE_LOAD_CHANNEL_LIST_LOAD_EVENT = "大词页加载";
	public static final String PAGE_LOAD_CHANNEL_LIST_LOAD_CODE = "channelListLoad";
	public static final String PAGE_LOAD_CHANNEL_LOAD_EVENT = "频道页加载";
	public static final String PAGE_LOAD_CHANNEL_LOAD_CODE = "channelload";
	public static final String PAGE_LOAD_USER_CENTER_EVENT = "用户中心加载";
	public static final String PAGE_LOAD_USER_CENTER_CODE = "usercenter";
	public static final String PAGE_LOAD_SEARCH_LOAD_EVENT = "搜索页加载";
	public static final String PAGE_LOAD_SEARCH_LOAD_CODE = "searchLoad";
	public static final String PAGE_LOAD_SEARCH_RESULT_LOAD_EVENT = "搜索结果页加载";
	public static final String PAGE_LOAD_SEARCH_RESULT_LOAD_CODE = "searchResultLoad";
	public static final String PAGE_LOAD_DETAIL_LOAD_EVENT = "详情页加载";
	public static final String PAGE_LOAD_DETAIL_LOAD_CODE = "detailload";
	/********************************************************************************************/
	
	
	
	
	/****************************搜索页面新增ktype字段的值************************************/
	//app获取（语音识别，关键词，提示词，历史词，热词）
	public static final String SEARCH_KREFVALUE_VOICE = "语音识别";
	public static final String SEARCH_KREFVALUE_KEY_WORD = "关键词";
	public static final String SEARCH_KREFVALUE_KUBOX_WORD = "提示词";
	public static final String SEARCH_KREFVALUE_HISTORY_WORD = "历史词";
	public static final String SEARCH_KREFVALUE_HOT_WORD = "热词";
	/********************************************************************************************/

	
	
	
}
