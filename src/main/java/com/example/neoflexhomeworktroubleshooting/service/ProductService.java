package com.example.neoflexhomeworktroubleshooting.service;

import com.example.neoflexhomeworktroubleshooting.entity.ProductEntity;
import com.example.neoflexhomeworktroubleshooting.exception.ProductNotFoundException;
import com.example.neoflexhomeworktroubleshooting.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductEntity create(ProductEntity product) {
        log.debug("Creating product: {}", product.getName());
        ProductEntity saved = repository.save(product);
        log.info("Product created with id {}", saved.getId());
        return saved;
    }

    public ProductEntity get(Long id) {
        log.debug("Fetching product by id {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found: {}", id);
                    return new ProductNotFoundException(id);
                });
    }

    public List<ProductEntity> getAll() {
        log.debug("Fetching all products");
        return repository.findAll();
    }

    public ProductEntity update(Long id, ProductEntity product) {
        ProductEntity existing = get(id);
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        log.info("Updating product id {}", id);
        return repository.save(existing);
    }

    public void delete(Long id) {
        try {
            log.info("Deleting product id {}", id);
            repository.deleteById(id);
        } catch (Exception ex) {
            log.error("Error deleting product {}", id, ex);
            throw ex;
        }
    }

    // Метод с высокой нагрузкой
    public void heavyLoad() {
        log.trace("Starting heavy load");

        IntStream.range(0, 8)
                .parallel()
                .forEach(thread -> {

                    double result = 0;

                    for (long i = 0; i < 1_000_000_000L; i++) {
                        result += Math.sqrt(i) * Math.sin(i);
                    }

                    log.debug("Thread {} finished with {}", thread, result);
                });

        log.info("Heavy load completed");
    }
}
