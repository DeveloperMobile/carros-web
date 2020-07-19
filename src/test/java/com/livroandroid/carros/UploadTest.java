package com.livroandroid.carros;

import com.livroandroid.carros.api.upload.FireBaseStorageService;
import com.livroandroid.carros.api.upload.UploadInput;
import com.livroandroid.carros.api.upload.UploadOutput;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarrosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UploadTest {
    @Autowired
    protected TestRestTemplate rest;

    @Autowired
    private FireBaseStorageService service;

    private TestRestTemplate basicAuth() {
        return rest.withBasicAuth("admin", "123");
    }

    private UploadInput getUploadInput() {
        UploadInput uploadInput = new UploadInput();
        uploadInput.setFileName("nome.txt");
        uploadInput.setBase64("VGlhZ28gVmllaXJhCg==");
        uploadInput.setMimeType("text/plain");
        return uploadInput;
    }

    @Test
    public void testUploadFirebase() {
        String url = service.upload(getUploadInput());
        // Faz o Get na url
        ResponseEntity<String> urlResponse = rest.getForEntity(url, String.class);
        System.out.println(url);
        Assertions.assertEquals(HttpStatus.OK, urlResponse.getStatusCode());
    }

    @Test
    public void testUpoadApi() {
        UploadInput uploadInput = getUploadInput();

        // Insert
        ResponseEntity<UploadOutput> response = basicAuth().postForEntity("/api/v1/upload", uploadInput, UploadOutput.class);
        System.out.println(response);

        // Verifica se criou
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        UploadOutput out = response.getBody();
        Assertions.assertNotNull(out);
        System.out.println(out);

        String url = out.getUrl();

        // Faz o Get na URL
        ResponseEntity<String> urlResponse = rest.getForEntity(url, String.class);
        System.out.println(urlResponse);
        Assertions.assertEquals(HttpStatus.OK, urlResponse.getStatusCode());
    }
}
