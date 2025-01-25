package org.yapp.domain.block.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.domain.block.BlockContact;
import org.yapp.domain.block.application.dto.BlockContactCreateDto;
import org.yapp.domain.block.dao.BlockContactRepository;
import org.yapp.domain.user.User;

@Service
@RequiredArgsConstructor
public class BlockContactService {

    private final BlockContactRepository blockContactRepository;

    @Transactional()
    public void blockPhoneNumbers(BlockContactCreateDto blockContactCreateDto) {
        Long userId = blockContactCreateDto.userId();
        List<String> phoneNumbers = blockContactCreateDto.phoneNumbers();
        List<BlockContact> newBlockContacts = new ArrayList<>();

        Set<String> blockedPhoneNumbers = blockContactRepository.findBlocksByUserId(userId)
            .stream()
            .map(BlockContact::getPhoneNumber)
            .collect(Collectors.toSet());

        phoneNumbers.stream()
            .filter(phoneNumber -> !blockedPhoneNumbers.contains(phoneNumber))
            .forEach(phoneNumber -> {
                BlockContact blockContact = BlockContact.builder()
                    .user(User.builder().id(userId).build())
                    .phoneNumber(phoneNumber)
                    .build();
                newBlockContacts.add(blockContact);
            });

        blockContactRepository.saveAll(newBlockContacts);
    }

    @Transactional(readOnly = false)
    public List<BlockContact> findBlocksByUserId(Long userId) {
        return blockContactRepository.findBlocksByUserId(userId);
    }
}
