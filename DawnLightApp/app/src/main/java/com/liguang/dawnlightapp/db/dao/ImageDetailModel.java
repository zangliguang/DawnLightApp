package com.liguang.dawnlightapp.db.dao;

import com.liguang.dawnlightapp.db.DawnLightDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

@Table(database = DawnLightDatabase.class)
@ModelContainer
public class ImageDetailModel extends BaseModel {
    @Column
    @PrimaryKey
    String image_id;
    @Column
    String image_title;
    @Column
    String image_type;
    @Column
    String image_link;
    @Column
    int image_order;
    @Column
    Date create_time;

    public ImageDetailModel(String image_id, String image_title, String image_type, String image_link, int image_order, Date create_time) {
        this.image_id = image_id;
        this.image_title = image_title;
        this.image_type = image_type;
        this.image_link = image_link;
        this.image_order = image_order;
        this.create_time = create_time;
    }

    public ImageDetailModel() {
    }

    public void resetData(String image_id, String image_title, String image_type, String image_link, int image_order, Date create_time) {
        this.image_id = image_id;
        this.image_title = image_title;
        this.image_type = image_type;
        this.image_link = image_link;
        this.image_order = image_order;
        this.create_time = create_time;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_title() {
        return image_title;
    }

    public void setImage_title(String image_title) {
        this.image_title = image_title;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public int getImage_order() {
        return image_order;
    }

    public void setImage_order(int image_order) {
        this.image_order = image_order;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}