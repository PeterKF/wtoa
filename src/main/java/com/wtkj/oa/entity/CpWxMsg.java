package com.wtkj.oa.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_CpWxMsg")
public class CpWxMsg extends Model<CpWxMsg> {
    private static final long serialVersionUID = -6934276701861117261L;

    //企业ID
    @TableField("corp_id")
    private String corpid;

    //应用的凭证密钥
    @TableField("corp_secret")
    private String corpsecret;

    //成员ID列表（多个接收者用‘|’分隔，最多支持1000个）
    @TableField("to_user")
    private String touser;

    //部门ID列表，多个接收者用‘|’分隔，最多支持100个
    @TableField("to_part")
    private String topart;

    //标签ID列表，多个接收者用‘|’分隔，最多支持100个
    @TableField("to_tag")
    private String totag;

    //消息类型，此时固定为：text
    @TableField("msg_type")
    private String msgtype;

    //企业应用的id
    @TableField("agent_Id")
    private String agentid;

    @TableField(exist = false)
    private Object text;

    @TableField(exist = false)
    private String content;

    //表示是否是保密消息
    @TableField("safe")
    private Integer safe = 0;

    //表示是否开启id转译，0表示否，1表示是
    @TableField("enableId_trans")
    private Integer enable_id_trans = 0;

    //表示是否开启重复消息检查
    @TableField("enable_duplicate_check")
    private Integer enable_duplicate_check = 0;

    //表示是否重复消息检查的时间间隔，默认1800s
    @TableField("duplicate_check_interval")
    private Integer duplicate_check_interval = 1800;

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getCorpsecret() {
        return corpsecret;
    }

    public void setCorpsecret(String corpsecret) {
        this.corpsecret = corpsecret;
    }

    public Object getText() {
        return text;
    }

    public void setText(Object text) {
        this.text = text;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


