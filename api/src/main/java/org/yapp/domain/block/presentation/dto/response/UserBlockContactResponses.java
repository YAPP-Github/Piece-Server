package org.yapp.domain.block.presentation.dto.response;

import java.util.List;
import org.yapp.core.domain.block.BlockContact;

public record UserBlockContactResponses(Long userId,
                                        List<BlockContactResponse> blockContactResponses) {

    public static UserBlockContactResponses from(Long userId, List<BlockContact> blockContacts) {
        List<BlockContactResponse> blockContactResponses = blockContacts.stream().map(
            blockContact -> new BlockContactResponse(blockContact.getPhoneNumber(),
                blockContact.getCreatedAt())).toList();

        return new UserBlockContactResponses(
            userId,
            blockContactResponses
        );
    }

}
