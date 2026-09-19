package com.di

import com.presentation.auth.LoginViewModel
import com.presentation.auth.RegisterViewModel
import com.presentation.home.HomeViewModel
import com.presentation.notes.AddNoteViewModel
import com.presentation.notes.NotesViewModel
import com.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { NotesViewModel(get(), get(), get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }


}