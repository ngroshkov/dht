package org.iot.dht.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


/**
 * Created by Nikolay Groshkov on 24-Mar-2019.
 */
@Controller
class IndexController {

    @GetMapping("/")
    fun index() = "index"

}