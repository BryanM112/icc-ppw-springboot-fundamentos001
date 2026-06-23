package ec.edu.ups.icc.fundamentos001.users.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos001.core.dto.ErrorResponseDto;
import ec.edu.ups.icc.fundamentos001.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos001.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos001.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos001.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos001.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos001.users.models.UserModel;

/*
 * Implementación del servicio de usuarios.
 *
 * En esta clase se mueve la lógica que antes estaba dentro del controlador:
 * listar, buscar, crear, actualizar y eliminar usuarios.
 *
 * En esta práctica todavía no se usa repository ni base de datos.
 * Por eso se mantiene una lista en memoria dentro del servicio.
 */
@Service
public class UserServiceImpl {

   
}