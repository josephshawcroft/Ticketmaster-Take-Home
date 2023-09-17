package com.shawcroftstudios.ticketmastertakehome.di

import com.shawcroftstudios.ticketmastertakehome.BuildConfig
import com.shawcroftstudios.ticketmastertakehome.network.ApiClient
import com.shawcroftstudios.ticketmastertakehome.network.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideApiKeyInterceptor(): ApiKeyInterceptor =
        ApiKeyInterceptor(BuildConfig.API_KEY)

    @Provides
    fun provideOkHttpClient(
        interceptor: ApiKeyInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)

        return builder.build()
    }

    @Provides
    fun provideApiClient(retrofit: Retrofit): ApiClient = ApiClient(retrofit)
}