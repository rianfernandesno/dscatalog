package com.yui.dscatalog.services;

import com.yui.dscatalog.dto.RoleDTO;
import com.yui.dscatalog.dto.UserDTO;
import com.yui.dscatalog.dto.UserInsertDTO;
import com.yui.dscatalog.dto.UserUpdateDTO;
import com.yui.dscatalog.models.Role;
import com.yui.dscatalog.models.User;
import com.yui.dscatalog.projections.UserDetailsProjection;
import com.yui.dscatalog.repositories.RoleRepository;
import com.yui.dscatalog.repositories.UserRepository;
import com.yui.dscatalog.services.exceptions.DatabaseException;
import com.yui.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService  implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {

        Page<User> list = repository.findAll(pageable);
        return list.map(x -> new UserDTO(x));

    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {

        User entity = new User();

        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        entity = repository.save(entity);

        return new UserDTO(entity);
    }


    @Transactional
    public UserDTO update(Long id, UserUpdateDTO dto) {

        try {
            User entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);

            return new UserDTO(entity);
        }
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void delete(Long id) {
        if (!repository.existsById(id)){
            throw new ResourceNotFoundException("Id not found " + id);
        }

        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {

            throw new DatabaseException("Integrity violation");
        }
    }


    private void copyDtoToEntity(UserDTO dto, User entity) {

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();

        for (RoleDTO roleDTO : dto.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDTO.getId());

            entity.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<UserDetailsProjection> result =  repository.searchUserAndRolesByEmail(email);
        if(result.size() == 0){
            throw  new UsernameNotFoundException("Email not found");
        }

        User user =  new User();
        user.setEmail(result.get(0).getUsername());
        user.setPassword(result.get(0).getPassword());
        for(UserDetailsProjection projection : result){
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }
        return user;
    }
}
