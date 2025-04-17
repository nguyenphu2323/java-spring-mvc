package vn.hoidanit.laptopshop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import ch.qos.logback.core.model.Model;

@Controller

public class ItemController {
    @GetMapping("/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        return "client/product/detail";
    }
}
