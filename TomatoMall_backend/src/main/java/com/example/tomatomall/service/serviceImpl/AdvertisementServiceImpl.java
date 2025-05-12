package com.example.tomatomall.service.serviceImpl;

import com.example.tomatomall.Repository.AdvertisementRepository;
import com.example.tomatomall.Repository.ProductRepository;
import com.example.tomatomall.exception.TomatoMallException;
import com.example.tomatomall.po.Advertisement;
import com.example.tomatomall.po.Product;
import com.example.tomatomall.service.AdvertisementService;
import com.example.tomatomall.vo.AdvertisementVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<AdvertisementVO> getAllAdvertisements() {
        return advertisementRepository.findAll().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public AdvertisementVO createAdvertisement(String title, String content, String imageUrl, Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("商品不存在"));

        Advertisement advertisement = new Advertisement();
        advertisement.setTitle(title);
        advertisement.setContent(content);
        advertisement.setImgUrl(imageUrl);
        advertisement.setProduct(product);

        Advertisement savedAd = advertisementRepository.save(advertisement);
        return toVO(savedAd);
    }

    @Override
    public AdvertisementVO updateAdvertisement(Integer id, String title, String content, String imageUrl, Integer productId) {
        Advertisement existingAd = advertisementRepository.findById(id)
                .orElseThrow(() -> TomatoMallException.productDoNotExist());


        if(productId!=null){
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("商品不存在"));
            existingAd.setProduct(product);
        }

        if(title != null){
            existingAd.setTitle(title);
        }
        if(content != null){
            existingAd.setContent(content);
        }
        if(imageUrl != null){
            existingAd.setImgUrl(imageUrl);
        }

        Advertisement updatedAd = advertisementRepository.save(existingAd);
        return toVO(updatedAd);
    }

    @Override
    public boolean deleteAdvertisement(Integer id) {
        if (advertisementRepository.existsById(id)) {
            advertisementRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private AdvertisementVO toVO(Advertisement advertisement) {
        AdvertisementVO vo = new AdvertisementVO();
        BeanUtils.copyProperties(advertisement, vo);
        vo.setProductId(advertisement.getProduct() != null ? advertisement.getProduct().getId() : null);
        return vo;
    }
}