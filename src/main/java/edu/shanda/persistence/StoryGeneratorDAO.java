package edu.shanda.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.shanda.storyGeneratorAPI.StoryGenerator;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * The type Story generator dao.
 */
public class StoryGeneratorDAO {

    /**
     * Gets stories.
     *
     * @return the stories
     * @throws IOException the io exception
     */
    public List<StoryGenerator> getStories() throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target =
                client.target("https://shortstories-api.onrender.com/stories");

        String response = target.request(MediaType.APPLICATION_JSON).get(String.class);

        ObjectMapper mapper = new ObjectMapper();
        List<StoryGenerator> stories = null;
        try {
            stories = mapper.readValue(response, new TypeReference<List<StoryGenerator>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return stories;
    }
}
