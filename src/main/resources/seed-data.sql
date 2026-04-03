-- ============================================================
-- Seed Data for AnonConfess - confession_form database
-- 10 Users + 30 Posts
-- ============================================================
-- Password Reference (BCrypt encoded below):
--   User 1  (alex_walker)       : Alpha@123
--   User 2  (sarah_chen)        : Bravo@123
--   User 3  (james_silva)       : Charlie@123
--   User 4  (priya_patel)       : Delta@123
--   User 5  (marcus_jones)      : Echo@123
--   User 6  (emma_larsson)      : Foxtrot@123
--   User 7  (ryan_murphy)       : Golf@123
--   User 8  (nina_kowalski)     : Hotel@123
--   User 9  (david_tanaka)      : India@123
--   User 10 (olivia_fernandez)  : Juliet@123
-- ============================================================

USE confession_form;

-- ---------- USERS ----------

INSERT INTO users (username, fname, lname, email, mobile_no, password, role, is_active, created_at, updated_at)
VALUES
('alex_walker',      'Alex',    'Walker',    'alex.walker@uom.lk',      '0771234501', '$2b$10$Wv.Vn/kI7zqmewRqoBz.9uKDCPrDfKPQ2JLmMn1mpEiyVG6pfCh7a', 'USER',  1, NOW(), NOW()),
('sarah_chen',       'Sarah',   'Chen',      'sarah.chen@uom.lk',       '0771234502', '$2b$10$Whs0D60rBolXTkRxpkhPP.D3P5eUpmHa7NQhh5/711sYB2./ygDnC', 'USER',  1, NOW(), NOW()),
('james_silva',      'James',   'Silva',     'james.silva@uom.lk',      '0771234503', '$2b$10$ppTFwSypPtXvSHphg6PjuOnwhfGsMbr8gNugtUc7PQIsao.vkTlja', 'USER',  1, NOW(), NOW()),
('priya_patel',      'Priya',   'Patel',     'priya.patel@uom.lk',      '0771234504', '$2b$10$UmsyvKHxmc.a3jc9s3cRZ.yPGR8Dd823zSIvYmqecQvyjPtZ86McO', 'USER',  1, NOW(), NOW()),
('marcus_jones',     'Marcus',  'Jones',     'marcus.jones@uom.lk',     '0771234505', '$2b$10$fwOc5I6Bj.HHHgOaQC9.ouVUjZO.0s1bUdwN5CzOALC0fie3CFrpa', 'USER',  1, NOW(), NOW()),
('emma_larsson',     'Emma',    'Larsson',   'emma.larsson@uom.lk',     '0771234506', '$2b$10$qHXzlTJb4qpQPy2E3ZVA2uO8CU1ZBS4Swj.yOrfGdsYkaFuCvLSuW', 'USER',  1, NOW(), NOW()),
('ryan_murphy',      'Ryan',    'Murphy',    'ryan.murphy@uom.lk',      '0771234507', '$2b$10$v0XAgeWyFUS242cazYsyBO.CT4caeuzllk4cKmMIzT/8mVODw4xG2', 'USER',  1, NOW(), NOW()),
('nina_kowalski',    'Nina',    'Kowalski',  'nina.kowalski@uom.lk',    '0771234508', '$2b$10$Uaa4yaXn4A2rfJ0yfOER/urTzpWHCnprN.YED2Ct.H2jl2ZK3JhnW', 'USER',  1, NOW(), NOW()),
('david_tanaka',     'David',   'Tanaka',    'david.tanaka@uom.lk',     '0771234509', '$2b$10$lxSqiYxziePSPfP9.xFvw.1WAjr2E9j7Z.UHxD0OzG1RsDyA5qemm', 'USER',  1, NOW(), NOW()),
('olivia_fernandez', 'Olivia',  'Fernandez', 'olivia.fernandez@uom.lk', '0771234510', '$2b$10$d5HSqTYSd7UP72CY.ys1d./cEe6XJ6X.ACE/G7YJyjbrXO8w1t0tS', 'ADMIN', 1, NOW(), NOW());

-- ---------- POSTS (30 total) ----------
-- Distribution: alex(5), sarah(2), james(4), priya(1), marcus(3),
--               emma(2), ryan(4), nina(3), david(1), olivia(5)

