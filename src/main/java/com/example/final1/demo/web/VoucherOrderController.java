package com.example.final1.demo.web;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.final1.demo.domain.BookRepository;
import com.example.final1.demo.domain.KillVoucher;
import com.example.final1.demo.domain.KillVoucherRepository;
import com.example.final1.demo.domain.VoucherOrder;
import com.example.final1.demo.domain.VoucherOrderRepository;

@EnableAspectJAutoProxy(exposeProxy = true)
@RestController
@RequestMapping("/voucher-order")
public class VoucherOrderController {

  @Resource
  private BookRepository bookRepository;
  private KillVoucherRepository killVoucherRepository;
  private VoucherOrderRepository voucherOrderRepository;

  @Autowired
  public VoucherOrderController(BookRepository bookRepository, KillVoucherRepository killVoucherRepository,
      VoucherOrderRepository voucherOrderRepository) {
    this.bookRepository = bookRepository;
    this.killVoucherRepository = killVoucherRepository;
    this.voucherOrderRepository = voucherOrderRepository;
  }

  @PostMapping("/seckill/{id}")
  @Transactional
  public String seckillVoucher(@PathVariable("id") int voucherId) {

    String VoucherINFO = seckillVoucherINFO(voucherId);

    return VoucherINFO;
  }

  // @GetMapping("/test/{id}")
  // public Integer getMethodName(@PathVariable("id") int id) {

  // KillVoucher userByStatusAndNameNamedParamsNative = killVoucherRepository
  // .findUserByStatusAndNameNamedParamsNative(1);
  // return userByStatusAndNameNamedParamsNative.getStock();
  // }

  public String seckillVoucherINFO(int voucherId) {

    // 1.查詢優惠券
    KillVoucher voucher = killVoucherRepository.findVKillVoucherID(voucherId);

    // 2.判斷秒殺是否開始

    // if (voucher.getBeginTime().isAfter(LocalDateTime.now())) {
    // // 尚未開始
    // return "秒殺尚未開始";
    // }
    // // 3.判斷秒殺是否結束
    // if (voucher.getEndTime().isBefore(LocalDateTime.now())) {
    // return "秒殺尚未開始";
    // }

    // 4.判斷庫存是否充足
    if (voucher.getStock() < 1) {
      return "庫存不足";
    }

    Long userID1 = 1L;

    synchronized (userID1.toString().intern()) {
      // 獲取代理對象
      VoucherOrderRepository proxy = (VoucherOrderRepository) AopContext.currentProxy();
      return createVoucherOrder(voucherId);
    }

  }

  @Transactional
  public String createVoucherOrder(int voucherId) {

    // int count = voucherOrderRepository.countByUser_id(1);

    // if (count > 0) {
    // return "用戶已經買過一次";
    // }

    // 5.扣減庫存
    int success = killVoucherRepository.updateKillVoucherMinus(1);

    if (success == 0) {
      return "庫存不足";
    }

    // 6.創建訂單

    VoucherOrder voucherOrder = new VoucherOrder();

    voucherOrder.setID(1);

    voucherOrderRepository.save(voucherOrder);

    return "創建成功";

  }

}
