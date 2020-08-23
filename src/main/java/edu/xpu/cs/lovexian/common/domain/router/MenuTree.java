package edu.xpu.cs.lovexian.common.domain.router;

import com.fasterxml.jackson.annotation.JsonInclude;
import edu.xpu.cs.lovexian.systemadmin.domain.SysMenu;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**                                                                                ____________________
      _                _                                                           < 神兽护体，永无bug! >
    | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
   | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
  | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
                                   |___/|_|                |___/                                ||----w |
                                                                                                ||     ||
 * @author hcp
 * @version 1.0.0
 * @create 20:43 2019/11/5
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
/**
 * 保证序列化json的时候,如果是null的对象,key也会消失
 */
public class MenuTree<T> implements Serializable {

    private static final long serialVersionUID = 7681873362531265829L;

    private String id;
    private String icon;
    private String jump;
    private String title;
    private String name;
    private Map<String, Object> state;
    private Boolean checked = false;
    private Map<String, Object> attributes;
    private List<MenuTree<T>> list = new ArrayList<>();
    private String parentId;
    private boolean hasParent = false;
    private boolean hasChild = false;

    private SysMenu data;

}