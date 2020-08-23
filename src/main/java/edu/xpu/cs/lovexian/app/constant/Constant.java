package edu.xpu.cs.lovexian.app.constant;

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
public class Constant {
    private final static Integer DEL_STATE = 0;

    @Getter
    public enum ProgressState{
        noprogress(0,"未处理"),
        progressing(100,"处理中"),
        progressfinish(200,"处理成功"),
        progressfail(400,"处理失败");

        private String desc;
        private int code;

        ProgressState(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public static ProgressState codeOf(int code){
            for(ProgressState dt:values()){
                if(dt.code == code){
                    return dt;
                }
            }
            throw new RuntimeException("未找到对应的枚举~");
        }
    }

    @Getter
    public enum DelStateEnum{
        deleted(1,"已删除"),
        nodel(0,"未删除");

        private String desc;
        private int code;

        DelStateEnum(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public static DelStateEnum codeOf(int code){
            for(DelStateEnum dt:values()){
                if(dt.code == code){
                    return dt;
                }
            }
            throw new RuntimeException("未找到对应的枚举~");
        }
    }

    @Getter
    public enum CheckStateEnum{
        draft(100,"草稿"),
        uncheck(0,"未审核"),
        checked(1,"已审核"),
        refuse(2,"审核拒绝");

        private String desc;
        private int code;

        CheckStateEnum(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public static CheckStateEnum codeOf(int code){
            for(CheckStateEnum dt:values()){
                if(dt.code == code){
                    return dt;
                }
            }
            throw new RuntimeException("未找到对应的枚举~");
        }
    }

    @Getter
    public enum ShowState{
        show(1,"展示"),
        noshow(0,"不展示");

        private String desc;
        private int code;

        ShowState(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public static ShowState codeOf(int code){
            for(ShowState dt:values()){
                if(dt.code == code){
                    return dt;
                }
            }
            throw new RuntimeException("未找到对应的枚举~");
        }
    }
}
