package com.mrg.testshrtome.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "ROLES")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleEntity extends BaseEntity  implements GrantedAuthority{

    @Column(name = "ROLE",unique = true)
    private String role;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
    private Set<UserEntity> users =new HashSet<>();


    @Override
    public String getAuthority() {
        return role;
    }
}
