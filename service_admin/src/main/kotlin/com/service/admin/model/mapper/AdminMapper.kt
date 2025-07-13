package com.service.admin.model.mapper

import com.service.account.GrpcAdminProtoDto
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

    fun toDto(grpcAdmin: GrpcAdminProtoDto): AdminDto
    fun toProtoDto(adminDto: AdminDto): GrpcAdminProtoDto
}