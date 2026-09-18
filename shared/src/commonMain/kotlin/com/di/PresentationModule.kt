package com.di

import com.presentation.auth.LoginViewModel
import com.presentation.auth.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }

}