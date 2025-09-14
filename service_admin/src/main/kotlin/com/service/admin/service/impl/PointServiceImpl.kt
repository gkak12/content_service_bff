package com.service.admin.service.impl

import com.service.admin.model.dto.PointDto
import com.service.admin.model.mapper.PointMapper
import com.service.admin.service.PointService
import com.service.common.enums.ErrorCodeEnums
import com.service.common.exception.ContentException
import com.service.grpc.service.GrpcClientPointService
import com.service.point.GrpcPointRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PointServiceImpl (
    private val grpcClientPointService: GrpcClientPointService,
    private val pointMapper: PointMapper
): PointService {

    override fun findPointById(id: Long): Mono<PointDto> {
        return grpcClientPointService.findPointById(
            GrpcPointRequest.newBuilder()
                .setPointSeq(id)
                .build()
        ).map { response ->
            if(response.statusCode != "OK"){
                throw ContentException(ErrorCodeEnums.INTERNAL_SERVER_ERROR)
            }

            response.dtoList
                .firstOrNull()
                ?.let { pointMapper.toDto(it) }
                ?: PointDto()
        }
    }
}