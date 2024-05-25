package ir.farzadafi.mapper;

import ir.farzadafi.dto.UserSaveRequest;
import ir.farzadafi.dto.UserSaveResponse;
import ir.farzadafi.dto.UserUpdateRequest;
import ir.farzadafi.dto.UserUpdateResponse;
import ir.farzadafi.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(source = "address.provinceId", target = "address.province.id"),
            @Mapping(source = "address.countyId", target = "address.county.id"),
            @Mapping(source = "address.cityId", target = "address.city.id"),
    })
    User requestToModel(UserSaveRequest userSaveRequest);

    UserSaveResponse modelToResponse(User user);

    User updateRequestToModel(UserUpdateRequest userUpdateRequest);

    UserUpdateResponse modelToUpdateResponse(User user);
}