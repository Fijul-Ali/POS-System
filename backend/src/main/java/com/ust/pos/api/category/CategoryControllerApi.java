package com.ust.pos.api.category;

import com.ust.pos.api.BaseController;
import com.ust.pos.category.service.CategoryService;
import com.ust.pos.dto.CategoryDto;
import com.ust.pos.dto.PaginationDto;
import com.ust.pos.dto.WsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryControllerApi extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/list")
    public WsDto<CategoryDto> home(@RequestBody PaginationDto paginationDto) {
        Pageable pageable = getPageable(paginationDto.getPage(), paginationDto.getSizePerPage(), paginationDto.getSortDirection(), paginationDto.getSortField());
        return categoryService.findAll(pageable);
    }

    @PostMapping("/add")
    public CategoryDto addPost(@RequestBody CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @GetMapping("/get")
    public CategoryDto update(@RequestParam String identifier) {
        return categoryService.findByIdentifier(identifier);
    }

    @PostMapping("/update")
    public CategoryDto updatePost(@RequestBody CategoryDto categoryDto) {
        return categoryService.update(categoryDto);
    }

    @GetMapping("/delete")
    public boolean delete(@RequestParam String identifier) {
        try {
            categoryService.delete(identifier);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @PostMapping("/changestatus")
    public CategoryDto changeStatus(@RequestBody CategoryDto categoryDto) {
        return categoryService.updateStatus(categoryDto.getIdentifier(), categoryDto.isStatus());
    }

    @GetMapping("/active")
    public List<CategoryDto> findAllActive() {
        return categoryService.findAllActive();
    }

    @GetMapping("/activechilds")
    public List<CategoryDto> findAllActiveChild() {
        return categoryService.findAllActiveChilds();
    }
}
