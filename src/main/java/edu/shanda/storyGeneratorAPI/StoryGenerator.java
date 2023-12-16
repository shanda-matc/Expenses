package edu.shanda.storyGeneratorAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoryGenerator {
	private String author;
	private String id;
	private String title;
	private String story;
	private String moral;

	public String getAuthor() {
		return author;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getStory() {
		return story;
	}

	public String getMoral() {
		return moral;
	}
	public void setStory(String storyContent) {
		this.story = storyContent;
	}

	@Override
	public String toString() {
		return "StoryGenerator{" +
				"author='" + author + '\'' +
				", id='" + id + '\'' +
				", title='" + title + '\'' +
				", story='" + story + '\'' +
				", moral='" + moral + '\'' +
				'}';
	}
}
