package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.request.product.ProductFilterRequestDTO;
import com.inventory_system.backend.dto.request.product.ProductRequestDTO;
import com.inventory_system.backend.dto.response.product.ProductResponseDTO;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.model.Product;
import com.inventory_system.backend.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static com.inventory_system.backend.util.InventorySystemConstant.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StatusService statusService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private StockService stockService;
    @Autowired
    private ElasticSearchService elasticSearchService;

    public Product findById(int id) throws BusinessException {
        return productRepository.findById(id).orElseThrow(()
                -> new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND));
    }

    public Page<Product> findAll(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            Sort sortDefault = Sort.by("id").descending();
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortDefault);
        }
        return productRepository.findAll(pageable);
    }

    public Page<Product> findAllFiltered(Pageable pageable, ProductFilterRequestDTO productFilterRequestDTO) {
        if (pageable.getSort().isEmpty()) {
            Sort sortDefault = Sort.by("id").descending();
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortDefault);
        }

        if (ObjectUtils.isEmpty(productFilterRequestDTO.getName()) && ObjectUtils.isEmpty(productFilterRequestDTO.getCode())) {
            return findAll(pageable);
        } else if (ObjectUtils.isEmpty(productFilterRequestDTO.getName()) || ObjectUtils.isEmpty(productFilterRequestDTO.getCode())) {
            return productRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(pageable,
                    ObjectUtils.isEmpty(productFilterRequestDTO.getName()) ? "ÑÑÑ" : productFilterRequestDTO.getName(),
                    ObjectUtils.isEmpty(productFilterRequestDTO.getCode()) ? "ÑÑÑ" : productFilterRequestDTO.getCode());
        } else {
            return productRepository.findByNameContainingIgnoreCaseAndCodeContainingIgnoreCase(pageable,
                    productFilterRequestDTO.getName(), productFilterRequestDTO.getCode());
        }
    }

    public List<ProductResponseDTO> findAllExisting(String input) {
        List<Integer> productsIdList = new ArrayList<>();
        List<ProductResponseDTO> productResponseDTOList = elasticSearchService.getSuggestion(input, null);
        productResponseDTOList.forEach(productResponseDTO -> productsIdList.add(productResponseDTO.getId()));
        List<ProductResponseDTO> productResponseDTOList2 = new ArrayList<>();
        productRepository.findAllActiveById(productsIdList).forEach(product ->
                productResponseDTOList2.add(modelMapper.map(product, ProductResponseDTO.class)));
        return productResponseDTOList2;
    }

    public Product create(ProductRequestDTO productRequestDTO) throws BusinessException {
        boolean exist = productRepository.findByCodeOrName(productRequestDTO.getShortName(), productRequestDTO.getBarcode()).size() == 0;
        Product product;
        if (exist) {
            product = modelMapper.map(productRequestDTO, Product.class);
            product.setStatus(statusService.findById(productRequestDTO.getStatusId()));
            product = productRepository.save(product);
        } else {
            throw new BusinessException(RECORD_EXIST_CODE, RECORD_EXIST + "code, name");
        }
        return product;
    }

    public Product update(ProductRequestDTO productRequestDTO, int id) throws BusinessException {
        Product product = findById(id);

        product.setStatus(statusService.findById(productRequestDTO.getStatusId()));
        /*Unique field don't map*/
        productRequestDTO.setBarcode(product.getBarcode());
        productRequestDTO.setShortName(product.getShortName());
        modelMapper.map(productRequestDTO, product);
        product = productRepository.save(product);
        if (stockService.findByProductId(product.getId()).size() > 0) {
            elasticSearchService.insertUpdateDocument(product);
        }
        return product;
    }

    public boolean delete(Integer id) throws BusinessException {
        Product product = findById(id);
        try {
            productRepository.delete(product);
            return true;
        } catch (Exception e) {
            throw new BusinessException(RECORD_CANNOT_BE_DELETED_CODE, RECORD_CANNOT_BE_DELETED);
        }
    }
}
