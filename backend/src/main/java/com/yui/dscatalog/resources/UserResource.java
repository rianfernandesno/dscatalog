package com.yui.dscatalog.resources;

import com.yui.dscatalog.dto.UserDTO;
import com.yui.dscatalog.dto.UserInsertDTO;
import com.yui.dscatalog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserResource {
    @Autowired
    private UserService UserService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll( Pageable pageable){

        Page<UserDTO> list = UserService.findAllPaged(pageable);

        return  ResponseEntity.ok().body(list);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findByID(@PathVariable Long id){
        UserDTO UserDTO = UserService.findById(id);

        return  ResponseEntity.ok().body(UserDTO);
    }

    @PostMapping
    public  ResponseEntity<UserDTO> insert(@RequestBody UserInsertDTO dto){
        UserDTO newDTO = UserService.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(newDTO);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO dto){
        dto = UserService.update(id, dto);

        return ResponseEntity.ok().body(dto);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        UserService.delete(id);

        return  ResponseEntity.noContent().build();
    }

}
