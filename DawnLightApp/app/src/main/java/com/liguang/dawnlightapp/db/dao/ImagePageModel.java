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
public class ImagePageModel extends BaseModel {
    @Column
    @PrimaryKey
    String id;
    @Column
    String link;
    @Column
    String type;
    @Column
    String title;
    @Column
    Date date;
    @Column
    Date create_time;




}