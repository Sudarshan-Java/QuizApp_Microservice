CREATE DATABASE IF NOT EXISTS quiz_app;
USE quiz_app;

CREATE TABLE IF NOT EXISTS quizzes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    topic VARCHAR(255) NOT NULL,
    description VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question_text VARCHAR(1000) NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    correct_option VARCHAR(1) NOT NULL,
    quiz_id BIGINT NOT NULL,
    CONSTRAINT fk_question_quiz FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);

-- Sample data
INSERT INTO quizzes (title, topic, description)
SELECT 'Java Basics Quiz', 'Java', 'Test your basic Java knowledge'
WHERE NOT EXISTS (SELECT 1 FROM quizzes WHERE title = 'Java Basics Quiz');

INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option, quiz_id)
SELECT 'Which keyword is used to create a class in Java?', 'class', 'struct', 'define', 'new', 'A', id
FROM quizzes
WHERE title = 'Java Basics Quiz'
  AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which keyword is used to create a class in Java?');

INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option, quiz_id)
SELECT 'Which method is the entry point of a Java application?', 'start()', 'main()', 'run()', 'init()', 'B', id
FROM quizzes
WHERE title = 'Java Basics Quiz'
  AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which method is the entry point of a Java application?');

INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option, quiz_id)
SELECT 'Which type stores true or false values?', 'int', 'String', 'boolean', 'double', 'C', id
FROM quizzes
WHERE title = 'Java Basics Quiz'
  AND NOT EXISTS (SELECT 1 FROM questions WHERE question_text = 'Which type stores true or false values?');
