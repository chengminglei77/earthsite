package edu.xpu.cs.lovexian.common.domain;

/**                                                                                ____________________
      _                _                                                           < 神兽护体，永无bug! >
    | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
   | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
  | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
                                   |___/|_|                |___/                                ||----w |
                                                                                                ||     ||
 * @author huchengpeng
 * LovaXian常量
 */
public class EarthSiteConstant {

    // user缓存前缀
    public static final String USER_CACHE_PREFIX = "lovexian.cache.user.";
    // user角色缓存前缀
    public static final String USER_ROLE_CACHE_PREFIX = "lovexian.cache.user.role.";
    // user权限缓存前缀
    public static final String USER_PERMISSION_CACHE_PREFIX = "lovexian.cache.user.permission.";
    // user个性化配置前缀
    public static final String USER_CONFIG_CACHE_PREFIX = "lovexian.cache.user.config.";
    // token缓存前缀
    public static final String TOKEN_CACHE_PREFIX = "lovexian.cache.token.";
    // scene缓存前缀
    public static final String SCENE_CACHE_PREFIX = "lovexian.cache.scene.";

    //hotel缓存前缀
    public static final String HOTEL_CACHE_PREFIX = "lovexian.cache.hotel";

    // scene pos缓存KEY
    public static final String SCENEPOI_CACHE_KEY = "lovexian.cache.scenepoi";

    // hotel pos缓存KEY
    public static final String HOTELPOI_CACHE_KEY = "lovexian.cache.hotelpoi";

    // 存储在线管理员用户的 zset前缀
    public static final String ACTIVE_USERS_ZSET_PREFIX = "lovexian.user.active";


    // 存储在线用户数目
    public static final String ACTIVE_CELL_USERS_NUMBER = "lovexian.celluser.active.number";

    // 排序规则： descend 降序
    public static final String ORDER_DESC = "desc";
    // 排序规则： ascend 升序
    public static final String ORDER_ASC = "asc";

    // 按钮
    public static final String TYPE_BUTTON = "1";
    // 菜单
    public static final String TYPE_MENU = "0";


    // 允许下载的文件类型，根据需求自己添加（小写）
    public static final String[] VALID_FILE_TYPE = {"xlsx", "zip"};

    // 前端页面路径前缀
    public static final String VIEW_PREFIX = "lovexian/views/";

    /**
     * {@link edu.xpu.cs.lovexian.common.controller.BaseController}
     * getDataTable 中 HashMap 默认的初始化容量
     */
    public static final int DATA_MAP_INITIAL_CAPACITY = 10;

    /**
     * 异步线程池名称
     */
    public static final String ASYNC_POOL = "lovexianAsyncThreadPool";

}
