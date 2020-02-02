package ru.skillbranch.skillarticles.extensions

/*
 * Created by yasina on 2020-02-01
*/
fun String?.indexesOf(query: String, ignoreCase: Boolean = true) : List<Int> {
    if (query.isBlank() || this.isNullOrEmpty()) return listOf()

    var content: String
    var searchQuery: String

    if (ignoreCase){
        content = this.toLowerCase()
        searchQuery = query.toLowerCase()
    }else{
        content = this
        searchQuery = query
    }
    val searchQueryLength = searchQuery.length

    return content.mapIndexed{ index, letter ->
        if (letter == searchQuery.first()){
            if (searchQueryLength + index <= length &&
                content.substring(index, searchQueryLength + index) == searchQuery)
                return@mapIndexed index
        }
        -1
    }.filter{ it > 0 }
}
