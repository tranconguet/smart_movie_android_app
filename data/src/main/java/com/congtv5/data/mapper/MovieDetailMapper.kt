package com.congtv5.data.mapper

import com.congtv5.data.response.movie.MovieDetailResponse
import com.congtv5.data.response.movie.ProductionCountryResponse
import com.congtv5.data.response.movie.SpokenLanguageResponse
import com.congtv5.data.utils.Constants.EMPTY_TEXT
import com.congtv5.domain.model.MovieDetail
import com.congtv5.domain.model.ProductionCountry
import com.congtv5.domain.model.SpokenLanguage
import javax.inject.Inject

class MovieDetailMapper @Inject constructor(
    private val genreMapper: GenreMapper,
    private val productCountryMapper: ProductCountryMapper,
    private val spokenLanguage: SpokenLanguageMapper
) : Mapper<MovieDetailResponse, MovieDetail>() {

    override fun map(response: MovieDetailResponse): MovieDetail {
        return MovieDetail(
            id = response.id ?: 0,
            title = response.title ?: EMPTY_TEXT,
            genres = genreMapper.map(response.genres ?: listOf()),
            overview = response.overview ?: EMPTY_TEXT,
            posterPath = response.posterPath ?: EMPTY_TEXT,
            productionCountries = productCountryMapper.map(response.productionCountries ?: listOf()),
            releaseDate = response.releaseDate ?: EMPTY_TEXT,
            revenue = response.revenue ?: 0,
            runtime = response.runtime ?: 0,
            voteAverage = response.voteAverage ?: 0.0,
            spokenLanguages = response.spokenLanguage?.let { spokenLanguage.map(it) } ?: listOf()
        )
    }

    override fun map(response: List<MovieDetailResponse>): List<MovieDetail> {
        return response.map { item -> map(item) }
    }
}

class ProductCountryMapper @Inject constructor() :
    Mapper<ProductionCountryResponse, ProductionCountry>() {
    override fun map(response: ProductionCountryResponse): ProductionCountry {
        return ProductionCountry(
            name = response.name ?: EMPTY_TEXT,
            iso_3166_1 = response.iso_3166_1 ?: EMPTY_TEXT
        )
    }

    override fun map(response: List<ProductionCountryResponse>): List<ProductionCountry> {
        return response.map { item -> map(item) }
    }
}

class SpokenLanguageMapper @Inject constructor() :
    Mapper<SpokenLanguageResponse, SpokenLanguage>() {
    override fun map(response: SpokenLanguageResponse): SpokenLanguage {
        return SpokenLanguage(
            englishName = response.englishName ?: EMPTY_TEXT,
            name = response.name ?: EMPTY_TEXT
        )
    }

    override fun map(response: List<SpokenLanguageResponse>): List<SpokenLanguage> {
        return response.map { item -> map(item) }
    }
}