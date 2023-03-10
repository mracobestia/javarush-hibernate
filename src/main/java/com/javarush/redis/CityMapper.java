package com.javarush.redis;

import com.javarush.entity.City;
import com.javarush.entity.CountryLanguage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CityMapper {

    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    @Mapping(source = "country.languages", target = "languages")
    @Mapping(source = "country.code", target = "countryCode")
    @Mapping(source = "country.code2", target = "alternativeCountryCode")
    @Mapping(source = "country.name", target = "countryName")
    @Mapping(source = "country.continent", target = "continent")
    @Mapping(source = "country.region", target = "countryRegion")
    @Mapping(source = "country.surfaceArea", target = "countrySurfaceArea")
    @Mapping(source = "country.population", target = "countryPopulation")
    CityCountry toCityCountry(City source);

    List<Language> toListLanguage(List<CountryLanguage> source);

    Language toLanguage(CountryLanguage source);

}
