package edu.shanda.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.shanda.entity.Story;
import edu.shanda.util.DaoFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class StoryDao {
    private final GenericDao<Story> storyDao = DaoFactory.createDao(Story.class);
    private final Logger logger = LogManager.getLogger(this.getClass());
    private static final String chatGptApiUrl = "https://api.openai.com/v1/chat/completions";
    private static final String apiKey = "";

    public String generateShortStory(String prompt) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(chatGptApiUrl);

        String requestBody = String.format("{\"messages\": [{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}, {\"role\": \"user\", \"content\": \"%s\"}]}", prompt);

        String response = target
                .request(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .post(Entity.entity(requestBody, MediaType.APPLICATION_JSON), String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonResponse = mapper.readTree(response);
            String generatedStory = jsonResponse.get("choices").get(0).get("message").get("content").asText();
            logger.debug("Generated Story: {}", generatedStory);
            return generatedStory;
        } catch (Exception e) {
            logger.error("Error parsing ChatGPT response.", e);
            return null;
        }
    }

    public void saveOrUpdate(Story newStory) {
        try {
            storyDao.saveOrUpdate(newStory);
        } catch (Exception e) {
            logger.error("Error saving or updating the story.", e);
        }
    }
}
