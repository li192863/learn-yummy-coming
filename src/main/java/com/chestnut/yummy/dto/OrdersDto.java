package com.chestnut.yummy.dto;

import com.chestnut.yummy.entity.OrderDetail;
import com.chestnut.yummy.entity.Orders;
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
