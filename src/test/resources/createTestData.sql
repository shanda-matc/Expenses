-- Insert data into categories table
INSERT INTO categories (category_name) VALUES
                                           ('Fable'),
                                           ('Ancedote'),
                                           ('Mini-Saga'),
                                           ('Sketch Story'),
                                           ('Feghoot');

-- Insert data into user table
INSERT INTO user (username, email) VALUES
                                       ('user1', 'user1@example.com'),
                                       ('user2', 'user2@example.com'),
                                       ('user3', 'user3@example.com');

-- Insert data into stories table
INSERT INTO stories (title, category_id, content, publicationDate, author_id) VALUES
                                                                                  ('The Quantum Paradox', 37, 'In the year 2150, humanity unlocked the secrets of quantum physics, leading to unexpected consequences and a thrilling adventure through parallel dimensions.', NOW(), 21),
                                                                                  ('The Enigma Files', 38, 'Detective Sarah Mitchell unravels a series of mysterious cases, each more puzzling than the last, leading her to a secret society with dark intentions.', NOW(), 20),
                                                                                  ('Realm of Eldoria', 39, 'A young mage embarks on a quest to save the magical realm of Eldoria from an ancient evil, encountering mythical creatures and testing the limits of bravery.', NOW(), 22);

-- Insert data into comments table
INSERT INTO comments (story_id, user_id, content, creation_date) VALUES
                                                                     (1, 21, 'Great story!', NOW()),
                                                                     (2, 20, 'This detective story ', NOW()),
                                                                     (3, 22, 'The magical word!', NOW());


