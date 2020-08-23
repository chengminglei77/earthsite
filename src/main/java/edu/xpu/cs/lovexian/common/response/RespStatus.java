package edu.xpu.cs.lovexian.common.response;

import lombok.Getter;

/**                                                                                ____________________
      _                _                                                           < 神兽护体，永无bug! >
    | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
   | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
  | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
                                   |___/|_|                |___/                                ||----w |
                                                                                                ||     ||
 * @author huchengpeng
 */
@Getter
public enum RespStatus {
    success("200","成功"),
    fail("400","失败"),
    paramsError("10086","参数错误"),
    limitError("10010","限流"),
    pageFail("300","页面静态化失败");

    private String code;
    private String desc;

    RespStatus(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
