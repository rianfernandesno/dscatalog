package com.yui.dscatalog.repositories;

import com.yui.dscatalog.models.Role;
import com.yui.dscatalog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
