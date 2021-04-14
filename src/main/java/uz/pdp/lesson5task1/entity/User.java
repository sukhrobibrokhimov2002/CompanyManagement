package uz.pdp.lesson5task1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.lesson5task1.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User extends AbsEntity implements UserDetails {

    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String password;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    private String position;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    private String verifyPassword;


    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;
    private boolean isEnabled = false;


    public User(String fullName, String password, @Email String email, String position, Set<Role> roles,boolean isEnabled) {
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.position = position;
        this.roles = roles;
        this.isEnabled=isEnabled;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }


    public User(String fullName, String password, Set<Role> roles, @Email String email, String position, boolean isEnabled) {
        this.fullName = fullName;
        this.password = password;
        this.roles = roles;
        this.email = email;
        this.position = position;
        this.isEnabled = isEnabled;
    }
}
