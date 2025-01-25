-- ProfileBasic 더미 데이터 삽입
INSERT INTO profile (nickname, birthdate, height, job, location, smoking_status,
                     religion,
                     sns_activity_level,
                     image_url, contacts, profile_status)
VALUES ('JohnDoe', '1990-01-01', 180, 'Engineer', 'Seoul', 'Non-smoker', 'None', 'Medium',
        'https://example.com/johndoe.jpg', '{
    "email": "nomad8@naver.com"
  }');


-- User 더미 데이터 삽입
INSERT INTO user_table (oauth_id, name, phone, role, profile_id, created_at, updated_at)
VALUES ('oauth12345', 'Alice', '010-1234-5678', 'ADMIN', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('oauth67890', 'Bob', '010-5678-1234', 'USER', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('oauth13579', 'Charlie', '010-1111-2222', 'USER', NULL, CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('oauth24680', 'David', '010-3333-4444', 'USER', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('oauth11223', 'Eve', '010-5555-6666', 'USER', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ValueItem 더미 데이터 삽입
INSERT INTO value_pick (value_pick_id, category, question, answers, is_active)
VALUES (1, '음주', '연인과 함께 술을 마시는 것을 좋아하나요?', '{"1": "함께 술을 즐기고 싶어요", "2": "같이 술을 즐길 수 없어도 괜찮아요"}',
        true),
       (2, '만남 빈도', '주말에 얼마나 자주 데이트를 하고 싶나요?',
        '{"1": "주말에는 최대한 같이 있고 싶어요", "2": "하루 정도는 각자 보내고 싶어요"}', false),
       (3, '연락 빈도', '연인 사이에 얼마나 자주 연락하는게 좋나요?',
        '{"1": "바쁘더라도 자주 연락하고 싶어요", "2": "연락은 생각날 때만 종종 해도 괜찮아요"}', true),
       (4, '연락 방식', '연락할 때 어떤 방법을 더 좋아하나요?',
        '{"1": "전화보다는 문자나 카톡이 더 좋아요", "2": "문자나 카톡보다는 전화가 더 좋아요"}', true);

INSERT INTO value_talk (category, title, is_active)
VALUES ('꿈과 목표', '어떤 일을 하며 무엇을 목표로 살아가나요? 인생에서 이루고 싶은 꿈은 무엇인가요?', true),

       ('관심사와 취향', '무엇을 할 때 가장 행복한가요? 요즘 어떠한 것에 관심을 두고 있나요?', true),

       ('연애관', '어떠한 사람과 어떠한 연애를 하고 싶은지 들려주세요', true);

INSERT INTO profile_value_pick (profile_value_pick_id, profile_id, value_pick_id, selected_answer)
VALUES (1, 1, 1, 2),
       (2, 1, 2, 3);

INSERT INTO profile_value_talk (profile_value_talk_id, profile_id, value_talk_id, answer)
VALUES (1, 1, 1, '문장입니다 1'),
       (2, 1, 2, '문장입니다 2');

INSERT INTO term (version, title, content, required, start_date, is_active)
VALUES ('1.0', '서비스 이용약관', 'https://github.com/YAPP-Github', true, '2024-01-01 00:00:00', true),
       ('1.0', '개인정보 처리방침',
        'https://velog.io/@jeong_hun_hui/MySQL%EC%97%90%EC%84%9C-Explain%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EC%8B%A4%ED%96%89-%EA%B3%84%ED%9A%8D-%EB%B6%84%EC%84%9D%ED%95%98%EA%B8%B0',
        true, '2024-01-01 00:00:00', true),
       ('1.0', '위치 정보 이용약관', 'https://www.naver.com', false, '2024-01-01 00:00:00', true),
       ('1.1', '서비스 이용약관', 'https://www.google.com', true, '2024-06-01 00:00:00', true),
       ('1.1', '광고 수신 동의', 'https://blog.naver.com/dksmt/223694486454', false,
        '2024-06-01 00:00:00', false)