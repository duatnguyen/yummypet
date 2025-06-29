package com.yummypet.service;


import com.yummypet.dto.request.CreateVoucherRequest;
import com.yummypet.dto.request.UpdateVoucherRequest;
import com.yummypet.entity.Voucher;
import com.yummypet.repository.VoucherRepository;
import com.yummypet.dto.response.VoucherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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

    public  Voucher getVoucherById(Integer id){
        return voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher"));

    }

    public Voucher getVoucherByCode(String code){
        return voucherRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("không tìm thấy voucher"));
    }


    @Transactional
    public Voucher updateVoucher(Integer id,UpdateVoucherRequest request){
        Voucher voucher = getVoucherById(id);

        if(request.getName() != null){
            voucher.setName(request.getName());
        }
        if(request.getDiscountType() != null){
            voucher.setDiscountType(request.getDiscountType());
        }

        if(request.getDiscountValue() != null){
            voucher.setDiscountValue(request.getDiscountValue());
        }

        if(request.getMinOrderAmount() != null){
            voucher.setMinOrderAmount(request.getMinOrderAmount());
        }

        if(request.getMaxDiscountAmount() != null){
            voucher.setMaxDiscountAmount(request.getMaxDiscountAmount());
        }

        if(request.getUsageLimit() != null){
            voucher.setUsageLimit(request.getUsageLimit());
        }

        if(request.getStartDate() != null){
            voucher.setStartDate(request.getStartDate());
        }

        if(request.getEndDate() != null){
            voucher.setEndDate(request.getEndDate());
        }

        if(request.getIsActive() != null){
            voucher.setIsActive(request.getIsActive());
        }

        voucher.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return voucherRepository.save(voucher);
    }

    @Transactional
    public void deleteVoucher(Integer id){
        Voucher  voucher = getVoucherById(id);
        voucherRepository.delete(voucher);
    }

    @Transactional
    public Voucher deactivateVoucher(Integer id){
        Voucher  voucher = getVoucherById(id);
        voucher.setIsActive(false);
        voucher.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return voucherRepository.save(voucher);
    }

    public Page<Voucher> getAllVouchers(Pageable pageable){
        return  voucherRepository.findAll(pageable);
    }

    public List<Voucher> getActiveVouchers(){
        return voucherRepository.findByIsActiveTrue();
    }

    public List<Voucher> getValidVouchers(){
        return voucherRepository.findAllValidVouchers(LocalDateTime.now());
    }

    public VoucherResponse mapVoucherToResponse(Voucher voucher){
        boolean isValid = isVoucherValid(voucher);

        return VoucherResponse.builder()
                .id(voucher.getId())
                .code(voucher.getCode())
                .name(voucher.getName())
                .discountType(voucher.getDiscountType())
                .discountValue(voucher.getDiscountValue())
                .minOrderAmount(voucher.getMinOrderAmount())
                .maxDiscountAmount(voucher.getMaxDiscountAmount())
                .usageLimit(voucher.getUsageLimit())
                .usedCount(voucher.getUsedCount())
                .startDate(voucher.getStartDate())
                .endDate(voucher.getEndDate())
                .isActive(voucher.getIsActive())
                .createdAt(voucher.getCreatedAt() != null ? voucher.getCreatedAt().toLocalDateTime() : null)
                .updateAt(voucher.getUpdatedAt() != null ? voucher.getUpdatedAt().toLocalDateTime() : null)
                .isValid(isValid)
                .build();
    }




}
