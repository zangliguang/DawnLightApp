package com.liguang.dawnlightapp.db;


import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by zangliguang on 15/12/21.
 */
@Database(name = DawnLightDatabase.NAME, version = DawnLightDatabase.VERSION, foreignKeysSupported = true)
public class DawnLightDatabase {
    public static final String NAME = "dawnlight";

    public static final int VERSION = 1;

}
