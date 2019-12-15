package ru.skillbranch.gameofthrones.data.remote.res

import com.squareup.moshi.Json

data class HouseRes(
    @Json(name = "url")
    val url: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "region")
    val region: String,
    @Json(name = "coatOfArms")
    val coatOfArms: String,
    @Json(name = "words")
    val words: String,
    @Json(name = "titles")
    val titles: List<String> = listOf(),
    @Json(name = "seats")
    val seats: List<String> = listOf(),
    @Json(name = "currentLord")
    val currentLord: String,
    @Json(name = "heir")
    val heir: String,
    @Json(name = "overlord")
    val overlord: String,
    @Json(name = "founded")
    val founded: String,
    @Json(name = "founder")
    val founder: String,
    @Json(name = "diedOut")
    val diedOut: String,
    @Json(name = "ancestralWeapons")
    val ancestralWeapons: List<String> = listOf(),
    @Json(name = "cadetBranches")
    val cadetBranches: List<Any> = listOf(),
    @Json(name = "swornMembers")
    val swornMembers: List<String> = listOf()
) : IRes{
    override val id: String
        get() = url.lastSegment()

    val shortName: String
        get() = name.split(" ")
            .dropLastUntil{it == "of"}
            .last()

    val members: List<String>
        get() = swornMembers.map { it.lastSegment() }
}


