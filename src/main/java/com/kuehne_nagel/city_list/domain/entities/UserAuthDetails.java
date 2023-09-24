package com.kuehne_nagel.city_list.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = 0")
@Table(name = "user_auth_details")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAuthDetails extends AbstractEntity {

    private Long userId;
    @LastModifiedDate
    private LocalDateTime lastLogin;

    @Column(length = 500)
    private String accessToken;

    @Column(length = 500)
    private String refreshToken;

}
