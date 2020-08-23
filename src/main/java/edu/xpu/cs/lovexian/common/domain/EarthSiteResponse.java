package edu.xpu.cs.lovexian.common.domain;

import edu.xpu.cs.lovexian.common.response.RespStatus;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

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
public class EarthSiteResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;

    private EarthSiteResponse(){super();}
    private EarthSiteResponse(HttpStatus status){
        this.put("code", status.value());
    }

    public EarthSiteResponse status(String status) {
        this.put("status", status);
        return this;
    }

    public EarthSiteResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public EarthSiteResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    @Override
    public EarthSiteResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }


    public static EarthSiteResponse SUCCESS(){
        return new EarthSiteResponse(HttpStatus.OK).status(RespStatus.success.getCode());
    }

    public static EarthSiteResponse FAIL(){
        return new EarthSiteResponse(HttpStatus.OK).status(RespStatus.fail.getCode());
    }

}
