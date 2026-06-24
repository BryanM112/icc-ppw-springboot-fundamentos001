package ec.edu.ups.icc.fundamentos001.users.services;

import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos001.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos001.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos001.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos001.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos001.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos001.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos001.users.models.UserModel;
import ec.edu.ups.icc.fundamentos001.users.repositories.UserRepository;

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
public class UserServiceImpl implements UserService {

    /*
 * Implementación del servicio de usuarios.
 *
 * En esta clase se reemplaza la lista en memoria por UserRepository.
 * El repositorio se encarga de comunicarse con PostgreSQL mediante JPA.
 */
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /*
     * Retorna todos los usuarios almacenados en PostgreSQL.
     *
     * El repositorio devuelve entidades.
     * El mapper convierte entidades a modelos.
     * Luego convierte modelos a DTOs de respuesta.
     */
    @Override
    public List<UserResponseDto> findAll() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toModelFromEntity)
                .map(UserMapper::toResponse)
                .toList();
    }

    /*
     * Busca un usuario por id.
     *
     * Si no existe, lanza un error simple.
     * El manejo formal de errores se implementará después.
     */
    @Override
    public UserResponseDto findOne(Long id) {

        return userRepository.findById(id)
                .map(UserMapper::toModelFromEntity)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    /*
     * Crea un nuevo usuario.
     *
     * Convierte DTO a Model.
     * Convierte Model a Entity.
     * Guarda Entity en PostgreSQL.
     * Convierte Entity guardada a Model.
     * Devuelve Response DTO.
     */
    @Override
    public UserResponseDto create(CreateUserDto dto) {

        UserModel model = UserMapper.toModelFromDTO(dto);

        UserEntity entity = UserMapper.toEntityFromModel(model);

        UserEntity savedEntity = userRepository.save(entity);

        UserModel savedModel = UserMapper.toModelFromEntity(savedEntity);

        return UserMapper.toResponse(savedModel);
    }

    /*
     * Actualiza completamente un usuario.
     *
     * Busca la entidad existente.
     * Actualiza los campos editables.
     * Guarda los cambios.
     * Devuelve DTO de respuesta.
     */
    @Override
    public UserResponseDto update(Long id, UpdateUserDto dto) {

        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());

        UserEntity savedEntity = userRepository.save(entity);

        UserModel model = UserMapper.toModelFromEntity(savedEntity);

        return UserMapper.toResponse(model);
    }

    /*
     * Actualiza parcialmente un usuario.
     *
     * Solo actualiza los campos enviados en el DTO.
     */
    @Override
    public UserResponseDto partialUpdate(Long id, PartialUpdateUserDto dto) {

        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }

        UserEntity savedEntity = userRepository.save(entity);

        UserModel model = UserMapper.toModelFromEntity(savedEntity);

        return UserMapper.toResponse(model);
    }

    /*
 * Elimina lógicamente un usuario por id.
 *
 * Primero verifica que exista.
 * Luego marca la entidad como eliminada usando deleted = true.
 * No elimina físicamente el registro de la base de datos.
 */
@Override
public void delete(Long id) {

    UserEntity entity = userRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException("User not found"));

    entity.setDeleted(true);

    userRepository.save(entity);
}

}