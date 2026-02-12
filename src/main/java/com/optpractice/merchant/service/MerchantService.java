package com.optpractice.merchant.service;

import com.optpractice.global.exception.ApiException;
import com.optpractice.global.response.ErrorCode;
import com.optpractice.merchant.dto.MerchantRequest;
import com.optpractice.merchant.dto.MerchantResponse;
import com.optpractice.merchant.entity.Category;
import com.optpractice.merchant.entity.Merchant;
import com.optpractice.merchant.repository.CategoryRepository;
import com.optpractice.merchant.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 가맹점 등록
     */
    @Transactional
    public Long registerMerchant(MerchantRequest.RegisterMerchant request) {
        // 카테고리 조회
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));

        // 존재하는 가맹점명인지 체크
        if (merchantRepository.existsByName(request.name())) {
            throw new ApiException(ErrorCode.MERCHANT_ALREADY_EXISTS);
        }

        // 가맹점 등록
        Merchant merchant = Merchant.register(request.name(), category, request.imageUrl());

        try {
            merchantRepository.save(merchant);
        } catch (DataIntegrityViolationException e) {
            throw new ApiException(ErrorCode.MERCHANT_ALREADY_EXISTS);
        }

        return merchant.getId();
    }

    /**
     * 가맹점 수정
     */
    @Transactional
    public void updateMerchant(Long merchantId, MerchantRequest.UpdateMerchant request) {
        // 가맹점 조회
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ApiException(ErrorCode.MERCHANT_NOT_FOUND));

        // 존재하는 가맹점명인지 체크 (나 제외)
        if (merchantRepository.existsByNameAndIdNot(request.name(), merchantId)) {
            throw new ApiException(ErrorCode.MERCHANT_ALREADY_EXISTS);
        }

        // 가맹점 수정
        merchant.update(request.name(), request.imageUrl());

        log.info("가맹점 수정 완료 - merchantId: {}", merchant.getId());
    }

    /**
     * 가맹점 리스트 조회
     */
    @Transactional(readOnly = true)
    public List<MerchantResponse.MerchantItem> getMerchants(Long categoryId) {
        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            throw new ApiException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        List<Merchant> merchants = merchantRepository.findMerchants(categoryId);

        return merchants.stream()
                .map(MerchantResponse.MerchantItem::from)
                .toList();
    }
}