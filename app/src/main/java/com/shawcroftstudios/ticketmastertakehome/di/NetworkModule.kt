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
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @InternalApi
    fun provideApiKeyInterceptor(): ApiKeyInterceptor =
        ApiKeyInterceptor(BuildConfig.API_KEY)

    @Provides
    @InternalApi
    fun provideOkHttpClient(
        @InternalApi interceptor: Lazy<ApiKeyInterceptor>
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor.value)
            .build()

    @Provides
    fun provideRetrofit(
        @InternalApi client: Lazy<OkHttpClient>
    ): Retrofit {
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client.value)

        return builder.build()
    }

    @Provides
    fun provideApiClient(retrofit: Retrofit): ApiClient = ApiClient(retrofit)
}

/**
 * Nifty trick I once found from: https://www.zacsweers.dev/dagger-party-tricks-private-dependencies/
 * TL;DR it's useful for making Dagger dependencies for all intents and purposes 'private'
 */
@Retention(AnnotationRetention.BINARY)
@Qualifier
private annotation class InternalApi