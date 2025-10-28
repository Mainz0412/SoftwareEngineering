package com.huawei.web.controller;

import com.huawei.web.entity.Reserve;
import com.huawei.web.entity.SeatTime;
import com.huawei.web.service.ReserveService;
import com.huawei.web.service.SeatTimeService;
import com.huawei.web.util.AjaxResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Ruijie Zhao
 */
@RestController
@RequestMapping("/reserve")
public class ReserveController {
    @Resource
    ReserveService reserveService;
    @Resource
    SeatTimeService seatTimeService;

    /**
     * 新增预约
     *
     * @param reserve 预约信息
     * @return 完成结果
     */
    @PostMapping("/add")
    public AjaxResult reserve(@RequestBody Reserve reserve) {
        SeatTime seatTime = seatTimeService.occupySeat(reserve);
        if (seatTime == null) {
            return AjaxResult.error("座位已被占用");
        }
        reserveService.insertReserve(reserve);
        seatTimeService.updateSeatReserve(reserve, seatTime);
        return AjaxResult.success();
    }

    /**
     * 更新预约信息
     *
     * @param reserve 预约信息
     * @return msg结果
     */
    @PutMapping("/update")
    public AjaxResult update(@RequestBody Reserve reserve) {
        //找到原来的seatTime
        SeatTime preSeatTime = seatTimeService.findSeatTime(reserve);
        //占用座位
        SeatTime seatTime = seatTimeService.occupySeat(reserve);
        if (seatTime == null) {
            return AjaxResult.error("座位已被占用");
        }
        //释放原座位
        seatTimeService.releaseSeat(preSeatTime);
        //更新结果
        reserveService.updateReserve(reserve);
        return AjaxResult.success();
    }

    /**
     * 删除预约
     *
     * @param reserve 预约信息
     * @return msg结果
     */
    @DeleteMapping("/delete")
    public AjaxResult delete(@RequestBody Reserve reserve) {
        //释放座位
        seatTimeService.deleteSeat(reserve);
        //删除预约
        reserveService.deleteReserve(reserve);
        return AjaxResult.success();
    }

    /**
     * 获得用户预约list
     *
     * @param userAccount 用户
     * @return 用户预约list
     */
    @GetMapping(value = "/list")
    public List<Reserve> list(@RequestParam("userAccount") String userAccount) {
        return reserveService.selectList(userAccount);
    }

    /**
     * 获得所有预约list
     *
     * @return 用户预约list
     */
    @GetMapping(value = "/all")
    public List<Reserve> listAll() {
        return reserveService.selectAllList();
    }
}
