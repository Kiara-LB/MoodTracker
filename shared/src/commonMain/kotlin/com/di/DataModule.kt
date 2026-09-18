package com.di

import com.data.remote.SupabaseClientProvider
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import org.koin.dsl.module
import kotlin.coroutines.EmptyCoroutineContext.get

val dataModule = module {
    single { SupabaseClientProvider.client }
    single { get<SupabaseClient>().auth }
    single { get<SupabaseClient>().postgrest }
}