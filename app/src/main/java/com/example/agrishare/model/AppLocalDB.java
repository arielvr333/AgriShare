package com.example.agrishare.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.agrishare.MYApplication;

@Database(entities = {Post.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract PostDao PostDao();
}

public class AppLocalDB{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MYApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}