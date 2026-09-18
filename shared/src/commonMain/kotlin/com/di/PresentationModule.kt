package com.di

import org.koin.dsl.module

val presentationModule = module {
    // acá vas a ir agregando, por ejemplo:
    // viewModel { LoginViewModel(get()) }
    // viewModel { HomeViewModel(get(), get()) }
    // viewModel { AddNoteViewModel(get()) }
}