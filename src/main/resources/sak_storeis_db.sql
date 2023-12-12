-- Table: categories
CREATE TABLE categories (
                            category_id INT NOT NULL AUTO_INCREMENT,
                            category_name VARCHAR(50) NOT NULL,
                            PRIMARY KEY (category_id)
);

-- Table: comments
CREATE TABLE comments (
                          comment_id INT NOT NULL AUTO_INCREMENT,
                          story_id INT NOT NULL,
                          user_id INT NOT NULL,
                          content VARCHAR(50) NOT NULL,
                          creation_date TIMESTAMP NOT NULL,
                          PRIMARY KEY (comment_id),
                          CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES user (user_id)
);

-- Table: ratings
CREATE TABLE ratings (
                         rating_id INT NOT NULL AUTO_INCREMENT,
                         story_id INT NOT NULL,
                         user_id INT NOT NULL,
                         PRIMARY KEY (rating_id),
                         CONSTRAINT fk_ratings_stories FOREIGN KEY (story_id) REFERENCES stories (story_id),
                         CONSTRAINT fk_ratings_user FOREIGN KEY (user_id) REFERENCES user (user_id)
);

-- Table: stories
CREATE TABLE stories (
                         story_id INT NOT NULL AUTO_INCREMENT,
                         title VARCHAR(50) NOT NULL,
                         category_id INT NOT NULL,
                         content VARCHAR(50) NOT NULL,
                         publicationDate TIMESTAMP NOT NULL,
                         author_id INT NOT NULL,
                         PRIMARY KEY (story_id),
                         CONSTRAINT fk_stories_categories FOREIGN KEY (category_id) REFERENCES categories (category_id),
                         CONSTRAINT fk_stories_user FOREIGN KEY (author_id) REFERENCES user (user_id)
);

-- Table: user
CREATE TABLE user (
                      user_id INT NOT NULL AUTO_INCREMENT,
                      username VARCHAR(50) NOT NULL,
                      email VARCHAR(50) NOT NULL,
                      PRIMARY KEY (user_id)
);

