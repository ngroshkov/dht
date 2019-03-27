package org.iot.dht.controller

import org.iot.dht.service.DHTService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody


/**
 * Created by Nikolay Groshkov on 24-Mar-2019.
 */
@Controller
class IndexController {

    @Autowired
    private lateinit var dhtService: DHTService

    @GetMapping("/")
    fun index() = "index"

    @GetMapping("/shadow", headers = ["Accept=*/*"], produces = ["application/json"])
    @ResponseBody
    fun shadow() = dhtService.dhtCurrentState()

}