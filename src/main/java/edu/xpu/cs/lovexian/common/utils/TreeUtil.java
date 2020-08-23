package edu.xpu.cs.lovexian.common.utils;

import edu.xpu.cs.lovexian.common.domain.Tree;
import edu.xpu.cs.lovexian.common.domain.router.DeptTree;
import edu.xpu.cs.lovexian.common.domain.router.MenuTree;

import java.util.ArrayList;
import java.util.List;
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
public class TreeUtil {

    protected TreeUtil() {

    }

    private final static String TOP_NODE_ID = "0";

    public static <T> MenuTree<T> buildMenuTree(List<MenuTree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<MenuTree<T>> topNodes = new ArrayList<>();
        nodes.forEach(children -> {
            String pid = children.getParentId();
            if (pid == null || "0".equals(pid)) {
//                children.setParentId(null);
//                children.setId(null);
//                children.setChecked(null);
//                if(StringUtils.isEmpty(children.getJump())){
//                    children.setJump(null);
//                }
                topNodes.add(children);
                return;
            }
            for (MenuTree<T> parent : nodes) {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
//                    children.setParentId(null);
//                    children.setId(null);
//                    children.setChecked(null);
//                    if(StringUtils.isEmpty(children.getJump())){
//                        children.setJump(null);
//                    }
                    parent.getList().add(children);
                    children.setHasParent(true);
                    parent.setHasChild(true);
                    return;
                }
            }
        });

        MenuTree<T> root = new MenuTree<>();
//        root.setId("0");
        root.setParentId("");
//        root.setHasParent(false);
//        root.setHasChild(true);
//        root.setChecked(true);
        root.setList(topNodes);
//        Map<String, Object> state = new HashMap<>(16);
//        root.setState(state);
        return root;
    }

    public static <T> List<DeptTree<T>> buildDeptTree(List<DeptTree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<DeptTree<T>> result = new ArrayList<>();
        nodes.forEach(children -> {
            String pid = children.getParentId();
            if (pid == null || "0".equals(pid)) {
                result.add(children);
                return;
            }
            for (DeptTree<T> n : nodes) {
                String id = n.getId();
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null){
                        n.initChildren();
                    }
                    n.getChildren().add(children);
                    children.setHasParent(true);
                    n.setHasChild(true);
                    return;
                }
            }
        });

        return result;
    }

    public static <T> List<MenuTree<T>> buildList(List<MenuTree<T>> nodes, String idParam) {
        if (nodes == null) {
            return new ArrayList<>();
        }
        List<MenuTree<T>> topNodes = new ArrayList<>();
        nodes.forEach(children -> {
            String pid = children.getParentId();
            if (pid == null || idParam.equals(pid)) {
                topNodes.add(children);
                return;
            }
            nodes.forEach(parent -> {
                String id = parent.getId();
                if (id != null && id.equals(pid)) {
                    parent.getList().add(children);
//                    children.setHasParent(true);
//                    parent.setHasChild(true);
                }
            });
        });
        return topNodes;
    }

    /**
     * 用于构建菜单或部门树
     *
     * @param nodes nodes
     * @param <T>   <T>
     * @return <T> Tree<T>
     */
    public static <T> Tree<T> build(List<Tree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            String pid = node.getParentId();
            if (pid == null || TOP_NODE_ID.equals(pid)) {
                topNodes.add(node);
                return;
            }
            for (Tree<T> n : nodes) {
                String id = n.getId();
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null){
                        n.initChildren();
                    }
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(true);
                    return;
                }
            }
            if (topNodes.isEmpty()){
                topNodes.add(node);
            }
        });


        Tree<T> root = new Tree<>();
        root.setId("0");
        root.setParentId("");
        root.setHasParent(false);
        root.setHasChildren(true);
        root.setChildren(topNodes);
        root.setText("root");
        return root;
    }


//    /**
//     * 构造前端路由
//     *
//     * @param routes routes
//     * @param <T>    T
//     * @return ArrayList<LayuiRouter<T>>
//     */
//    public static <T> ArrayList<LayuiRouter<T>> buildVueRouter(List<LayuiRouter<T>> routes) {
//        if (routes == null) {
//            return null;
//        }
//        List<LayuiRouter<T>> topRoutes = new ArrayList<>();
//        LayuiRouter<T> router = new LayuiRouter<>();
//        router.setName("系统主页");
//        router.setPath("/home");
//        router.setComponent("HomePageView");
//        router.setIcon("home");
//        router.setChildren(null);
//        router.setMeta(new RouterMeta(false, true));
//        topRoutes.add(router);
//
//        routes.forEach(route -> {
//            String parentId = route.getParentId();
//            if (parentId == null || TOP_NODE_ID.equals(parentId)) {
//                topRoutes.add(route);
//                return;
//            }
//            for (LayuiRouter<T> parent : routes) {
//                String id = parent.getId();
//                if (id != null && id.equals(parentId)) {
//                    if (parent.getChildren() == null)
//                        parent.initChildren();
//                    parent.getChildren().add(route);
//                    parent.setHasChildren(true);
//                    route.setHasParent(true);
//                    parent.setHasParent(true);
//                    return;
//                }
//            }
//        });
//        router = new LayuiRouter<>();
//        router.setPath("/profile");
//        router.setName("个人中心");
//        router.setComponent("personal/Profile");
//        router.setIcon("none");
//        router.setMeta(new RouterMeta(true, false));
//        topRoutes.add(router);
//
//        ArrayList<LayuiRouter<T>> list = new ArrayList<>();
//        LayuiRouter<T> root = new LayuiRouter<>();
//        root.setName("主页");
//        root.setComponent("MenuView");
//        root.setIcon("none");
//        root.setPath("/");
//        root.setRedirect("/home");
//        root.setChildren(topRoutes);
//        list.add(root);
//
//        root = new LayuiRouter<>();
//        root.setName("404");
//        root.setComponent("error/404");
//        root.setPath("*");
//        list.add(root);
//
//        return list;
//    }
}