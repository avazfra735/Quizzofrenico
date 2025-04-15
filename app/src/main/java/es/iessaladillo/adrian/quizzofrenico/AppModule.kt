package es.iessaladillo.adrian.quizzofrenico

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.iessaladillo.adrian.quizzofrenico.data.DefaultRepository
import es.iessaladillo.adrian.quizzofrenico.data.Repository
import es.iessaladillo.adrian.quizzofrenico.data.SessionManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(): Repository {
        return DefaultRepository()
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }
}