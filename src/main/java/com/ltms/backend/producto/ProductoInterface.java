package com.ltms.backend.producto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoInterface extends JpaRepository<Producto, Integer> {
}
