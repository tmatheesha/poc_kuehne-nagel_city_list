package com.kuehne_nagel.city_list.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kuehne_nagel.city_list.domain.util.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = 0")
@Table(name = "city",
        uniqueConstraints = {
                @UniqueConstraint(name = Constants.DUPLICATE_CITY_NAME,
                        columnNames = {"name"})
        })
@JsonIgnoreProperties(ignoreUnknown = true)
public class City extends AbstractEntity {

    private String name;

    @Lob
    private byte[] picture;
}