-- Alex Walker — 5 posts
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'alex_walker'), 'I finally passed the Data Structures exam after failing it twice. I never told anyone about those two failures. It feels so good to finally get through it.', 'HAPPY', 'ACADEMIC', 'PUBLIC', '2026-03-01 08:15:00', '2026-03-01 08:15:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'alex_walker'), 'I pretend to be fine in group projects but I actually have no idea what is going on half the time. Anyone else feel this way?', 'CONFUSED', 'ACADEMIC', 'ANONYMOUS', '2026-03-05 14:30:00', '2026-03-05 14:30:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'alex_walker'), 'The sunset from the library rooftop yesterday was absolutely stunning. Sometimes you just need to stop and breathe.', 'PEACEFUL', 'CAMPUS_LIFE', 'PUBLIC', '2026-03-10 18:45:00', '2026-03-10 18:45:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'alex_walker'), 'I started journaling every night and it has changed my life. Highly recommend it to anyone feeling overwhelmed.', 'GRATEFUL', 'PERSONAL_GROWTH', 'PUBLIC', '2026-03-15 21:00:00', '2026-03-15 21:00:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'alex_walker'), 'Does anyone else eat lunch alone and actually enjoy it? I love my solo cafeteria time honestly.', 'RELAXED', 'SOCIAL', 'STUDENT_ONLY', '2026-03-20 12:30:00', '2026-03-20 12:30:00');

-- Sarah Chen — 2 posts
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'sarah_chen'), 'I switched my major from Engineering to Psychology and my parents still do not know. I am terrified of telling them but I have never been happier.', 'ANXIOUS', 'PERSONAL_GROWTH', 'ANONYMOUS', '2026-03-02 09:00:00', '2026-03-02 09:00:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'sarah_chen'), 'Shoutout to the librarian who always saves me the quiet corner desk. You are the real MVP.', 'GRATEFUL', 'CAMPUS_LIFE', 'PUBLIC', '2026-03-18 10:20:00', '2026-03-18 10:20:00');

-- James Silva — 4 posts
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'james_silva'), 'I have been coding for 12 hours straight and I just realized my bug was a missing semicolon. I want to scream.', 'FRUSTRATED', 'ACADEMIC', 'PUBLIC', '2026-03-03 02:00:00', '2026-03-03 02:00:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'james_silva'), 'The new coffee shop near campus makes the best iced latte. If you have not tried it yet you are missing out.', 'EXCITED', 'RECOMMENDATIONS', 'PUBLIC', '2026-03-08 15:00:00', '2026-03-08 15:00:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'james_silva'), 'I got my first internship offer today! All those late nights working on projects finally paid off.', 'EXCITED', 'CAREER', 'PUBLIC', '2026-03-14 16:30:00', '2026-03-14 16:30:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'james_silva'), 'Confession: I watch lecture recordings at 2x speed and still barely understand anything. The struggle is real.', 'SILLY', 'HUMOR', 'STUDENT_ONLY', '2026-03-22 20:00:00', '2026-03-22 20:00:00');

-- Priya Patel — 1 post
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'priya_patel'), 'Moving to a new country for university was the hardest thing I have ever done. Some days I miss home so much it physically hurts. But I know this is where I need to be.', 'NOSTALGIC', 'PERSONAL_GROWTH', 'ANONYMOUS', '2026-03-04 22:15:00', '2026-03-04 22:15:00');

-- Marcus Jones — 3 posts
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'marcus_jones'), 'I have been volunteering at the animal shelter every weekend and it honestly keeps me sane during exam season.', 'HAPPY', 'WELLNESS', 'PUBLIC', '2026-03-06 11:00:00', '2026-03-06 11:00:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'marcus_jones'), 'Unpopular opinion: 8 AM lectures are actually not that bad if you go to bed at a reasonable time.', 'ENERGETIC', 'HUMOR', 'PUBLIC', '2026-03-12 08:30:00', '2026-03-12 08:30:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'marcus_jones'), 'Got into a heated debate in philosophy class today and accidentally made the professor speechless. Best moment of my semester.', 'PLAYFUL', 'ACADEMIC', 'STUDENT_ONLY', '2026-03-25 14:00:00', '2026-03-25 14:00:00');

