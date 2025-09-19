INSERT INTO categories(id, name, eng_name, parent_id)
VALUES (1, '맛집', 'eats', null),
       (2, '카페', 'cafe', 1);

INSERT INTO places (id, name, latitude, longitude, address1, address2, address3, category_id)
VALUES (1, '테스트장소1', 36.0, 126.0, '주소1', '주소2', '주소3', 1),
       (2, '테스트장소2', 36.1, 126.1, '주소1', '주소2', '주소3', 2);

INSERT INTO users(id, nickname, username, role, user_type)
VALUES (1, 'user1', 'user1@mail', 'USER', 'KAKAO'),
       (2, 'user2', 'user2@mail', 'USER', 'KAKAO'),
       (3, 'user3', 'user3@mail', 'USER', 'KAKAO'),
       (4, 'user4', 'user4@mail', 'USER', 'KAKAO'),
       (5, 'user5', 'user5@mail', 'USER', 'KAKAO');

INSERT INTO reviews (id, place_id, user_id, is_liked, comment)
VALUES (1, 1, 1, true, '1->1 like'),
       (2, 1, 2, true, '2->1 like'),
       (3, 1, 3, false, '3->1 dislike'),
       (4, 1, 4, false, '4->1 dislike'),
       (5, 1, 5, false, '5->1 dislike'),
       (6, 2, 1, true, '1->2 like'),
       (7, 2, 2, true, '2->2 like'),
       (8, 2, 3, true, '3->2 like'),
       (9, 2, 4, true, '4->2 like'),
       (10, 2, 5, true, '5->2 like');

INSERT INTO influencers (id, name, job, channel_id, img_url)
VALUES (1, '인플루언서1', '직업1', 'channel1', 'img1'),
       (2, '인플루언서2', '직업2', 'channel2', 'img2'),
       (3, '인플루언서3', '직업3', 'channel3', 'img3'),
       (4, '인플루언서4', '직업4', 'channel4', 'img4');

INSERT INTO videos (id, influencer_id, place_id, uuid, view_count_increase, publish_time)
VALUES (1, 1, 1, 'Video1', 1, '2025-02-06 12:00:01.000000'),
       (2, 1, 2, 'Video2', 2, '2025-02-06 12:01:01.000000'),
       (3, 1, 3, 'Video3', 3, '2025-02-06 12:02:01.000000'),
       (4, 1, 4, 'Video4', 4, '2025-02-06 12:03:01.000000'),
       (5, 2, 5, 'Video5', 5, '2025-02-06 12:04:01.000000'),
       (6, 2, 6, 'Video6', 6, '2025-02-06 12:05:01.000000'),
       (7, 2, 7, 'Video7', 7, '2025-02-06 12:06:01.000000'),
       (8, 2, 8, 'Video8', 8, '2025-02-06 12:07:01.000000'),
       (9, 3, 9, 'Video9', 9, '2025-02-06 12:08:01.000000'),
       (10, 3, 10, 'Video10', 10, '2025-02-06 12:09:01.000000'),
       (11, 3, 11, 'Video11', 11, '2025-02-06 12:10:01.000000'),
       (12, 3, 12, 'Video12', 12, '2025-02-06 12:11:01.000000'),
       (13, 4, 13, 'Video13', 13, '2025-02-06 12:12:01.000000'),
       (14, 4, 14, 'Video14', 14, '2025-02-06 12:13:01.000000'),
       (15, 4, 15, 'Video15', 15, '2025-02-06 12:14:01.000000'),
       (16, 4, 16, 'Video16', 16, '2025-02-06 12:15:01.000000'),
       (17, 5, 17, 'Video17', 17, '2025-02-06 12:16:01.000000'),
       (18, 5, 18, 'Video18', 18, '2025-02-06 12:17:01.000000'),
       (19, 5, 19, 'Video19', 19, '2025-02-06 12:18:01.000000'),
       (20, 5, 20, 'Video20', 20, '2025-02-06 12:19:01.000000'),
       (21, 5, null, 'Video21', 16, '2025-02-06 12:20:01.000000'),
       (22, 5, null, 'Video22', 17, '2025-02-06 12:21:01.000000'),
       (23, 5, null, 'Video23', 18, '2025-02-06 12:22:01.000000'),
       (24, 5, null, 'Video24', 19, '2025-02-06 12:23:01.000000'),
       (25, 5, null, 'Video25', 20, '2025-02-06 12:24:01.000000');
