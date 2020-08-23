package edu.xpu.cs.lovexian.app.appadmin.upload;

import edu.xpu.cs.lovexian.common.service.CacheService;
import io.tus.java.client.TusURLStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;

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
@Service
public class TusURLRedisStore implements TusURLStore  {

    @Autowired
    private CacheService cacheService;

    @Override
    public void set(String fingerprint, URL url) {
        fingerprint = fingerprint.toLowerCase();
        cacheService.setTusUrl(fingerprint, url);
    }

    @Override
    public URL get(String fingerprint) {
        fingerprint = fingerprint.toLowerCase();
        return cacheService.getTusUrl(fingerprint);
    }

    @Override
    public void remove(String fingerprint) {
        fingerprint = fingerprint.toLowerCase();
        cacheService.removeTusUrl(fingerprint);
    }
}
