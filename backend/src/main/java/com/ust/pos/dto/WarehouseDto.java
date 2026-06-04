package com.ust.pos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseDto extends CommonDto {
    private String address;
    private String contactNumber;
}
