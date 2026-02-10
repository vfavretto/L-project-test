package com.itinerary.feature.schedule.di

import com.itinerary.feature.schedule.ScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val scheduleModule = module {
    viewModel {
        ScheduleViewModel(
            getScheduledInterestMarksUseCase = get()
        )
    }
}
