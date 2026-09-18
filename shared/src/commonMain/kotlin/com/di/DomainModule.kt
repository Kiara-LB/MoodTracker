package com.di

import org.koin.dsl.module

val domainModule = module {
    // acá vas a ir agregando, por ejemplo:
    // factory { LoginUseCase(get()) }
    // factory { SaveNoteUseCase(get()) }
    // factory { GetMonthlyMoodsUseCase(get()) }
}