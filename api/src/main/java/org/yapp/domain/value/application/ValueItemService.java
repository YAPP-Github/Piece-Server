package org.yapp.domain.value.application;

import org.springframework.stereotype.Service;
import org.yapp.domain.value.ValueItem;
import org.yapp.domain.value.dao.ValueItemRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValueItemService {
  private final ValueItemRepository valueItemRepository;

  public List<ValueItem> getAllValueItems() {
    return valueItemRepository.findAll();
  }
}
