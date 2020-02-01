package ru.skillbranch.skillarticles.ui.custom.behavior

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.marginEnd
import ru.skillbranch.skillarticles.ui.custom.Bottombar

/*
 * Created by yasina on 2019-12-29
*/
class ArticleBehavior<V : View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attrs) {

    private var dependedViewTranslationY: Float = 0f
    private var dependedViewHeight: Float = 0f

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

        if (dependedViewTranslationY != 0f) {
            child.translationX =
                dependedViewTranslationY * (child.width + child.marginEnd)/ dependedViewHeight
            Log.d("testest", "onNestedPreScroll " +
                    "dependedViewTranslationY=" + dependedViewTranslationY +
                    " child.translationX=" + child.translationX);
        }else {
            child.translationX = 0f
            Log.d("testest", "onNestedPreScroll " + "child.translationX = 0f");
        }
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        return dependency is Bottombar
    }
    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: V,
        dependency: View
    ): Boolean {
        super.onDependentViewChanged(parent, child, dependency)
        if (dependency.translationY != dependedViewTranslationY) {
            dependedViewTranslationY = dependency.translationY
            dependedViewHeight = dependency.height.toFloat()
            Log.d("testest", "onDependentViewChanged " +
                    "dependency.translationY=" + dependency.translationY +
                    " dependedViewTranslationY=" + dependedViewTranslationY +
                    " dependedViewHeight=" + dependedViewHeight);
        }
        return false
    }
}