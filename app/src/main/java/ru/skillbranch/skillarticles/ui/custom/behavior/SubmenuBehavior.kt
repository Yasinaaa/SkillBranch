package ru.skillbranch.skillarticles.ui.custom.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.marginRight
import ru.skillbranch.skillarticles.ui.custom.ArticleSubmenu
import ru.skillbranch.skillarticles.ui.custom.Bottombar

/*
 * Created by yasina on 2020-01-27
*/
class SubmenuBehavior(): CoordinatorLayout.Behavior<ArticleSubmenu>(){
    constructor(context: Context, attrs: AttributeSet): this()

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ArticleSubmenu,
        dependency: View
    ): Boolean {
        return dependency is Bottombar
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: ArticleSubmenu,
        dependency: View
    ): Boolean {
        return if (child.isOpen && dependency.translationY >= 0f){
            animate(child, dependency)
            true
        }else false
    }

    private fun animate(child: View, dependency: View){
        val fraction = dependency.translationY / dependency.height
        child.translationY = (child.width + child.marginRight) * fraction
    }
}