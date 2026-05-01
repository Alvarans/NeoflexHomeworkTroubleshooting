package com.example.neoflexhomeworktroubleshooting.controller;

import com.example.neoflexhomeworktroubleshooting.entity.ProductEntity;
import com.example.neoflexhomeworktroubleshooting.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    public ProductEntity create(@RequestBody ProductEntity product) {
        return service.create(product);
    }

    @GetMapping("/{id}")
    public ProductEntity get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<ProductEntity> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public ProductEntity update(@PathVariable Long id, @RequestBody ProductEntity product) {
        return service.update(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/heavy")
    public void heavy() {
        service.heavyLoad();
    }
}
