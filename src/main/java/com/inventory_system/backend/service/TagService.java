package com.inventory_system.backend.service;

import com.inventory_system.backend.dto.request.stock.StockFilterRequestDTO;
import com.inventory_system.backend.dto.request.tag.TagFilterRequestDTO;
import com.inventory_system.backend.dto.request.tag.TagRequestDTO;
import com.inventory_system.backend.dto.response.tag.TagResponseDTO;
import com.inventory_system.backend.dto.response.tag.TagTypeResponseDTO;
import com.inventory_system.backend.exception.BusinessException;
import com.inventory_system.backend.exception.UnauthorizedException;
import com.inventory_system.backend.model.Stock;
import com.inventory_system.backend.model.Tag;
import com.inventory_system.backend.model.TagType;
import com.inventory_system.backend.repository.TagRepository;
import com.inventory_system.backend.repository.TagTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.inventory_system.backend.util.InventorySystemConstant.*;
import static com.inventory_system.backend.util.InventorySystemConstant.RECORD_NOT_FOUND;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagTypeRepository tagTypeRepository;
    @Autowired
    ModelMapper modelMapper;

    public Tag findById(int id) throws BusinessException {
        return tagRepository.findById(id).orElseThrow(() ->
                new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND));
    }

    public Tag findByNameAndTypeId(String name, Integer typeId) {
        return tagRepository.findByNameAndTypeId(name, typeId).orElse(null);
    }

    public TagResponseDTO findByIdDto(int id) throws BusinessException {
        Tag tag = tagRepository.findById(id).orElseThrow(() ->
                new BusinessException(RECORD_NOT_FOUND_CODE, RECORD_NOT_FOUND));
        return this.mapTagToTagResponseDto(tag);
    }

    public Page<TagResponseDTO> findAll(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            Sort sortDefault = Sort.by("id").descending();
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortDefault);
        }
        return tagRepository.findAll(pageable).map(this::mapTagToTagResponseDto);
    }

    public List<TagTypeResponseDTO> findAllTagType() {
        List<TagType> tagTypeList = tagTypeRepository.findAll();
        if(CollectionUtils.isEmpty(tagTypeList))
            return new ArrayList<>();
        List<TagTypeResponseDTO> tagTypeResponseDTOList = new ArrayList<>();
        tagTypeList.forEach(tagType ->
                tagTypeResponseDTOList.add(new TagTypeResponseDTO(tagType.getId(),tagType.getName())));
        return tagTypeResponseDTOList;
    }

    public Page<TagResponseDTO> findAllFiltered(Pageable pageable, TagFilterRequestDTO tagFilterRequestDTO) {
        if (pageable.getSort().isEmpty()) {
            Sort sortDefault = Sort.by("id").descending();
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortDefault);
        }

        if (ObjectUtils.isEmpty(tagFilterRequestDTO.getName()) && ObjectUtils.isEmpty(tagFilterRequestDTO.getTypeName())) {
            return findAll(pageable);
        } else if (ObjectUtils.isEmpty(tagFilterRequestDTO.getName()) || ObjectUtils.isEmpty(tagFilterRequestDTO.getTypeName())) {
            return tagRepository.findByTagNameContainingIgnoreCaseOrTypeNameContainingIgnoreCase(
                    ObjectUtils.isEmpty(tagFilterRequestDTO.getName()) ? "ÑÑÑ" : tagFilterRequestDTO.getName().toLowerCase(),
                    ObjectUtils.isEmpty(tagFilterRequestDTO.getTypeName()) ? "ÑÑÑ" : tagFilterRequestDTO.getTypeName().toLowerCase(), pageable)
                    .map(this::mapTagToTagResponseDto);
        } else {
            return tagRepository.findByTagNameContainingIgnoreCaseAndTypeNameContainingIgnoreCase(
                    tagFilterRequestDTO.getName().toLowerCase(), tagFilterRequestDTO.getTypeName().toLowerCase(), pageable)
                    .map(this::mapTagToTagResponseDto);
        }
    }

    public TagResponseDTO create(TagRequestDTO tagRequestDTO) throws BusinessException {
        Tag tagExist = findByNameAndTypeId(tagRequestDTO.getName(), tagRequestDTO.getTypeId());
        if (Objects.isNull(tagExist)) {
            Tag tag = new Tag(null, tagRequestDTO.getFatherId(),
                    tagRequestDTO.getName(), null);

            TagType tagType = null;
            if (tagRequestDTO.getTypeId() != null) {
                tagType = tagTypeRepository.findById(tagRequestDTO.getTypeId()).orElse(null);
            }
            tag.setType(tagType);
            return this.mapTagToTagResponseDto(tagRepository.save(tag));
        } else {
            throw new BusinessException(RECORD_EXIST_CODE, RECORD_EXIST + "name, typeId");
        }
    }

    public TagResponseDTO update(TagRequestDTO tagRequestDTO, int id) throws BusinessException {
        Tag tag = findById(id);
        if(!tag.getName().equals(tagRequestDTO.getName())||
                (tag.getType()!=null&&tag.getType().getId()!=tagRequestDTO.getTypeId())) {
            Tag tagExist = findByNameAndTypeId(tagRequestDTO.getName(),tagRequestDTO.getTypeId());
            if (!Objects.isNull(tagExist) && tag.getId() != id) {
                throw new BusinessException(RECORD_EXIST_CODE, RECORD_EXIST + "name, typeId");
            }
        }
        tag.setName(tagRequestDTO.getName());
        tag.setFatherId(tagRequestDTO.getFatherId());

        TagType tagType = null;
        if (tagRequestDTO.getTypeId() != null) {
            tagType = tagTypeRepository.findById(tagRequestDTO.getTypeId()).orElse(null);
        }
        tag.setType(tagType);
        return this.mapTagToTagResponseDto( tagRepository.save(tag));
    }

    public boolean delete(Integer id) throws BusinessException {
        Tag tagToDelete = findById(id);
        tagRepository.delete(tagToDelete);
        return true;
    }

    private TagResponseDTO mapTagToTagResponseDto(Tag tag) {
        TagResponseDTO tagResponseDTO = modelMapper.map(tag, TagResponseDTO.class);
        if (tag.getFatherId() != null) {
            try {
                tagResponseDTO.setFatherName(findById(tag.getFatherId()).getName());
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
        if (tag.getType().getFatherId() != null) {
            TagType tagType = tagTypeRepository.findById(tag.getType().getFatherId()).orElse(null);
            tagResponseDTO.setFatherTypeName(tagType != null ? tagType.getName() : null);
        }
        return tagResponseDTO;
    }
}
