package com.example.neoflexhomeworktroubleshooting;

import com.example.neoflexhomeworktroubleshooting.entity.ProductEntity;
import com.example.neoflexhomeworktroubleshooting.exception.ProductNotFoundException;
import com.example.neoflexhomeworktroubleshooting.repository.ProductRepository;
import com.example.neoflexhomeworktroubleshooting.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void create_shouldSaveProduct() {
        ProductEntity input = new ProductEntity();
        input.setName("Phone");

        ProductEntity saved = new ProductEntity();
        saved.setId(1L);
        saved.setName("Phone");

        when(repository.save(input)).thenReturn(saved);

        ProductEntity result = service.create(input);

        assertEquals(1L, result.getId());
        assertEquals("Phone", result.getName());

        verify(repository).save(input);
    }

    @Test
    void get_shouldReturnProduct() {
        ProductEntity product = new ProductEntity();
        product.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        ProductEntity result = service.get(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void get_shouldThrowException_whenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.get(1L));

        verify(repository).findById(1L);
    }

    @Test
    void getAll_shouldReturnList() {
        List<ProductEntity> list = List.of(new ProductEntity(), new ProductEntity());

        when(repository.findAll()).thenReturn(list);

        List<ProductEntity> result = service.getAll();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void update_shouldModifyAndSave() {
        ProductEntity existing = new ProductEntity();
        existing.setId(1L);
        existing.setName("Old");
        existing.setPrice(new BigDecimal(20));

        ProductEntity update = new ProductEntity();
        update.setName("New");
        update.setPrice(new BigDecimal(30));

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProductEntity result = service.update(1L, update);

        assertEquals("New", result.getName());
        verify(repository).save(existing);
    }

    @Test
    void delete_shouldCallRepository() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowAndLogError() {
        doThrow(new RuntimeException("DB error"))
                .when(repository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> service.delete(1L));

        verify(repository).deleteById(1L);
    }
}
