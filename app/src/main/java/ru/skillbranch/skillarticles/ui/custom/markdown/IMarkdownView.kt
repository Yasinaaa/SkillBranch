package ru.skillbranch.skillarticles.ui.custom.markdown

import android.text.Spannable

/**
 * Created by yasina on 10.03.2020.
 */
interface IMarkdownView {
    var fontSize: Float
    val spannableContent: Spannable

    fun renderSearchResult(
        results: List<Pair<Int, Int>>,
        offset: Int
    ) {
        //TODO implement me
    }

    fun renderSearchPosition(
        searchPosition: Pair<Int, Int>,
        offset: Int
    ){
        //TODO implement me
    }

    fun clearSearchResult(){
        //TODO implement me
    }
}