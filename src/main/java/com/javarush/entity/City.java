package com.javarush.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "city")
@SqlResultSetMapping(name = "mapping_city_id", columns = { @ColumnResult(name = "id") })
@NamedNativeQuery(name = City.RANDOM_20_CITY_ID_QUERY, resultClass = Integer.class,
        query = "select c.id from world.city c order by rand() limit 20",
        resultSetMapping = "mapping_city_id")
@Getter
@Setter
public class City {
    public static final String RANDOM_20_CITY_ID_QUERY = "random_20_city_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    private String district;

    private Integer population;
}
