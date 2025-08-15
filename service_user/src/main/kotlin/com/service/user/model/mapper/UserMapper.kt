package com.service.user.model.mapper

import com.service.account.GrpcUserProtoDto
import com.service.user.model.dto.UserDto
import org.mapstruct.*

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring"
)
interface UserMapper {

    fun toDto(grpcUser: GrpcUserProtoDto): UserDto
    fun toProtoDto(userDto: UserDto): GrpcUserProtoDto
}