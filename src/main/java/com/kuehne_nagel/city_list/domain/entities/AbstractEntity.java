package com.kuehne_nagel.city_list.domain.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
@Setter
@Getter
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column( name = "created_date",
            updatable = false,
            nullable = false )
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column( name = "last_modified_date",
            nullable = false )
    private LocalDateTime lastModifiedDate;

    @ColumnDefault( "false" )
    private boolean isDeleted;

    private String lastUpdatedUserId;

    private String lastUpdatedUserName;
}
