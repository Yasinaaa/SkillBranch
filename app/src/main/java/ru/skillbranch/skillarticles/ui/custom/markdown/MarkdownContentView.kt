package ru.skillbranch.skillarticles.ui.custom.markdown

import android.content.Context
import android.util.AttributeSet
import android.view.TextureView
import android.view.View
import android.widget.TextView
import ru.skillbranch.skillarticles.extensions.dpToIntPx
import ru.skillbranch.skillarticles.markdown.MarkdownBuilder
import kotlin.properties.Delegates
import kotlin.time.measureTime

/**
 * Created by yasina on 10.03.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class MarkdownContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) {
    private lateinit var element: List<MarkdownElement>

    //for restore
    private var ids = arrayListOf<Int>()

    var textSize by Delegates.observable(14f){ _, old, value ->
        if (value == old) return@observable
        this.children.forEach{
            it as IMarkdownView
            it.fontSize = value
        }
    }
    var isLoading: Boolean = true
    val padding = context.dpToIntPx(8)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int){
        val usedHeight = paddingTop
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)

        children.forEach{
            measureChild(it, widthMeasureSpec, heightMeasureSpec)
            usedHeight += paddingBottom
            setMeasuredDimension(width, usedHeight)
        }
    }

    public override fun onLayout(changed: Boolean, l:Int, t:Int, r:Int, b:Int){
        val usedHeight = paddingTop
        val bodyWidth = right - left - paddingLeft - paddingRight
        val left = paddingLeft
        val right = paddingLeft + bodyWidth

        children.forEach{
            if(it is MarkdownTextView){
                it.layout(
                    left - paddingLeft / 2,
                    usedHeight,
                    r - paddingRight / 2,
                    usedHeight + it.measuredHeight
                )
            }
        } else {
            it.layout(
                left,
                usedHeight,
                right,
                usedHeight + it.measuredHeight
            )
        }
        usedHeight += it.measuredHeight
    }

    fun setContent(content : List<MarkdownElement>){
        elements = content
        content.forEach {
            when(it){
                is MarkdownElement.Text -> {
                    val tv = MarkdownTextView(content, textSize).apply {
                        setPaddingOptionally(
                            left = padding,
                            right = padding
                        )
                        setLineSpacing(fontSize * 0.5f, 1f)
                    }

                    MarkdownBuilder(context)
                        .markdownToSpan(it)
                        .run {
                            tv.setText(this, TextView.BufferType.SPANNABLE)
                        }
                    addView(tv)
                }

                is MarkdownElement.Image -> {
                    val iv = MarkdownImageView(
                        content,
                        textSize,
                        it.image.url,
                        it.image.text,
                        it.image.alt
                    )
                    addView(iv)
                }

                is MarkdownElement.Scroll -> {
                    val sv = MarkdownCodeView(
                        content,
                        textSize,
                        it.blockCode.text
                    )
                    addView(sv)
                }
            }
        }
    }

    fun renderSearchResult(searchResult: List<Pair<Int, Int>>){
        children.forEach{ view ->
            view as IMarkdownView
            view.clearSearchResult()
        }

        if (searchResult.isEmpty()) return

        val bounds = element.map{it.bounds}
        val result = searchResult.groupByBounds(bounds)

        children.forEachIndexed{ index, view ->
            view as IMarkdownView
            //search for child with markdown element offset
            view.renderSearchResult(result[index], elements[index].offset)
        }
    }

    fun renderSearchPosition(searchPosition: List<Pair<Int, Int>>){
        searchPosition ?: return
        val bounds = elements.map{ it.bounds }

        val index = bounds.indexOfFirst{ (start, end) ->
            val boundRange = start.end
            val (startPos, endPos) = searchPosition
            startPos in boundRange && endPos in boundRange
        }

        if (index == -1) return
        val view = getChildAt(index)
        view as IMarkdownView
        view.renderSearchPosition(searchPosition, elements[index].offset)
    }

    fun clearSearchResult(){
        children.forEach{ view ->
            view as IMarkdownView
            view.clearSearchResult()
        }
    }

    fun setCopyListener(listener: (String) -> Unit){
        children.filterIsInstance<MarkdownCodeView>()
            .forEach{ it.copyListener = listener}
    }
}