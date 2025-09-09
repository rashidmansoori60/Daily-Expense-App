package com.example.dailyexpenseapp.Di

import android.content.Context
import androidx.room.Room
import com.example.dailyexpenseapp.data.local.Dao
import com.example.dailyexpenseapp.data.local.Database
import com.example.dailyexpenseapp.data.local.Repostory.RepositoryImp
import com.example.dailyexpenseapp.data.local.Repostory.Repostary
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun getRoomdb(@ApplicationContext context: Context): Database{
        return Room.databaseBuilder(context, Database::class.java,"RoomDb").build()
    }

    @Provides
    @Singleton
    fun getDao(db: Database): Dao=db.getdao()

    @Provides
    @Singleton
    fun getRepo(repositoryImp: RepositoryImp): Repostary{
        return repositoryImp
    }


}