package com.di

import com.domain.usecase.GetNotesUseCase
import com.domain.usecase.LoginUseCase
import com.domain.usecase.RegisterUseCase
import com.domain.usecase.SaveNoteUseCase
import com.domain.usecase.UpdateNoteUseCase
import com.presentation.notes.DeleteNoteUseCase
import org.koin.dsl.module

val domainModule = module {
        factory { RegisterUseCase(get()) }
        factory { LoginUseCase(get()) }
        factory { SaveNoteUseCase(repository = get()) }
        factory { GetNotesUseCase(repository = get()) }
        factory { DeleteNoteUseCase(get()) }
        factory { UpdateNoteUseCase(get()) }
}
