-- Inserting test data for the 'categories' table
INSERT INTO categories (category_id, category_name) VALUES
                                                        (1, 'Anecdote'),
                                                        (2, 'Drabble'),
                                                        (3, 'Fable'),
                                                        (4, 'Feghoot'),
                                                        (5, 'Flash Fiction'),
                                                        (6, 'Frame Story'),
                                                        (7, 'Mini-saga'),
                                                        (8, 'Story Sequence'),
                                                        (9, 'Sketch Story'),
                                                        (10, 'Vignette');

-- Inserting test data for the 'user' table
INSERT INTO user (user_id, username, email, display_name, bio) VALUES
                                                                   (1, 'user1', 'user1@example.com', 'User One', 'Bio for User One'),
                                                                   (2, 'user2', 'user2@example.com', 'User Two', 'Bio for User Two'),
                                                                   (3, 'user3', 'user3@example.com', 'User Three', 'Bio for User Three');

-- Inserting test data for the 'stories' table
INSERT INTO stories (story_id, title, category_id, content, publicationDate, categories_category_id, author_id, user_user_id) VALUES
                                                                                                                                  (1, 'Story One', 1, 'Content for Story One', '2023-11-14 12:00:00', 1, 1, 1),
                                                                                                                                  (2, 'Story Two', 2, 'Content for Story Two', '2023-11-14 12:30:00', 2, 2, 2),
                                                                                                                                  (3, 'Story Three', 3, 'Content for Story Three', '2023-11-14 13:00:00', 3, 3, 3);

-- Inserting test data for the 'comments' table
INSERT INTO comments (comment_id, story_id, user_id, content, creation_date, user_user_id) VALUES
                                                                                               (1, 1, 1, 'Comment for Story One', '2023-11-14 12:15:00', 1),
                                                                                               (2, 2, 2, 'Comment for Story Two', '2023-11-14 12:45:00', 2),
                                                                                               (3, 3, 3, 'Comment for Story Three', '2023-11-14 13:15:00', 3);

-- Inserting test data for the 'ratings' table
INSERT INTO ratings (rating_id, story_id, user_id, stories_story_id) VALUES
                                                                         (1, 1, 1, 1),
                                                                         (2, 2, 2, 2),
                                                                         (3, 3, 3, 3);
