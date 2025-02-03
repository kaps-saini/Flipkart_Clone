package com.example.flipkartclone.di

import android.content.Context
import androidx.room.Room
import com.example.flipkartclone.data.api.FakeStoreApi
import com.example.flipkartclone.data.api.FlipkartCloneApi
import com.example.flipkartclone.data.cache.AppDataBase
import com.example.flipkartclone.data.cache.BrandsForYouDao
import com.example.flipkartclone.data.cache.DroppedItemDao
import com.example.flipkartclone.data.cache.ItemDao
import com.example.flipkartclone.data.cache.SponsorsDao
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.domain.models.FakeStore
import com.example.flipkartclone.utils.CheckNetwork
import com.example.flipkartclone.utils.Util.BASE_URL
import com.example.flipkartclone.utils.Util.BASE_URL_2
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance():FlipkartCloneApi{
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FlipkartCloneApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFakeStoreRetrofitInstance():FakeStoreApi{
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_2)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(FakeStoreApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkInstance():CheckNetwork{
        return CheckNetwork()
    }

    @Provides
    @Singleton
    fun provideUserDetailsPrefInstance( @ApplicationContext context: Context):UserDetailsPref{
        return UserDetailsPref(context)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideItemDao(appDatabase: AppDataBase): ItemDao {
        return appDatabase.itemDao()
    }

    @Provides
    fun provideDroppedItemDao(appDatabase: AppDataBase):DroppedItemDao{
        return appDatabase.droppedItemDao()
    }

    @Provides
    fun provideBrandsForYouDao(appDatabase: AppDataBase):BrandsForYouDao{
        return appDatabase.brandsForYouDao()
    }

    @Provides
    fun provideSponsorsDao(appDatabase: AppDataBase):SponsorsDao{
        return appDatabase.sponsorsDao()
    }

}