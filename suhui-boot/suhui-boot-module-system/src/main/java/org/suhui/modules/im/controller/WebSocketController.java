package org.suhui.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import org.suhui.common.util.DateUtils;
import org.suhui.modules.im.entity.ImEntity;
import org.suhui.modules.im.server.SocketServer;
import org.suhui.modules.im.service.ImService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


/**
 * websocket
 * 消息推送(个人和广播)
 */
@Controller
@RequestMapping("/im")
public class WebSocketController {

    @Autowired
    private SocketServer socketServer;
    @Autowired
    private ImService imService;

    /**
     *
     * 客户端页面
     * @return
     */
    @RequestMapping(value = "/index")
    public String idnex() {

        return "index";
    }

//    /**
//     *
//     * 服务端页面
//     * @param model
//     * @return
//     */
//    @RequestMapping(value = "/admin")
//    public String admin(Model model) {
//        int num = socketServer.getOnlineCount();
//        List<String> list = socketServer.getOnlineUsers();
//
//        model.addAttribute("num",num);
//       // model.addAttribute("users",list);
//        return "admin";
//    }

    /**
     * 个人信息推送
     * @return
     *  @ApiParam(required = true, name = "username", value = "账号") @RequestParam(value = "username") String username,
     *             @ApiParam(required = true, name = "password", value = "密码") @RequestParam(value = "password") String password,
     */
    @RequestMapping("/sendmsg")
    @ResponseBody
    public String sendmsg(@RequestParam Map<String, Object> params){
        System.out.println(params.toString());
        //{Content=ddffffffg, MessageKey=115, MessageType=0, Parameter={"type":"text"}, ClientToken=0b1576160405458939f97ba6dd44ed05, Template=WebSocket}
        //
        //第一个参数 :msg 发送的信息内容
        //第二个参数为用户长连接传的用户人数
        //String [] persons = username.split(",");
        //记录消息记录
        ImEntity imEntity=new ImEntity();
        imEntity.setSenduser(params.get("MessageFromKey").toString());
        imEntity.setReceiveuser(params.get("MessageKey").toString());
        imEntity.setMsg(params.get("Content").toString());
        imEntity.setCreatetime(DateUtils.now());
        imService.save(imEntity);
        SocketServer.sendMessage(imEntity.getId(),imEntity.getMsg(),imEntity.getSenduser(),imEntity.getReceiveuser());
        return "success";
    }

    /**
     * 根据客户端标识登陆 获取 ClientToken
     * @param params
     * @return
     */
    @RequestMapping("/wsbyclient")
    @ResponseBody
    public String wsbyclient(@RequestParam Map<String, Object> params){
        System.out.println(params.toString());
        JSONObject obj=new JSONObject();
        obj.put("code", 200);
        JSONObject date=new JSONObject();
        date.put("id",  params.get("uuid").toString());//"projectToken" -> "6D7-BB91-13901823A235"  "parameter" -> "{"client":"android"}"
        obj.put("date",date);
        obj.put("info", "");
        return obj.toString();
    }
    /**
     *更新消息状态
     * @return
     */
    @RequestMapping("/updatemsg")
    @ResponseBody
    public String updatemsg(int  id){
        //第一个参数 :msg 发送的信息内容
        //第二个参数为用户长连接传的用户人数
        //String [] persons = username.split(",");
        //记录消息记录
        ImEntity imEntity=new ImEntity();
        imEntity.setId(id);;
        imService.updateById(imEntity);
        return "success";
    }
    /**
     * 推送给所有在线用户
     * @return
     */
    @RequestMapping("/sendAll")
    @ResponseBody
    public String sendAll(String msg){
        SocketServer.sendAll(msg);
        return "success";
    }
}
