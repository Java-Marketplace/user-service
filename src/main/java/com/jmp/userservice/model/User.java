package com.jmp.userservice.model;

import com.jmp.userservice.constant.UserStatus;
import com.jmp.userservice.validation.annotation.ValidLinks;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

@Table(name = "users")
@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String birthDate;

    @Column
    private String aboutMe;

    @ElementCollection
    @CollectionTable(name = "user_links", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "link_type")
    @Column
    @ValidLinks
    private Map<String, String> links;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @PrePersist
    protected void onCreate() {
        if (this.registrationDate == null) {
            this.registrationDate = new Timestamp(System.currentTimeMillis());
        }
        if (this.status == null) {
            this.status = UserStatus.ACTIVE;
        }
    }
}
