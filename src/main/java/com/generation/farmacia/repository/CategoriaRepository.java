package com.generation.farmacia.repository;

import com.generation.farmacia.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByDescricaoContainingIgnoreCase(String descricao);
}
