package com.trackray.module.auxiliary;

import com.trackray.base.annotation.Plugin;
import com.trackray.base.annotation.Rule;
import com.trackray.base.attack.Metasploit;
import com.trackray.base.plugin.WebSocketPlugin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/1/16 15:56
 */
@Rule(websocket = true , sync = true)
@Plugin(title = "Metasploit" , author = "浅蓝", desc = "metasploit websocket插件" , time = 1547625524)
public class Msf extends WebSocketPlugin {

    @Autowired
    private Metasploit metasploit;

    @Override
    public boolean check(Map param) {
        if (metasploit.login() || StringUtils.isEmpty(metasploit.getToken())) {
            errorMsg = "metasploit 认证未通过";
            return false;
        }
        return true;
    }

    @Override
    public Object start() {
        println(metasploit.banner());
        while (true){

            println(metasploit.getConsole());
            String input = getInput();
            if (input.equals("quit")){
                break;
            }
            String response = metasploit.execute(metasploit.list(input));
            println(response);
        }
        return "bye!";
    }

    @Override
    public void onClose() {
        if (metasploit.isLogind() && StringUtils.isNotEmpty(metasploit.getToken())) {
            metasploit.close();
        }
    }
}
