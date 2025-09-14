package com.service.admin.api

import com.service.admin.model.dto.PointDto
import com.service.admin.service.PointService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/point")
class PointController (
    private val pointService: PointService
){

    @GetMapping("/get/{id}")
    fun getPoint(@PathVariable("id") id: Long) : Mono<PointDto>{
        return pointService.findPointById(id)
    }
}