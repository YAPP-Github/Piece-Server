-- ProfileBasic 더미 데이터 삽입
INSERT INTO profile (profile_id, profile_status, nickname, description, birthdate, height, job,
                     location, smoking_status, weight, sns_activity_level, contacts, image_url,
                     created_at, updated_at)
VALUES (1, 'APPROVED', 'johndoe', '소프트웨어 개발자', '1990-05-10', 180, '엔지니어', '서울', '비흡연자', 75, '활동적',
        '{
          "PHONE_NUMBER": "010-1234-5678",
          "KAKAO_TALK_ID": "johndoe123"
        }',
        'https://example.com/john.jpg', NOW(), NOW()),

       (2, 'APPROVED', 'janesmith', '마케팅 매니저', '1992-08-15', 165, '마케터', '인천', '비흡연자', 55, '보통',
        '{
          "PHONE_NUMBER": "010-2345-6789",
          "INSTAGRAM_ID": "@janesmith"
        }',
        'https://example.com/jane.jpg', NOW(), NOW()),

       (3, 'APPROVED', 'robbrown', '그래픽 디자이너', '1988-03-22', 175, '디자이너', '부산', '흡연자', 70, '낮음',
        '{
          "PHONE_NUMBER": "010-3456-7890",
          "OPEN_CHAT_URL": "https://open.kakao.com/o/robdesign"
        }',
        'https://example.com/robert.jpg', NOW(), NOW()),

       (4, 'APPROVED', 'emilydavis', 'HR 전문가', '1995-09-10', 170, 'HR', '대구', '비흡연자', 60, '활동적',
        '{
          "PHONE_NUMBER": "010-4567-8901",
          "INSTAGRAM_ID": "@emilyd"
        }',
        'https://example.com/emily.jpg', NOW(), NOW()),

       (5, 'APPROVED', 'mikejohnson', '데이터 과학자', '1993-12-01', 185, '과학자', '서울', '비흡연자', 80,
        '매우 활동적', '{
         "PHONE_NUMBER": "010-5678-9012",
         "KAKAO_TALK_ID": "mikejohn123",
         "INSTAGRAM_ID": "@mike.data"
       }',
        'https://example.com/mike.jpg', NOW(), NOW()),

       (6, 'APPROVED', 'sarahwilson', '학교 교사', '1985-04-17', 160, '교사', '광주', '비흡연자', 50, '보통',
        '{
          "PHONE_NUMBER": "010-6789-0123",
          "OPEN_CHAT_URL": "https://open.kakao.com/o/sarahteach"
        }',
        'https://example.com/sarah.jpg', NOW(), NOW()),

       (7, 'APPROVED', 'davidmartinez', '프로젝트 매니저', '1991-11-11', 172, '매니저', '수원', '비흡연자', 68,
        '활동적', '{
         "PHONE_NUMBER": "010-7890-1234",
         "INSTAGRAM_ID": "@davidpm"
       }',
        'https://example.com/david.jpg', NOW(), NOW()),

       (8, 'APPROVED', 'laurawhite', '비즈니스 분석가', '1987-06-25', 167, '분석가', '대전', '비흡연자', 58, '낮음',
        '{
          "PHONE_NUMBER": "010-8901-2345",
          "KAKAO_TALK_ID": "lauraw123",
          "OPEN_CHAT_URL": "https://open.kakao.com/o/laurabiz"
        }',
        'https://example.com/laura.jpg', NOW(), NOW()),

       (9, 'APPROVED', 'jamesanderson', '기업가', '1990-01-19', 178, '기업가', '서울', '흡연자', 72, '매우 활동적',
        '{
          "PHONE_NUMBER": "010-9012-3456",
          "INSTAGRAM_ID": "@jamesbiz"
        }',
        'https://example.com/james.jpg', NOW(), NOW()),

       (10, 'APPROVED', 'oliviataylor', '제품 디자이너', '1994-07-30', 162, '디자이너', '울산', '비흡연자', 54,
        '보통', '{
         "PHONE_NUMBER": "010-0123-4567",
         "OPEN_CHAT_URL": "https://open.kakao.com/o/oliviadesign"
       }',
        'https://example.com/olivia.jpg', NOW(), NOW());


