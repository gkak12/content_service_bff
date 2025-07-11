package com.service.admin.model.mapper

import com.service.account.GrpcAdminDto
import com.service.admin.model.dto.AdminDto
import org.mapstruct.*

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring"
)
interface AdminMapper {

    fun toDto(grpcAdmin: GrpcAdminDto): AdminDto
    fun toProtoDto(adminDto: AdminDto): GrpcAdminDto
}