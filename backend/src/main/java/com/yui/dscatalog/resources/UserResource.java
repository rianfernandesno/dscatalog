package com.yui.dscatalog.resources;

import com.yui.dscatalog.dto.UserDTO;
import com.yui.dscatalog.dto.UserInsertDTO;
import com.yui.dscatalog.dto.UserUpdateDTO;
import com.yui.dscatalog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserResource {
    @Autowired
    private UserService UserService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll( Pageable pageable){

        Page<UserDTO> list = UserService.findAllPaged(pageable);

        return  ResponseEntity.ok().body(list);

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findByID(@PathVariable Long id){
        UserDTO UserDTO = UserService.findById(id);

        return  ResponseEntity.ok().body(UserDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public  ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto){
        UserDTO newDTO = UserService.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public  ResponseEntity<UserDTO> update(@PathVariable Long id,@Valid @RequestBody UserUpdateDTO dto){
        UserDTO newDto = UserService.update(id, dto);

        return ResponseEntity.ok().body(newDto);

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        UserService.delete(id);

        return  ResponseEntity.noContent().build();
    }

}
