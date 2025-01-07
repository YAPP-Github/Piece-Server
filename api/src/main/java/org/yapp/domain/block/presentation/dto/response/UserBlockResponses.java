package org.yapp.domain.block.presentation.dto.response;

import org.yapp.domain.block.Block;

import java.util.List;

public record UserBlockResponses (Long userId, List<BlockResponse> blockResponses) {
    public static UserBlockResponses from (Long userId, List<Block> blocks) {
        List<BlockResponse> blockResponses = blocks.stream().map(block -> new BlockResponse(block.getPhoneNumber(), block.getCreatedAt())).toList();

        return new UserBlockResponses(
                userId,
                blockResponses
        );
    }

}
