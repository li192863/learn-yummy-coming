package com.chestnut.haoweilai.dto;

import com.chestnut.haoweilai.entity.OrderDetail;
import com.chestnut.haoweilai.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}
