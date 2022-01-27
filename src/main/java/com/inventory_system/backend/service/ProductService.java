package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.request.product.ProductRequestDTO;
import com.inventory_system.backend.dto.response.product.ProductResponseDTO;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.model.Product;
import com.inventory_system.backend.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        return productRepository.findAll(pageable);
    }

    public List<ProductResponseDTO> findAllExisting(String input) {
        List<Integer> productsIdList = new ArrayList<>();
        List<ProductResponseDTO> productResponseDTOList = elasticSearchService.getSuggestion(input, null);
        productResponseDTOList.forEach(productResponseDTO -> productsIdList.add(productResponseDTO.getId()));
        List<ProductResponseDTO> productResponseDTOList2 =new ArrayList<>();
        productRepository.findAllActiveById(productsIdList).forEach(product ->
                productResponseDTOList2.add(modelMapper.map(product, ProductResponseDTO.class)));
        return productResponseDTOList2;
    }

    public Product create(ProductRequestDTO productRequestDTO) throws BusinessException {
        Product product = productRepository.findByShortNameOrBarcode(productRequestDTO.getShortName(), productRequestDTO.getBarcode()).orElse(null);

        if (Objects.isNull(product)) {
            product = modelMapper.map(productRequestDTO, Product.class);
            product.setStatus(statusService.findById(productRequestDTO.getStatusId()));
            product = productRepository.save(product);
        } else {
            throw new BusinessException(RECORD_EXIST_CODE, RECORD_EXIST + "shortName, barcode");
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
