package com.kuehne_nagel.city_list.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kuehne_nagel.city_list.domain.util.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(name = Constants.DUPLICATE_CITY_NAME,
                        columnNames = {"name"})
        })
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends AbstractEntity {

    @Column(length = 500)
    private String password;

    @Column(length = 100)
    private String email;

    @Column(length = 500)
    private String name;

    @Column(length = 100)
    private String userRole;

}
