package com.chestnut.yummy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chestnut.yummy.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
