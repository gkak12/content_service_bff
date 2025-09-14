package com.service.admin.model.mapper

import com.service.admin.model.dto.PointDto
import com.service.point.GrpcPointProtoDto
import org.mapstruct.*

@Mapper(
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring"
)
interface PointMapper {

    fun toDto(grpcPoint: GrpcPointProtoDto): PointDto
    fun toProtoDto(pointDto: PointDto): GrpcPointProtoDto
}