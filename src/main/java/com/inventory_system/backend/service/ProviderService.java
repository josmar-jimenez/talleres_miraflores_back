package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.request.provider.ProviderRequestDTO;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.model.Product;
import com.inventory_system.backend.model.Provider;
import com.inventory_system.backend.repository.ProviderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.inventory_system.backend.util.InventorySystemConstant.*;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private StatusService statusService;
    @Autowired
    private ModelMapper modelMapper;

    public Provider findById(int id) throws BusinessException {
        return providerRepository.findById(id).orElseThrow(()
                -> new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND));
    }

    public Page<Provider> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), 1000, pageable.getSort());
        return providerRepository.findAll(pageable);
    }

    public Provider create(ProviderRequestDTO providerRequestDTO) throws BusinessException {
        Provider provider = providerRepository.findByName(providerRequestDTO.getName()).orElse(null);

        if (Objects.isNull(provider)) {
            provider = modelMapper.map(providerRequestDTO, Provider.class);
            provider.setStatus(statusService.findById(providerRequestDTO.getStatusId()));
            provider = providerRepository.save(provider);
        } else {
            throw new BusinessException(RECORD_EXIST_CODE, RECORD_EXIST + " shortName");
        }
        return provider;
    }

    public Provider update(ProviderRequestDTO providerRequestDTO, int id) throws BusinessException {
        Provider provider = findById(id);
        provider.setStatus(statusService.findById(providerRequestDTO.getStatusId()));
        modelMapper.map(providerRequestDTO, provider);
        provider = providerRepository.save(provider);
        return provider;
    }

    public boolean delete(Integer id) throws BusinessException {
        Provider provider = findById(id);
        try {
            providerRepository.delete(provider);
            return true;
        } catch (Exception e) {
            throw new BusinessException(RECORD_CANNOT_BE_DELETED_CODE, RECORD_CANNOT_BE_DELETED);
        }
    }
}
