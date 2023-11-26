-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2023-11-14 03:11:20.056

-- tables
-- Table: categories
CREATE TABLE categories (
    category_id int  NOT NULL,
    category_name varchar(50)  NOT NULL,
    CONSTRAINT categories_pk PRIMARY KEY (category_id)
);

-- Table: comments
CREATE TABLE comments (
    comment_id int  NOT NULL AUTO_INCREMENT,
    story_id int  NOT NULL,
    user_id int  NOT NULL,
    content varchar(50)  NOT NULL,
    creation_date timestamp  NOT NULL,
    user_user_id int  NOT NULL,
    CONSTRAINT comments_pk PRIMARY KEY (comment_id)
);

-- Table: ratings
CREATE TABLE ratings (
    rating_id int  NOT NULL AUTO_INCREMENT,
    story_id int  NOT NULL,
    user_id int  NOT NULL,
    stories_story_id int  NOT NULL,
    CONSTRAINT ratings_pk PRIMARY KEY (rating_id)
);

-- Table: stories
CREATE TABLE stories (
    story_id int  NOT NULL,
    title varchar(50)  NOT NULL,
    category_id int  NOT NULL,
    content varchar(50)  NOT NULL,
    publicationDate timestamp  NOT NULL,
    categories_category_id int  NOT NULL,
    author_id int  NOT NULL,
    user_user_id int  NOT NULL,
    CONSTRAINT stories_pk PRIMARY KEY (story_id)
);

-- Table: user
CREATE TABLE user (
    user_id int  NOT NULL,
    username varchar(50)  NOT NULL,
    email varchar(50)  NOT NULL,
    display_name varchar(50)  NOT NULL,
    bio varchar(50)  NOT NULL,
    CONSTRAINT user_pk PRIMARY KEY (user_id)
);

-- foreign keys
-- Reference: comments_user (table: comments)
ALTER TABLE comments ADD CONSTRAINT comments_user FOREIGN KEY comments_user (user_user_id)
    REFERENCES user (user_id);

-- Reference: ratings_stories (table: ratings)
ALTER TABLE ratings ADD CONSTRAINT ratings_stories FOREIGN KEY ratings_stories (stories_story_id)
    REFERENCES stories (story_id);

-- Reference: stories_categories (table: stories)
ALTER TABLE stories ADD CONSTRAINT stories_categories FOREIGN KEY stories_categories (categories_category_id)
    REFERENCES categories (category_id);

-- Reference: stories_user (table: stories)
ALTER TABLE stories ADD CONSTRAINT stories_user FOREIGN KEY stories_user (user_user_id)
    REFERENCES user (user_id);

-- End of file.

