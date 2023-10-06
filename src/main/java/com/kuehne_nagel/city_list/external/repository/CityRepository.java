package com.kuehne_nagel.city_list.external.repository;

import com.kuehne_nagel.city_list.domain.entities.City;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Boolean existsByName(String name);

    List<City> findDistinctByNameContaining(String searchBy, Pageable pageable);

    Long countByNameContaining(String searchBy);

    Boolean existsByNameAndIdNot(String name, Long id);

}
