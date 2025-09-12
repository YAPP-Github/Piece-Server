package org.yapp.core.similarity.preprocessing.tokenizer;

import java.util.ArrayList;
import java.util.List;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.extern.slf4j.Slf4j;

/**
 * KOMORAN 기반 형태소 토크나이저 한국어 형태소 분석을 통해 의미있는 형태소를 토큰으로 추출합니다.
 */
@Slf4j
public class KomoranTokenizer implements SentenceTokenizer {

    private static final Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

    @Override
    public List<String> tokenize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<String> tokens = new ArrayList<>();

        try {
            List<Token> morphemes = komoran.analyze(text).getTokenList();

            for (Token token : morphemes) {
                String pos = token.getPos();
                String morph = token.getMorph();

                // 명사(NN*), 동사(VV), 형용사(VA), 부사(MAG) 등 의미있는 형태소만 추출
                if (pos.startsWith("NN") || pos.startsWith("VV") || pos.startsWith("VA")
                    || pos.equals("MAG")) {
                    tokens.add(morph.toLowerCase());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to analyze morphemes for text: {}", text, e);
        }

        return tokens;
    }

    @Override
    public String getType() {
        return "KOMORAN";
    }
}