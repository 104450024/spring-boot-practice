package com.example.final1.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.final1.demo.domain.KillVoucherRepository;

@Service
public class KillVoucherService {
  @Autowired
  private final KillVoucherRepository killVoucherRepository;

  public KillVoucherService(KillVoucherRepository killVoucherRepository) {
    this.killVoucherRepository = killVoucherRepository;
  }

}
