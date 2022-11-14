package com.dalgorithm.nbuy.order.mapper;

import com.dalgorithm.nbuy.order.dto.OrderDto;
import com.dalgorithm.nbuy.order.entity.OrderInput;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<OrderDto> selectListMyOrder(OrderInput parameter);
}
