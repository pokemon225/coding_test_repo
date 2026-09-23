package com.colorvotes;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/colors")
public class ColorApiController {

    private final ColorRepository colors;

    public ColorApiController(ColorRepository colors) {
        this.colors = colors;
    }

    @GetMapping("/{id}/votes")
    public ColorVotes votes(@PathVariable long id) {
        return colors.findVotes(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown color: " + id));
    }
}
