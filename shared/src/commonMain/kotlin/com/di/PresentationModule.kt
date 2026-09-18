package com.di

import com.presentation.auth.LoginViewModel
import com.presentation.auth.RegisterViewModel
import com.presentation.notes.AddNoteViewModel
import com.presentation.notes.NotesViewModel
import com.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { AddNoteViewModel(get()) }
    viewModel { NotesViewModel(get()) }
    viewModel { ProfileViewModel(get()) }


}