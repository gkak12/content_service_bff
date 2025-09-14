package com.service.admin.service

import com.service.admin.model.dto.PointDto
import com.service.admin.model.request.RequestPointPagingDto
import reactor.core.publisher.Mono

interface PointService {

    fun findPointById(id: Long): Mono<PointDto>
}