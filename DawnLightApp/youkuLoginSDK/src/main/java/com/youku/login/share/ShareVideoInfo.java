package com.youku.login.share;

/**
 * 分享到第三方的数据模型
 * @author afei
 *
 */
public class ShareVideoInfo {
	/** 剧集id */
	public String showId;
	/** 剧集标题 */
	public String showName;
	/** 剧集集数号 */
	public int showVideoseq;
	/** 剧集总集数 */
	public int showepisodeTotal;
	/** 视频类型 */
	public String cats;
	/** 缩略图地址 */
	public String imgDrawable;
	public String getShowId() {
		return showId;
	}
	public void setShowId(String showId) {
		this.showId = showId;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	public int getShowVideoseq() {
		return showVideoseq;
	}
	public void setShowVideoseq(int showVideoseq) {
		this.showVideoseq = showVideoseq;
	}
	public int getShowepisodeTotal() {
		return showepisodeTotal;
	}
	public void setShowepisodeTotal(int showepisodeTotal) {
		this.showepisodeTotal = showepisodeTotal;
	}
	public String getCats() {
		return cats;
	}
	public void setCats(String cats) {
		this.cats = cats;
	}
	public String getImgDrawable() {
		return imgDrawable;
	}
	public void setImgDrawable(String imgDrawable) {
		this.imgDrawable = imgDrawable;
	}
}
