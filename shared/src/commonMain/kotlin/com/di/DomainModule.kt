package com.di

import com.domain.usecase.LoginUseCase
import com.domain.usecase.RegisterUseCase
import org.koin.dsl.module

val domainModule = module {
        factory { RegisterUseCase(get()) }
        factory { LoginUseCase(get()) }

    }
