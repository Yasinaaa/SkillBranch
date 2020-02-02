package ru.skillbranch.skillarticles.viewmodels.base

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/*
 * Created by yasina on 2020-02-01
*/
class ViewModelDelegate<T : ViewModel>(private val clazz: Class<T>, private val arg: Any?)
: ReadOnlyProperty<FragmentActivity, T> {

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        return ViewModelProvider(thisRef, ViewModelFactory(arg?:"")).get(clazz)
    }

}