-- Emma Larsson — 2 posts
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'emma_larsson'), 'I have social anxiety and forcing myself to join a club this semester was terrifying. But I have made three genuine friends already. Growth is uncomfortable but worth it.', 'HOPEFUL', 'SOCIAL', 'ANONYMOUS', '2026-03-07 19:00:00', '2026-03-07 19:00:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'emma_larsson'), 'The meditation room in the wellness center is an absolute hidden gem. Please do not let it get too crowded though.', 'CALM', 'WELLNESS', 'STUDENT_ONLY', '2026-03-19 07:30:00', '2026-03-19 07:30:00');

-- Ryan Murphy — 4 posts
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'ryan_murphy'), 'I accidentally called my professor "mom" in front of the entire lecture hall. I am transferring universities immediately.', 'SILLY', 'HUMOR', 'PUBLIC', '2026-03-09 10:45:00', '2026-03-09 10:45:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'ryan_murphy'), 'Started going to the gym at 6 AM and the campus is so peaceful at that hour. It is like a completely different world.', 'PEACEFUL', 'WELLNESS', 'PUBLIC', '2026-03-13 06:30:00', '2026-03-13 06:30:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'ryan_murphy'), 'I thought I wanted to be a lawyer my whole life. After one internship I realized I actually want to teach. Still processing that.', 'CONFUSED', 'CAREER', 'ANONYMOUS', '2026-03-17 23:00:00', '2026-03-17 23:00:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'ryan_murphy'), 'If you are reading this and feeling burnt out just know you are not alone. Take a break. Your degree will still be there tomorrow.', 'INSPIRED', 'GENERAL', 'PUBLIC', '2026-03-28 15:30:00', '2026-03-28 15:30:00');

-- Nina Kowalski — 3 posts
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'nina_kowalski'), 'I wrote an entire essay the night before it was due and got the highest mark in class. I do not know if I should feel proud or concerned.', 'SURPRISED', 'ACADEMIC', 'STUDENT_ONLY', '2026-03-11 04:00:00', '2026-03-11 04:00:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'nina_kowalski'), 'The friendships you make during late night study sessions hit different. These are my people.', 'LOVED', 'SOCIAL', 'PUBLIC', '2026-03-16 01:30:00', '2026-03-16 01:30:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'nina_kowalski'), 'I have been learning guitar in secret and I am going to perform at the open mic night next month. Absolutely terrified but excited.', 'ANXIOUS', 'ACHIEVEMENTS', 'PUBLIC', '2026-03-24 20:00:00', '2026-03-24 20:00:00');

-- David Tanaka — 1 post
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'david_tanaka'), 'I come from a small town and being at a big university sometimes makes me feel invisible. But I remind myself that I earned my place here just like everyone else.', 'LONELY', 'PERSONAL_GROWTH', 'ANONYMOUS', '2026-03-21 23:45:00', '2026-03-21 23:45:00');

-- Olivia Fernandez — 5 posts
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'olivia_fernandez'), 'I organized a charity bake sale on campus and we raised over 50000 LKR for the children hospital. So proud of everyone who contributed!', 'GRATEFUL', 'ACHIEVEMENTS', 'PUBLIC', '2026-03-02 17:00:00', '2026-03-02 17:00:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'olivia_fernandez'), 'Hot take: the campus canteen rice and curry is actually underrated. Fight me.', 'PLAYFUL', 'HUMOR', 'PUBLIC', '2026-03-09 13:00:00', '2026-03-09 13:00:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'olivia_fernandez'), 'I failed my driving test three times before passing. Some things just take longer and that is okay.', 'HOPEFUL', 'GENERAL', 'PUBLIC', '2026-03-15 09:00:00', '2026-03-15 09:00:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'olivia_fernandez'), 'The career guidance office helped me rewrite my CV and I have already gotten two interview calls. Seriously underrated resource on campus.', 'PRODUCTIVE', 'RECOMMENDATIONS', 'STUDENT_ONLY', '2026-03-23 11:00:00', '2026-03-23 11:00:00');
INSERT INTO post (user_id, content, mood, category, visibility_level, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'olivia_fernandez'), 'To the person who returned my laptop that I left in the lecture hall — thank you. You restored my faith in humanity.', 'GRATEFUL', 'CAMPUS_LIFE', 'PUBLIC', '2026-03-30 16:00:00', '2026-03-30 16:00:00');
