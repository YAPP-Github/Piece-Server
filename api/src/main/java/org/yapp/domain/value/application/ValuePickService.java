package org.yapp.domain.value.application;

import org.springframework.stereotype.Service;
import org.yapp.domain.value.ValuePick;
import org.yapp.domain.value.dao.ValuePickRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValuePickService {
  private final ValuePickRepository valuePickRepository;

  public List<ValuePick> getAllValueItems() {
    return valuePickRepository.findAll();
  }
}
