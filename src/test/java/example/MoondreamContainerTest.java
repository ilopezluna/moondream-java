package example;

import io.restassured.http.Header;
import org.junit.Assert;
import org.junit.Test;
import org.testcontainers.containers.ContainerFetchException;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.List;

import static io.restassured.RestAssured.given;

public class MoondreamContainerTest {

    @Test
    public void visionModel() throws IOException, InterruptedException {
        try (OllamaContainer ollama = new OllamaContainer(DockerImageName.parse("tc-ollama-moondream").asCompatibleSubstituteFor("ollama/ollama:0.1.42"))) {
            try {
                ollama.start();
            } catch (ContainerFetchException ex) {
                createImage();
                ollama.start();
            }

            URL resourceUrl = MoondreamContainerTest.class.getResource("/whale.jpeg");
            byte[] fileContent = FileUtils.readFileToByteArray(new File(resourceUrl.getFile()));
            String image = Base64.getEncoder().encodeToString(fileContent);

            CompletionResponse response = given()
                    .baseUri(ollama.getEndpoint())
                    .header(new Header("Content-Type", "application/json"))
                    .body(new CompletionRequest("moondream:latest", "Describe the image.", image))
                    .post("/api/generate")
                    .getBody().as(CompletionResponse.class);

            Assert.assertTrue(response.response.contains("a whale swimming in the ocean"));
        }
    }

    public void createImage() throws IOException, InterruptedException {
        String newImageName = "tc-ollama-moondream";
        try (OllamaContainer ollama = new OllamaContainer("ollama/ollama:0.1.42")) {
            ollama.start();
            ollama.execInContainer("ollama", "pull", "moondream");
            String modelName = given()
                    .baseUri(ollama.getEndpoint())
                    .get("/api/tags")
                    .jsonPath()
                    .getString("models[0].name");
            Assert.assertEquals("moondream:latest", modelName);
            ollama.commitToImage(newImageName);
        }
    }

    record CompletionRequest(String model, String prompt, List<String> images, boolean stream) {
        public CompletionRequest(String model, String prompt, String image) {
            this(model, prompt, List.of(image), false);
        }
    }

    record CompletionResponse(String response) {}

}
