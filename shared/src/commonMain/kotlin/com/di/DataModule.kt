package com.di

import com.data.remote.AuthRemoteDataSource
import com.data.remote.NoteRemoteDataSource
import com.data.remote.SupabaseClientProvider
import com.data.repository.AuthRepositoryImpl
import com.data.repository.NoteRepositoryImpl
import com.domain.repository.AuthRepository
import com.domain.repository.NoteRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import org.koin.dsl.module
import kotlin.coroutines.EmptyCoroutineContext.get

val dataModule = module {
    single { SupabaseClientProvider.client }
    single { get<SupabaseClient>().auth }
    single { get<SupabaseClient>().postgrest }
    single { AuthRemoteDataSource(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { NoteRemoteDataSource(get()) }
    single<NoteRepository> { NoteRepositoryImpl(get()) }
}