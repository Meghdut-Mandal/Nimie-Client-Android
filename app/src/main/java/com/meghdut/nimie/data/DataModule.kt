package com.meghdut.nimie.data

import android.content.Context
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.image.ImageCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    fun provideNimieDb(@ApplicationContext context: Context): NimieDb {
        return NimieDb.create(context)
    }

    @Provides
    fun provideImageCache(@ApplicationContext context: Context) : ImageCache{
        return ImageCache(context)
    }
}