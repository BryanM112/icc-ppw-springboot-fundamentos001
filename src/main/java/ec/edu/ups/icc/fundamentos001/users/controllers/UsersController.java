package ec.edu.ups.icc.fundamentos001.users.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos001.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos001.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos001.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos001.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos001.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos001.users.models.UserModel;
import ec.edu.ups.icc.fundamentos001.users.services.UserService;

/*
 * Controlador REST encargado de exponer los endpoints HTTP
 * para la gestión de usuarios.
 *
 * En esta práctica el controlador ya no contiene la lógica del CRUD.
 * Solo recibe la petición y delega la operación al servicio.
 */
@RestController
@RequestMapping("/users")
public class UsersController {


    private List<UserModel> users = new ArrayList<>();
    private int currentId = 1;

    @GetMapping
    public List<UserResponseDto> findAll() {

        // Programación tradicional iterativa para mapear cada User a UserResponseDto
        //List<UserResponseDto> dtos = new ArrayList<>();
        //for (UserModel user : users) {
        //    dtos.add(UserMapper.toResponse(user));
        //}
        //return dtos;

        // Programación funcional para mapear cada User a UserResponseDto
        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }


    @GetMapping("/{id}")
    public Object findOne(@PathVariable Long id) {

      // Programación tradicional iterativa para mapear cada User a UserResponseDto
      // Busqueda Lineal
       // for (UserModel user : users) {
        //    if (user.getId().equals(id)) {
        //        return UserMapper.toResponse(user);
        //    }
        //}
        //return new Object() {
        //    public String error = "User not found";
        //};

      // Programación funcional para mapear cada User a UserResponseDto
      // Busqueda Lineal
    return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
              .map(user -> (Object) UserMapper.toResponse(user))
            .orElseGet(() -> new Object() {
                public String error = "User not found";
            });
    }




    @PostMapping
    public UserResponseDto create(@RequestBody CreateUserDto dto) {

    UserModel user = UserMapper.toModel(dto);

    user.setId((long) currentId++);

    users.add(user);

    return UserMapper.toResponse(user);
    }


 @PutMapping("/{id}")
    public Object update(@PathVariable Long id, @RequestBody UpdateUserDto dto) {

        // Programacion tradicional iterativa
        //for (UserModel user : users) {
        //if (user.getId().equals(id)) {
        //user.setName(dto.getName());
        //user.setEmail(dto.getEmail());
        //return UserMapper.toResponse(user);
        //}
        //}
        //return new Object() {
        //public String error = "UserModel not found";
        //};

        // Programacion funcional
        UserModel user = users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
        if (user == null)
            return new Object() {
                public String error = "UserModel not found";
            };

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        return UserMapper.toResponse(user);
    }



    
@PatchMapping("/{id}")
    public Object partialUpdate(@PathVariable Long id, @RequestBody PartialUpdateUserDto dto) {

        // Programacion tradicional iterativa
        //for (UserModel user : users) {
            // ESTE ES EL CAMBIO pero deberia estar en un metodo aparte para evitar
            // duplicacion de codigo y mejorar mantenibilidad con separacion de
            // responsabilidades.
        //    if (user.getId().equals(id)) {
        //        if (dto.getName() != null)
        //            user.setName(dto.getName());
        //        if (dto.getEmail() != null)
        //            user.setEmail(dto.getEmail());
        //        return UserMapper.toResponse(user);
        //    }
        //}
        //return new Object() {
        //    public String error = "UserModel not found";
        //};

        // Programación funcional
        // Búsqueda lineal del usuario por id
        UserModel user = users.stream().filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user == null)
            return new Object() {
                public String error = "UserModel not found";
            };

        if (dto.getName() != null)
            user.setName(dto.getName());
        if (dto.getEmail() != null)
            user.setEmail(dto.getEmail());

        return UserMapper.toResponse(user);
    }



   @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) {
        
        // Programacion funcional
        boolean exists = users.removeIf(u -> u.getId().equals(id));
        if (!exists)
            return new Object() {
                public String error = "User not found";
            };

        return new Object() {
            public String message = "Deleted successfully";
        };
    }

}

