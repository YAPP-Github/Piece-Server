package org.yapp.domain.profile.application.util;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NicknameGenerator {

    private static final List<String> ADJECTIVES = Arrays.asList(
        "멋진", "귀여운", "용감한", "지혜로운", "빠른", "상냥한", "친절한", "재밌는", "활기찬", "화려한",
        "행복한", "웃는", "훌륭한", "든든한", "기쁜", "강한", "사랑스러운", "정직한", "부지런한", "섬세한",
        "따뜻한", "창의적인", "똑똑한", "현명한", "빛나는", "청량한", "우아한", "온화한", "달콤한", "튼튼한",
        "대단한", "예쁜", "화끈한", "소중한", "차분한", "참신한", "열정적인", "훤칠한", "침착한", "깔끔한",
        "친근한", "믿음직한", "매력적인", "유쾌한", "겸손한", "청순한", "독창적인", "유능한", "고운", "소박한",
        "환상적인", "쾌활한", "매서운", "달변가인", "상냥한", "포근한", "용감무쌍한", "친근한", "솔직한", "정중한",
        "꼼꼼한", "도도한", "풍요로운", "젊은", "민첩한", "포근한", "예리한", "다정한", "아름다운", "반짝이는",
        "풍부한", "깊은", "깨끗한", "참을성 있는", "충성스러운", "긍정적인", "믿음직한", "지적인", "세련된", "풍요로운",
        "활발한", "침착한", "창의적인", "섬세한", "빛나는", "예의 바른", "강렬한", "평온한", "낙천적인", "부드러운"
    );

    private static final List<String> NOUNS = Arrays.asList(
        "고양이", "강아지", "사자", "호랑이", "늑대", "여우", "곰", "판다", "원숭이", "코끼리",
        "토끼", "새", "나비", "거북이", "고래", "돌고래", "공룡", "말", "쥐", "햄스터",
        "사슴", "부엉이", "독수리", "돼지", "양", "닭", "오리", "거위", "표범", "치타",
        "기린", "캥거루", "고슴도치", "여치", "장수풍뎅이", "매미", "벌", "무당벌레", "달팽이", "메뚜기",
        "낙타", "참새", "두루미", "수달", "하마", "코뿔소", "이구아나", "도마뱀", "청개구리", "뱀",
        "펭귄", "너구리", "담비", "족제비", "스컹크", "오소리", "올빼미", "까마귀", "까치", "앵무새",
        "나무늘보", "라쿤", "판골", "개미", "개미핥기", "두더지", "미어캣", "고라니", "청솔모", "도롱뇽",
        "여우원숭이", "캥거루쥐", "작은새", "큰까치", "대머리독수리", "아르마딜로", "알락꼬리원숭이", "불독", "치와와", "말티즈",
        "코카스패니얼", "시바견", "도베르만", "말라뮤트", "불곰", "바다사자", "물개", "불가사리", "해파리", "성게"
    );

    public static String generate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String adjective = ADJECTIVES.get(random.nextInt(ADJECTIVES.size()));
        String noun = NOUNS.get(random.nextInt(NOUNS.size()));

        return adjective + noun;
    }
}