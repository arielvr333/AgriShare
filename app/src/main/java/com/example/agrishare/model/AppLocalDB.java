package com.example.agrishare.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.agrishare.MYApplication;
import com.example.agrishare.feed.MainActivity;

@Database(entities = {Post.class}, version = 19)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao PostDao();
}

public class AppLocalDB{
    static public AppLocalDbRepository db = Room.databaseBuilder(MYApplication.getAppContext(), AppLocalDbRepository.class, "dbFileName.db").fallbackToDestructiveMigration().build();
}