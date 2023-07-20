package dev.hub.security.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoles {

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Enumerated(EnumType.STRING)
    private StatusRoles statusRoles;

    private String apiKeyToken;

    @Column(unique = true)
    private String userId;
    private String passWord ;
    private LocalDate expireDate;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate createDate;
    private LocalDateTime updateDateTime;

    //admin permission add
    public void setAllowPermission() {
        this.roleType = RoleType.ADMIN;
    }

    //delete permission
    public void deletePermission(){
        this.roleType = RoleType.GUEST;
    }

}