INSERT INTO user_table (user_id, oauth_id, name, phone, role, profile_id, created_at, updated_at)
VALUES (1, 'oauth_1', '홍길동', '010-1234-5678', 'USER', 1, NOW(), NOW()),
       (2, 'oauth_2', '김철수', '010-2345-6789', 'USER', 2, NOW(), NOW()),
       (3, 'oauth_3', '이영희', '010-3456-7890', 'USER', 3, NOW(), NOW()),
       (4, 'oauth_4', '박민수', '010-4567-8901', 'USER', 4, NOW(), NOW()),
       (5, 'oauth_5', '최지우', '010-5678-9012', 'USER', 5, NOW(), NOW()),
       (6, 'oauth_6', '강하늘', '010-6789-0123', 'USER', 6, NOW(), NOW()),
       (7, 'oauth_7', '오세훈', '010-7890-1234', 'USER', 7, NOW(), NOW()),
       (8, 'oauth_8', '장미란', '010-8901-2345', 'USER', 8, NOW(), NOW()),
       (9, 'oauth_9', '정다은', '010-9012-3456', 'USER', 9, NOW(), NOW()),
       (10, 'oauth_10', '김유진', '010-0123-4567', 'USER', 10, NOW(), NOW());

INSERT INTO report (report_id, reporter_user_id, reported_user_id, reason, created_at, updated_at)
VALUES (1, 1, 2, '부적절한 언어 사용', NOW(), NOW()),
       (2, 3, 4, '스팸 메시지 전송', NOW(), NOW()),
       (3, 5, 6, '허위 정보 제공', NOW(), NOW()),
       (4, 7, 8, '욕설 및 비방', NOW(), NOW()),
       (5, 9, 10, '기타 사유', NOW(), NOW()),
       (6, 2, 1, '사기 의심', NOW(), NOW()),
       (7, 4, 3, '허위 정보 제공', NOW(), NOW()),
       (8, 6, 5, '욕설 및 비방', NOW(), NOW()),
       (9, 8, 7, '부적절한 언어 사용', NOW(), NOW()),
       (10, 10, 9, '스팸 메시지 전송', NOW(), NOW());

INSERT INTO report (report_id, reporter_user_id, reported_user_id, reason, created_at, updated_at)
VALUES (11, 3, 2, '부적절한 언어 사용 2222', NOW(), NOW());


INSERT INTO report (report_id, reporter_user_id, reported_user_id, reason, created_at, updated_at)
VALUES (12, 1, 2, '부적절한 언어 사용 3333', NOW(), NOW());

INSERT INTO user_block (user_block_id, blocking_user_id, blocked_user_id, created_at, updated_at)
VALUES (1, 1, 2, NOW(), NOW()),
       (2, 2, 3, NOW(), NOW()),
       (3, 3, 4, NOW(), NOW()),
       (4, 4, 1, NOW(), NOW()),
       (5, 1, 3, NOW(), NOW());

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

INSERT INTO value_talk (category, title, is_active, placeholder, guides)
VALUES ('꿈과 목표', '어떤 일을 하며 무엇을 목표로 살아가나요? 인생에서 이루고 싶은 꿈은 무엇인가요?', true,
        '단순한 목표보다 당신의 가치관과 삶의 방향을 드러낼 수 있는 이야기를 공유하면 더 인상 깊을 거예요.',
        '[
          "당신의 직업은 무엇인가요?",
          "앞으로 하고 싶은 일에 대해 이야기해주세요",
          "어떤 일을 할 때 가장 큰 성취감을 느끼나요?",
          "당신의 버킷리스트를 알려주세요",
          "당신이 꿈꾸는 삶은 어떤 모습인가요?"
        ]'),

       ('관심사와 취향', '무엇을 할 때 가장 행복한가요? 요즘 어떠한 것에 관심을 두고 있나요?', true,
        '진솔하고 자신감 있게 일상의 즐거움을 이야기 해주세요. 자연스럽고 친근한 매력이 전달될 거예요.',
        '[
          "당신의 삶을 즐겁게 만드는 것들은 무엇인가요?",
          "일상에서 소소한 행복을 느끼는 순간을 적어보세요",
          "최근에 몰입했던 취미가 있다면 소개해 주세요",
          "최근 마음이 따뜻해졌던 순간을 들려주세요",
          "요즘 마음을 사로잡은 콘텐츠를 공유해 보세요"
        ]'),

       ('연애관', '어떠한 사람과 어떠한 연애를 하고 싶은지 들려주세요', true,
        '연애에서 중요하게 생각하는 가치와 관계의 모습을 구체적으로 적어보세요. 따뜻하고 진실된 태도는 큰 매력이 됩니다.', '[
         "함께 하고 싶은 데이트 스타일은 무엇인가요?",
         "이상적인 관계의 모습을 적어 보세요",
         "연인과 함께 만들고 싶은 추억이 있나요?",
         "연애에서 가장 중요시하는 가치는 무엇인가요?",
         "연인 관계를 통해 어떤 가치를 얻고 싶나요?"
       ]');

INSERT INTO profile_value_pick (profile_value_pick_id, profile_id, value_pick_id, selected_answer)
VALUES (1, 1, 1, 2),
       (2, 1, 2, 1);

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