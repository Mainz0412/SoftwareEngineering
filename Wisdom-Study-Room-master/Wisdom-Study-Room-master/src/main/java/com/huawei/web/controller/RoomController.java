package com.huawei.web.controller;

import com.huawei.web.entity.Room;
import com.huawei.web.service.RoomService;
import com.huawei.web.util.AjaxResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Binhui Liu
 */
@RestController
@RequestMapping("/room")
public class RoomController {
    @Resource
    RoomService roomService;

    /**
     * 添加房间
     *
     * @param room 房间
     * @return msg信息
     */
    @PostMapping("/insert")
    public AjaxResult insert(@RequestBody Room room) {
        Room requestRoom = roomService.selectRoomName(room);
        if (requestRoom != null) {
            return AjaxResult.error("房间已存在");
        }
        //添加房间
        roomService.insertRoom(room);
        return AjaxResult.success();
    }

    /**
     * 更新房间信息
     *
     * @param room 房间信息
     * @return msg结果
     */
    @PutMapping("/update")
    public AjaxResult update(@RequestBody Room room) {
        Room requestRoom = roomService.selectRoomName(room);
        if (requestRoom != null) {
            return AjaxResult.error("房间名重复");
        }
        //更新结果
        roomService.updateRoom(room);
        return AjaxResult.success();
    }

    /**
     * 删除房间
     *
     * @param room 房间
     * @return msg信息
     */
    @DeleteMapping("/delete")
    public AjaxResult delete(@RequestBody Room room) {
        //删除房间
        roomService.deleteRoom(room);
        return AjaxResult.success();
    }

    /**
     * 根据房间Id获得房间list
     *
     * @param roomId 用户
     * @return 用户list
     */
    @GetMapping(value = "/listbyid")
    public List<Room> list(@RequestParam("roomId") Integer roomId) {
        return roomService.selectRoomListById(roomId);
    }

    /**
     * 根据房间Name获得房间list
     *
     * @param roomNmae 用户
     * @return 用户list
     */
    @GetMapping(value = "/listbyname")
    public List<Room> list(@RequestParam("roomName") String roomNmae) {
        return roomService.selectRoomListByName(roomNmae);
    }

    /**
     * 获得所有房间list
     *
     * @return 房间list
     */
    @GetMapping(value = "/all")
    public List<Room> listAll() {
        return roomService.selectAllList();
    }
}
