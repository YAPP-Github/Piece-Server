package org.yapp.domain.block.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yapp.domain.block.Block;
import org.yapp.domain.block.application.dto.BlockCreateDto;
import org.yapp.domain.block.dao.BlockRepository;
import org.yapp.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockService {
    private final BlockRepository blockRepository;

    @Transactional()
    public void blockPhoneNumbers(BlockCreateDto blockCreateDto) {
        Long userId = blockCreateDto.userId();
        List<String> phoneNumbers = blockCreateDto.phoneNumbers();
        List<Block> newBlocks = new ArrayList<>();

        Set<String> blockedPhoneNumbers = blockRepository.findBlocksByUserId(userId)
                .stream()
                .map(Block::getPhoneNumber)
                .collect(Collectors.toSet());

        phoneNumbers.stream()
                .filter(phoneNumber -> !blockedPhoneNumbers.contains(phoneNumber))
                .forEach(phoneNumber -> {
                    Block block = Block.builder()
                            .user(User.builder().id(userId).build())
                            .phoneNumber(phoneNumber)
                            .build();
                    newBlocks.add(block);
                });

        blockRepository.saveAll(newBlocks);
    }

    @Transactional(readOnly = false)
    public List<Block> findBlocksByUserId(Long userId) {
        return blockRepository.findBlocksByUserId(userId);
    }
}
