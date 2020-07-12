package com.livroandroid.carros;


import com.livroandroid.carros.domain.Carro;
import com.livroandroid.carros.domain.CarroService;
import com.livroandroid.carros.domain.dto.CarroDTO;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarrosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarrosAPITests {
    @Autowired
    protected TestRestTemplate rest;

    @Autowired
    private CarroService service;

    private ResponseEntity<CarroDTO> getCarro(String url) {
        return rest.withBasicAuth("user", "123").getForEntity(url, CarroDTO.class);
    }

    private ResponseEntity<List<CarroDTO>> getCarros(String url) {
        return rest.withBasicAuth("user", "123").exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CarroDTO>>() {
                });
    }

    @Test
    public void testSave() {
        Carro carro = new Carro();
        carro.setNome("Porshe");
        carro.setTipo("esportivos");

        // Insert
        ResponseEntity response = rest.withBasicAuth("admin", "123").postForEntity("/api/v1/carros", carro, null);
        System.out.println(response);

        // Verifica se criou
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Busca o objeto
        String location = response.getHeaders().get("location").get(0);
        CarroDTO c = getCarro(location).getBody();

        Assertions.assertNotNull(c);
        Assertions.assertEquals("Porshe", c.getNome());
        Assertions.assertEquals("esportivos", c.getTipo());

        // Deletar o objeto
        rest.withBasicAuth("user", "123").delete(location);

        // Verificar se deletou
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getCarro(location).getStatusCode());
    }

    @Test
    public void testLista() {
        List<CarroDTO> carros = getCarros("/api/v1/carros").getBody();
        Assertions.assertNotNull(carros);
        Assertions.assertEquals(30, carros.size());
    }

    @Test
    public void testListaPorTipo() {
        Assertions.assertEquals(10, getCarros("/api/v1/carros/tipo/classicos").getBody().size());
        Assertions.assertEquals(10, getCarros("/api/v1/carros/tipo/esportivos").getBody().size());
        Assertions.assertEquals(10, getCarros("/api/v1/carros/tipo/luxo").getBody().size());
        Assertions.assertEquals(HttpStatus.NO_CONTENT, getCarros("/api/v1/carros/tipo/xxx").getStatusCode());
    }

    @Test
    public void testGetOk() {
        ResponseEntity<CarroDTO> response = getCarro("/api/v1/carros/11");
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        CarroDTO c = response.getBody();
        Assertions.assertEquals("Ferrari FF", c.getNome());
    }

    @Test
    public void testGetNotFound() {
        ResponseEntity response = getCarro("/api/carros/1100");
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
