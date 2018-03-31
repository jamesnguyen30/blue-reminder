package com.example.jamesnguyen.taskcycle.modules;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.jamesnguyen.taskcycle.room.ItemDao;
import com.example.jamesnguyen.taskcycle.room.ItemDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    private ItemDatabase itemDatabase;

    public RoomModule(Application mApp) {
        this.itemDatabase = Room.databaseBuilder(mApp, ItemDatabase.class, "item-database")
                .build();
    }

    @Singleton
    @Provides
    ItemDatabase provideItemDatabase(){
        return itemDatabase;
    }

    @Singleton
    @Provides
    ItemDao provideItemDao(ItemDatabase itemDatabase){
        return itemDatabase.getItemDao();
    }

}
