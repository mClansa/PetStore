// 1 - Pacote
package petstore;

// 2 - Bibliotecas

import org.testng.annotations.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;

// 3 - Classe
public class Pet {
    // 3.1 - Atributos
    String uri = "https://petstore.swagger.io/v2/pet"; //endereço da entidade Pet


    // 3.2 - Métodos e Funções
    public String lerJson (String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    // Incluir - Creat - Post
    @Test(priority = 0) // Identifica o método ou função como um teste para o TestNG
    public void incluirPet () throws IOException {
        String jsonBody = lerJson("db/pet1.json");

        // Sintaxe Gherkin
        // Dado - Quando - Então
        // Given - When - Then

        given() // dado
                .contentType("application/json") //Comum em API REST - antigas era "text/xml"
                .log().all()
                .body(jsonBody)
        .when() // quando
                .post(uri)
        .then() // então
                .log().all()
                .statusCode(200)
                .body("name", is ("Atena"))
                .body("status", is ("available"))
                .body("category.name", is("BEACHCOIN1010"))
        ;
    }

    @Test (priority = 1)
    public void consultarPet(){
        String petId = "1501198142";

        String token =
        given()
                .contentType("aplication/json")
                .log().all()
        .when()
                .get(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is ("Atena"))
                .body("status", is ("available"))
                .body("category.name", is("BEACHCOIN1010"))
        .extract()
                .path("category.name")
        ;
        System.out.println("o token é "+ token);
    }

    @Test(priority = 2)
    public void alterarPet() throws IOException {
        String jsonBody = lerJson("db/pet2.json");

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(uri)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is ("Atena"))
                .body("status", is("sold"))
        ;
    }
    @Test
    public void excluirPet() {
        String petId = "1501198142";

        given()
                .contentType("aplication/json")
                .log().all()
                .when()
                .delete(uri + "/" + petId)
                .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(petId))
        ;
    }
}

