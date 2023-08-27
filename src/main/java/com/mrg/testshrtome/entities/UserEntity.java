package com.mrg.testshrtome.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;


@Table(name = "USERS")
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity implements UserDetails  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_NAME",unique = true)
    private String userName;
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ENABLED",columnDefinition = "varchar(1) DEFAULT  'T'")
    private String enabled = "T";

//    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE"
    ,joinColumns = @JoinColumn(name = "USER_ID",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name = "ROLE_ID",referencedColumnName = "id"))
    private Set<RoleEntity> roles =new HashSet<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled.equals("T");
    }
}
