package com.example.fpi.domain.vo.certify;

import com.example.fpi.domain.dto.certify.CardInfoFileDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardInfoFileVO {
    private Long cardInfoFileId;
    private String cardInfoFileRoute;
    private String cardInfoFileOriginal;
    private Long cardInfoId;

    @Builder
    public CardInfoFileVO(Long cardInfoFileId, String cardInfoFileRoute, String cardInfoFileOriginal, Long cardInfoId) {
        this.cardInfoFileId = cardInfoFileId;
        this.cardInfoFileRoute = cardInfoFileRoute;
        this.cardInfoFileOriginal = cardInfoFileOriginal;
        this.cardInfoId = cardInfoId;
    }

    public static CardInfoFileVO toEntity(CardInfoFileDTO dto) {
        return CardInfoFileVO.builder()
                .cardInfoFileId(dto.getCardInfoFileId())
                .cardInfoFileRoute(dto.getCardInfoFileRoute())
                .cardInfoFileOriginal(dto.getCardInfoFileOriginal())
                .cardInfoId(dto.getCardInfoId())
                .build();
    }
}
