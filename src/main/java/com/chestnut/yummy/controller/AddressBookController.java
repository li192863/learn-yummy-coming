package com.chestnut.yummy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chestnut.yummy.common.BaseContext;
import com.chestnut.yummy.common.R;
import com.chestnut.yummy.entity.AddressBook;
import com.chestnut.yummy.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址簿管理
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 添加地址
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> luw = new LambdaUpdateWrapper<>();
        luw.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        luw.set(AddressBook::getIsDefault, 0);
        // SQL: update address_book set is_default = 0 where user_id = ?
        addressBookService.update(luw);

        addressBook.setIsDefault(1);
        // SQL: update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        lqw.eq(AddressBook::getIsDefault, 1);

        // SQL: select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(lqw);
        return R.success(addressBook);
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());

        // 条件构造器
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        lqw.orderByDesc(AddressBook::getUpdateTime);

        // SQL: select * from address_book where user_id = ? order by update_time desc
        List<AddressBook> list = addressBookService.list(lqw);
        return R.success(list);
    }
}
