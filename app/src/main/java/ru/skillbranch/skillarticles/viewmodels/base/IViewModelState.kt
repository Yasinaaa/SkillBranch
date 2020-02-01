package ru.skillbranch.skillarticles.viewmodels.base

import android.os.Bundle
/*
 * Created by yasina on 2020-01-31
*/

interface IViewModelState {
    fun save(outState: Bundle)
    fun restore(savedState:Bundle) : IViewModelState
}