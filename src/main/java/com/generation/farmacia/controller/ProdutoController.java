package com.generation.farmacia.controller;

import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@GetMapping
	public ResponseEntity<List<Produto>> listarProdutos() {
		List<Produto> produtos = produtoRepository.findAll();
		return ResponseEntity.ok(produtos);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		return produto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Produto> cadastrarProduto(@RequestBody Produto produto) {
		Produto novoProduto = produtoRepository.save(produto);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
		if (produtoRepository.existsById(id)) {
			produtoRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto) {
		if (produtoRepository.existsById(id)) {
			produto.setId(id);
			Produto produtoAtualizado = produtoRepository.save(produto);
			return ResponseEntity.ok(produtoAtualizado);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Produto>> consultarPorTitulo(@PathVariable String titulo) {
		List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase(titulo);
		if (produtos.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(produtos);
	}

	@PostMapping("/desconto/{id}")
	public ResponseEntity<String> definirDescontoParaProduto(@PathVariable Long id,
			@RequestBody Map<String, BigDecimal> requestBody) {
		Optional<Produto> produtoOptional = produtoRepository.findById(id);
		if (produtoOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		BigDecimal desconto = requestBody.getOrDefault("desconto", BigDecimal.ZERO);
		if (desconto.compareTo(BigDecimal.ZERO) < 0 || desconto.compareTo(BigDecimal.valueOf(100)) > 0) {
			return ResponseEntity.badRequest().body("O desconto deve estar entre 0% e 100%.");
		}

		Produto produto = produtoOptional.get();
		produto.setPrice(produto.calcularPrecoComDesconto(desconto));
		produtoRepository.save(produto);

		return ResponseEntity.ok("Desconto definido: " + desconto + "%");
	}
}
