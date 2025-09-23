package org.yapp.domain.block.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.core.domain.block.BlockContact;
import org.yapp.core.domain.user.User;
import org.yapp.domain.block.application.dto.BlockContactCreateDto;
import org.yapp.domain.block.dao.BlockContactRepository;
import org.yapp.domain.setting.application.BlockContactSyncTimeService;
import org.yapp.global.util.PhoneNumberDecoder;

@Service
@RequiredArgsConstructor
public class BlockContactService {

    private final BlockContactRepository blockContactRepository;
    private final BlockContactSyncTimeService blockContactSyncTimeService;
    private final BloomBlockService bloomBlockService;

    @Transactional()
    public void blockPhoneNumbers(BlockContactCreateDto blockContactCreateDto) {
        Long userId = blockContactCreateDto.userId();
        List<String> phoneNumberList = blockContactCreateDto.phoneNumbers();

        // 핸드폰 번호 디코딩 처리 (iOS Base64 인코딩 대응)
        Set<String> decodedPhoneNumbers = phoneNumberList.stream()
            .map(PhoneNumberDecoder::decodeIfBase64)
            .collect(Collectors.toSet());

        List<BlockContact> newBlockContacts = new ArrayList<>();
        List<String> newPhoneNumbers = new ArrayList<>();

        Set<String> blockedPhoneNumbers = blockContactRepository.findBlocksByUserId(userId)
            .stream()
            .map(BlockContact::getPhoneNumber)
            .collect(Collectors.toSet());

        decodedPhoneNumbers.stream()
            .filter(phoneNumber -> !blockedPhoneNumbers.contains(phoneNumber))
            .forEach(phoneNumber -> {
                BlockContact blockContact = BlockContact.builder()
                    .user(User.builder().id(userId).build())
                    .phoneNumber(phoneNumber)
                    .build();
                newPhoneNumbers.add(phoneNumber);
                newBlockContacts.add(blockContact);
            });

        bloomBlockService.blockContactList(userId, newPhoneNumbers);
        blockContactSyncTimeService.saveBlockContactSyncTime(userId, LocalDateTime.now());
        blockContactRepository.saveAll(newBlockContacts);
    }

    @Transactional(readOnly = true)
    public List<BlockContact> findBlocksByUserId(Long userId) {
        return blockContactRepository.findBlocksByUserId(userId);
    }

    public boolean checkIfUserBlockedPhoneNumber(Long userId, String phoneNumber) {
        // 조회 시에도 디코딩 처리
        String decodedPhoneNumber = PhoneNumberDecoder.decodeIfBase64(phoneNumber);
        return blockContactRepository.existsByUser_IdAndPhoneNumber(userId, decodedPhoneNumber);
    }
}
