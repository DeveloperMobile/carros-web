package com.livroandroid.carros;

import com.livroandroid.carros.api.exception.ObjectNotFoundException;
import com.livroandroid.carros.domain.Carro;
import com.livroandroid.carros.domain.CarroService;
import com.livroandroid.carros.domain.dto.CarroDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class CarrosServiceTests {

	@Autowired
	public CarroService service;

	@Test
	void testSave() {
		Carro carro = new Carro();
		carro.setNome("Ferrari");
		carro.setTipo("esportivos");

		CarroDTO c = service.insert(carro);

		Assertions.assertNotNull(c);

		Long id = c.getId();
		Assertions.assertNotNull(id);

		// Buscar o objeto
		c = service.getCarroById(id);
		Assertions.assertNotNull(c);

		Assertions.assertEquals("Ferrari", c.getNome());
		Assertions.assertEquals("esportivos", c.getTipo());

		// Deletar o objeto
		service.delete(id);

		// Verifica se deletou
		try {
			Assertions.assertNotNull(service.getCarroById(id));
			Assertions.fail("O carro não foi excluído");
		} catch (ObjectNotFoundException e) {
			// OK
		}
	}

	@Test
	void testLista() {
		List<CarroDTO> carros = service.getCarros();
		Assertions.assertEquals(30, carros.size());
	}

	@Test
	void testListaPorTipo() {
		Assertions.assertEquals(10, service.getCarrosByTipo("classicos").size());
		Assertions.assertEquals(10, service.getCarrosByTipo("esportivos").size());
		Assertions.assertEquals(10, service.getCarrosByTipo("luxo").size());
		Assertions.assertEquals(0, service.getCarrosByTipo("x").size());
	}

	@Test
	void testGet() {
		CarroDTO c = service.getCarroById(11L);
		Assertions.assertNotNull(c);
		Assertions.assertEquals("Ferrari FF", c.getNome());
	}

}
