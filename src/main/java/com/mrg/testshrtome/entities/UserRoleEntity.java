package com.mrg.testshrtome.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "USERS_ROLES")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleEntity extends BaseEntity implements GrantedAuthority {

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private RoleEntity role;

    @Override
    public String getAuthority() {
        return role.getRole();
    }
}
