package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.request.tag.TagRequestDTO;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.model.Tag;
import com.inventory_system.backend.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.inventory_system.backend.util.InventorySystemConstant.*;
import static com.inventory_system.backend.util.InventorySystemConstant.RECORD_NOT_FOUND;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Tag findById(int id) throws BusinessException {
        return tagRepository.findById(id).orElseThrow(() ->
                new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND));
    }

    public Tag findByName(String name) {
        return tagRepository.findByName(name).orElse(null);
    }


    public Page<Tag> findAll(Pageable pageable) {
        if(pageable.getSort().isEmpty()) {
            Sort sortDefault = Sort.by("id").descending();
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortDefault);
        }
        return tagRepository.findAll(pageable);
    }

    public Tag create(TagRequestDTO tagRequestDTO) throws BusinessException {
        Tag tagExist = findByName(tagRequestDTO.getName());
        if (Objects.isNull(tagExist)) {
            Tag tag = new Tag(null, tagRequestDTO.getName());
            return tagRepository.save(tag);
        } else {
            throw new BusinessException(RECORD_EXIST_CODE, RECORD_EXIST + "name");
        }
    }

    public Tag update(TagRequestDTO tagRequestDTO, int id) throws BusinessException {
        Tag tag = findById(id);
        Tag tagExist = findByName(tagRequestDTO.getName());
        if (!Objects.isNull(tagExist)) {
            throw new BusinessException(RECORD_EXIST_CODE, RECORD_EXIST + "name");
        }
        tag.setName(tagRequestDTO.getName());
        tag = tagRepository.save(tag);
        return tag;
    }

    public boolean delete(Integer id) throws BusinessException {
        Tag tagToDelete = findById(id);
        tagRepository.delete(tagToDelete);
        return true;
    }
}
