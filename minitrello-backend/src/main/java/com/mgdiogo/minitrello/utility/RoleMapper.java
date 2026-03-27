package com.mgdiogo.minitrello.utility;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.mgdiogo.minitrello.enums.UserRole;

@Component
public class RoleMapper {
    public static List<String> toAuthorityNames(UserRole role) {
        if (role == UserRole.ADMIN)
            return List.of("ADMIN", "USER");

        return List.of("USER");
    }

    public static List<GrantedAuthority> toAuthorities(UserRole role) {
        return toAuthorityNames(role).stream()
                .map(SimpleGrantedAuthority::new)
                .map(authority -> (GrantedAuthority) authority)
                .toList();
    }
}
