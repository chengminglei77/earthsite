package edu.xpu.cs.lovexian.app.appadmin.utils;

import edu.xpu.cs.lovexian.app.appadmin.entity.AdminCommandInfo;
import edu.xpu.cs.lovexian.app.appadmin.mapper.CommandInfoAdminMapper;
import edu.xpu.cs.lovexian.app.appadmin.service.ICommandInfoAdminService;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ACKCheck {

    @Autowired
    CommandInfoAdminMapper commandInfoAdminMapper;

    @Autowired
    private ICommandInfoAdminService commandInfoAdminService;

    AdminCommandInfo adminCommandInfo=new AdminCommandInfo();



    public void judgeIfTheSameCommand(String returnMessage){
        //AA5500A100020002AE8755AA0D0A
        String returnSubt1 = returnMessage.substring(0, 8);
        String returnSubt2 = returnMessage.substring(14, 16);
        String type = returnMessage.substring(12, 14);
        StringBuilder sb = new StringBuilder();
        sb.append(returnSubt1).append(returnSubt2);
        String synthesisReturnStr = sb.toString();//合成的返回的命令的字符串的拼接

        Map<String, String> map = allCommandMap();


        for(String key:map.keySet()){//keySet获取map集合key的集合  然后在遍历key即可
            String value = map.get(key);
            Date currentDate = new Date(System.currentTimeMillis());
            String id = commandInfoAdminMapper.checkIfExist(key);
            if (value.equals(synthesisReturnStr)){
                adminCommandInfo.setId(id);
                adminCommandInfo.setCommand(key);
                adminCommandInfo.setStatus(type);
                adminCommandInfo.setReceiveTime(currentDate);
                commandInfoAdminService.saveOrUpdate(adminCommandInfo);
                break;
            }
        }
    }

    public Map<String,String> allCommandMap(){
        List<String> list = commandInfoAdminMapper.selectAllCommand();
        Map<String,String> map = new HashMap<String,String>();
        for (String everyCommand:list){
            String sendSub1 = everyCommand.substring(0, 8);
            String sendSub2 = everyCommand.substring(12, 14);
            StringBuilder sb = new StringBuilder();
            sb.append(sendSub1).append(sendSub2);
            String synthesisSendStr = sb.toString();//合成的发送的命令的字符串的拼接
            map.put(everyCommand,synthesisSendStr);
        }
        return map;
    }


}
