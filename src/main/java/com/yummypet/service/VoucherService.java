package com.yummypet.service;


import com.yummypet.dto.request.CreateVoucherRequest;
import com.yummypet.entity.Voucher;
import com.yummypet.repository.VoucherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VoucherService {
    private final VoucherRepository voucherRepository;

    public boolean isVoucherValid(Voucher voucher) {
        if (voucher == null) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        if (!voucher.getIsActive()) {
            return false;
        }

        if (voucher.getEndDate() != null && voucher.getEndDate().isBefore(now)) {
            return false;
        }

        if (voucher.getStartDate() != null && voucher.getStartDate().isAfter(now)) {
            return false;
        }

        if (voucher.getUsageLimit() != null && voucher.getUsedCount() >= voucher.getUsageLimit()) {
            return false;
        }

        return false;

    }

    public BigDecimal calculateDiscount(Voucher voucher, BigDecimal subtotal){
        if(voucher == null){
            return BigDecimal.ZERO;
        }

        BigDecimal discount;

        switch (voucher.getDiscountType()){
            case percentage:
                discount = subtotal.multiply(voucher.getDiscountValue().divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
                break;
            case fixed_amount:
                discount = voucher.getDiscountValue();
                break;
            default:
                discount = BigDecimal.ZERO;
        }

        if(voucher.getMaxDiscountAmount()!= null && discount.compareTo(voucher.getMaxDiscountAmount())>0){
            discount = voucher.getMaxDiscountAmount();
        }

        return discount;
    }

    public void incrementVoucherUsage(Voucher voucher){
        if(voucher != null){
            voucher.setUsedCount(voucher.getUsedCount()+1);
            voucherRepository.save(voucher);
        }
    }

    @Transactional
    public Voucher createVoucher(CreateVoucherRequest request){
        if (voucherRepository.existsByCode(request.getCode())){
            throw new RuntimeException("Mã voucher đã tồn tại");
        }
        Voucher voucher = new Voucher();
        voucher.setCode(request.getCode());
        voucher.setName(request.getName());
        voucher.setDiscountType(request.getDiscountType());
        voucher.setDiscountValue(request.getDiscountValue());
        voucher.setMinOrderAmount(request.getMinOrderAmount());
        voucher.setMaxDiscountAmount(request.getMaxDiscountAmount());
        voucher.setUsageLimit(request.getUsageLimit());
        voucher.setUsedCount(0);
        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());
        voucher.setIsActive(request.getIsActive());
        voucher.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return voucherRepository.save(voucher);
    }

}
