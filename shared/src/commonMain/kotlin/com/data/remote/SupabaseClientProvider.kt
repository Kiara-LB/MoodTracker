package com.data.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import com.example.moodtracker.BuildKonfig

object SupabaseClientProvider {
    val client = createSupabaseClient(
        supabaseUrl = BuildKonfig.SUPABASE_URL,
        supabaseKey = BuildKonfig.SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }
}