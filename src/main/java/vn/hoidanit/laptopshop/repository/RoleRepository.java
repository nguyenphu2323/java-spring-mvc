package vn.hoidanit.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.laptopshop.domain.Role;
import vn.hoidanit.laptopshop.domain.User;
import java.util.List;
import java.util.Optional;

//curd: create, read, update, delete
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

}
