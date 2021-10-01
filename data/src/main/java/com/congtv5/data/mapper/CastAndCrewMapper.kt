package com.congtv5.data.mapper

import com.congtv5.data.response.castandcrewlist.CastAndCrewResponse
import com.congtv5.data.response.castandcrewlist.CastResponse
import com.congtv5.data.response.castandcrewlist.CrewResponse
import com.congtv5.data.utils.Constants.EMPTY_TEXT
import com.congtv5.domain.model.Cast
import com.congtv5.domain.model.CastAndCrew
import com.congtv5.domain.model.Crew
import javax.inject.Inject

class CastAndCrewMapper @Inject constructor(
    private val castMapper: CastMapper,
    private val crewMapper: CrewMapper
) : Mapper<CastAndCrewResponse, CastAndCrew>() {

    override fun map(response: CastAndCrewResponse): CastAndCrew {
        return CastAndCrew(
            id = response.id ?: 0,
            casts = castMapper.map(response.cast ?: listOf()),
            crews = crewMapper.map(response.crew ?: listOf())
        )
    }

    override fun map(response: List<CastAndCrewResponse>): List<CastAndCrew> {
        return response.map { item -> map(item) }
    }
}

class CastMapper @Inject constructor() : Mapper<CastResponse, Cast>() {

    override fun map(response: CastResponse): Cast {
        return Cast(
            castId = response.castId ?: 0,
            id = response.id ?: 0,
            name = response.name ?: EMPTY_TEXT,
            profilePath = response.profilePath ?: EMPTY_TEXT
        )
    }

    override fun map(response: List<CastResponse>): List<Cast> {
        return response.map { item -> map(item) }
    }

}

class CrewMapper @Inject constructor() : Mapper<CrewResponse, Crew>() {

    override fun map(response: CrewResponse): Crew {
        return Crew(
            id = response.id ?: 0,
            name = response.name ?: EMPTY_TEXT,
            profilePath = response.profilePath ?: EMPTY_TEXT
        )
    }

    override fun map(response: List<CrewResponse>): List<Crew> {
        return response.map { item -> map(item) }
    }

}