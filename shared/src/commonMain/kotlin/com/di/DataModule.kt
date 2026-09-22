package com.di

import com.data.remote.AuthRemoteDataSource
import com.data.remote.AuthRemoteDataSourceImpl
import com.data.remote.NoteRemoteDataSource
import com.data.remote.NoteRemoteDataSourceImpl
import com.data.remote.ProfileRemoteDataSource
import com.data.remote.ProfileRemoteDataSourceImpl
import com.data.remote.SupabaseClientProvider
import com.data.repository.AuthRepositoryImpl
import com.data.repository.NoteRepositoryImpl
import com.data.repository.ProfileRepositoryImpl
import com.domain.repository.AuthRepository
import com.domain.repository.NoteRepository
import com.domain.repository.ProfileRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import org.koin.dsl.module


val dataModule = module {
    single { SupabaseClientProvider.client }
    single { get<SupabaseClient>().auth }
    single { get<SupabaseClient>().postgrest }

    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    single<NoteRemoteDataSource> { NoteRemoteDataSourceImpl(get()) }
    single<NoteRepository> { NoteRepositoryImpl(get()) }

    single<ProfileRemoteDataSource> { ProfileRemoteDataSourceImpl(get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
}