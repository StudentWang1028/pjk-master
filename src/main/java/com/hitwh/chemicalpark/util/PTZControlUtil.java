package com.hitwh.chemicalpark.util;

import com.netsdk.demo.module.LoginModule;
import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.ToolKits;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;

//这个类并没有被系统使用

/**
 * @Author lycj
 * @Description 云台控制工具类
 * @Date 2022/3/30 14:43
 * @Version 1.0
 */
@Slf4j
public class PTZControlUtil
{
    // 初始化sdk
    public static NetSDKLib netsdk = NetSDKLib.NETSDK_INSTANCE;

    // 设备信息
    private static NetSDKLib.NET_DEVICEINFO_Ex m_stDeviceInfo = new NetSDKLib.NET_DEVICEINFO_Ex();

    // 登陆句柄
    private static NetSDKLib.LLong m_hLoginHandle = new NetSDKLib.LLong(0);

    // 网络断线处理
    private static DisConnect disConnect = new DisConnect();

    // 设备连接恢复，实现设备连接恢复接口
    private static HaveReConnect haveReConnect = new HaveReConnect();



    /**
     * 云台控制
     * 向上移动
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam1       默认 0，当有左上或左下等操作时才会传值 （1-8）
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static void upControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam1, int lParam2)
    {


        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始向上移动，若超过角度则会变为左右移动
        if (m_hLoginHandle.longValue() != 0)
        {

            startUpControl(nChannelID, lParam1, lParam2);
        }
        log.info("操作完成");
        // 停止移动
       stopUpControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源


    }

    /**
     * 云台控制
     * 向下移动
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam1       默认 0，当有左上或左下等操作时才会传值 （1-8）
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean downControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam1, int lParam2)
    {
        boolean result;

        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始向下移动，若超过角度则会变为左右移动
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startDownControl(nChannelID, lParam1, lParam2);
        }
        log.info("操作完成");
        // 停止移动
        result = stopDownControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();

        return result;
    }

    /**
     * 云台控制
     * 向左移动
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam1       默认 0，当有左上或左下等操作时才会传值 （1-8）
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean leftControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam1, int lParam2) throws InterruptedException {
        boolean result;

        // 初始化
        init(disConnect,haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始向左移动，若超过角度则会变为左右移动
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startLeftControl(nChannelID, lParam1, lParam2);
        }
        log.info("操作完成");
        Thread.sleep(8000);
//         停止移动
        result = stopLeftControl(nChannelID);
//         退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();
        return true;

    }

    /**
     * 云台控制
     * 向右移动
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam1       默认 0，当有左上或左下等操作时才会传值 （1-8）
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean rightControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam1, int lParam2)
    {
        boolean result;

        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始向右移动，若超过角度则会变为左右移动
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startRightControl(nChannelID, lParam1, lParam2);
        }
        log.info("操作完成");
        // 停止移动
        result = stopRightControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();

        return result;
    }

    /**
     * 云台控制
     * 向左上移动
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam1       默认 0，当有左上或左下等操作时才会传值 （1-8）
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean leftUpControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam1, int lParam2)
    {
        boolean result;

        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始向左上移动，若超过角度则会变为左右移动
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startLeftUpControl(nChannelID, lParam1, lParam2);
        }
        log.info("操作完成");
        // 停止移动
        result = stopLeftUpControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();

        return result;
    }

    /**
     * 云台控制
     * 向右上移动
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam1       默认 0，当有左上或左下等操作时才会传值 （1-8）
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean rightUpControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam1, int lParam2)
    {
        boolean result;

        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始向右上移动，若超过角度则会变为左右移动
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startRightUpControl(nChannelID, lParam1, lParam2);
        }
        log.info("操作完成");
        // 停止移动
        result = stopRightUpControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();

        return result;
    }

    /**
     * 云台控制
     * 向左下移动
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam1       默认 0，当有左上或左下等操作时才会传值 （1-8）
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean leftDownControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam1, int lParam2)
    {
        boolean result;

        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始向左下移动，若超过角度则会变为左右移动
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startLeftDownControl(nChannelID, lParam1, lParam2);
        }
        log.info("操作完成");
        // 停止移动
        result = stopLeftDownControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();

        return result;
    }

    /**
     * 云台控制
     * 向右下移动
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam1       默认 0，当有左上或左下等操作时才会传值 （1-8）
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean rightDownControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam1, int lParam2)
    {
        boolean result;

        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始向右下移动，若超过角度则会变为左右移动
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startRightDownControl(nChannelID, lParam1, lParam2);
        }
        log.info("操作完成");
        // 停止移动
        result = stopRightDownControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();

        return result;
    }

    /**
     * 云台控制
     * 变倍+
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean zoomAddControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam2)
    {
        boolean result;

        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始变倍+
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startZoomAddControl(nChannelID, lParam2);
        }
        log.info("操作完成");
        // 停止变倍
        result = stopZoomAddControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();

        return result;
    }

    /**
     * 云台控制
     * 变倍-
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean zoomDecControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam2)
    {
        boolean result;

        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始变倍-
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startZoomDecControl(nChannelID, lParam2);
        }
        log.info("操作完成");
        // 停止变倍
        result = stopZoomDecControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();

        return result;
    }

    /**
     * 云台控制
     * 变焦+
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean focusAddControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam2)
    {
        boolean result;

        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始变焦+
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startFocusAddControl(nChannelID, lParam2);
        }
        log.info("操作完成");
        // 停止变焦
        result = stopFocusAddControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();

        return result;
    }

    /**
     * 云台控制
     * 变焦-
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean focusDecControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam2)
    {
        boolean result;

        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始变焦-
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startFocusDecControl(nChannelID, lParam2);
        }
        log.info("操作完成");
        // 停止变焦
        result = stopFocusDecControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();

        return result;
    }

    /**
     * 云台控制
     * 光圈+
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean irisAddControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam2)
    {
        boolean result;

        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始变焦+
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startIrisAddControl(nChannelID, lParam2);
        }
        log.info("操作完成");
        // 停止变焦
        result = stopIrisAddControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();

        return result;
    }

    /**
     * 云台控制
     * 光圈-
     * @param m_strIp       ip
     * @param m_nPort       端口
     * @param m_strUser     登录名
     * @param m_strPassword 密码
     * @param nChannelID    通道id 默认为0
     * @param lParam2       垂直/水平 移动速度 （1-8）
     */
    public static boolean irisDecControlPtz(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int nChannelID, int lParam2)
    {
        boolean result;

        // 初始化
        LoginModule.init(disConnect, haveReConnect);
        // 若未登录，先登录。
        if (m_hLoginHandle.longValue() == 0)
        {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }
        // 开始变焦-
        if (m_hLoginHandle.longValue() != 0)
        {
            result = startIrisDecControl(nChannelID, lParam2);
        }
        log.info("操作完成");
        // 停止变焦
        result = stopIrisDecControl(nChannelID);
        // 退出
        logout();
        log.info("退出登录...");
        // 释放资源
        LoginModule.cleanup();

        return result;
    }

    // 向上
    private static boolean startUpControl(int nChannelID, int lParam1, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_PTZ_ControlType.NET_PTZ_UP_CONTROL,
                    lParam1, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }
    }

    private static boolean stopUpControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_UP_CONTROL,
                0, 0, 0, 1);
    }


    /**
     * 向下
     */
    public static boolean startDownControl(int nChannelID, int lParam1, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_PTZ_ControlType.NET_PTZ_DOWN_CONTROL,
                    lParam1, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }
    }

    public static boolean stopDownControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_DOWN_CONTROL,
                0, 0, 0, 1);
    }

    /**
     * 向左
     */
    public static boolean startLeftControl(int nChannelID, int lParam1, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_PTZ_ControlType.NET_PTZ_LEFT_CONTROL,
                    lParam1, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }
    }

    public static boolean stopLeftControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_LEFT_CONTROL,
                0, 0, 0, 1);
    }

    /**
     * 向右
     */
    public static boolean startRightControl(int nChannelID, int lParam1, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_PTZ_ControlType.NET_PTZ_RIGHT_CONTROL,
                    lParam1, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }
    }

    public static boolean stopRightControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_RIGHT_CONTROL,
                0, 0, 0, 1);
    }

    /**
     * 向左上
     */
    public static boolean startLeftUpControl(int nChannelID, int lParam1, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_EXTPTZ_ControlType.NET_EXTPTZ_LEFTTOP,
                    lParam1, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }
    }

    public static boolean stopLeftUpControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_EXTPTZ_ControlType.NET_EXTPTZ_LEFTTOP,
                0, 0, 0, 1);
    }

    /**
     * 向右上
     */
    public static boolean startRightUpControl(int nChannelID, int lParam1, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_EXTPTZ_ControlType.NET_EXTPTZ_RIGHTTOP,
                    lParam1, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }
    }

    public static boolean stopRightUpControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_EXTPTZ_ControlType.NET_EXTPTZ_RIGHTTOP,
                0, 0, 0, 1);
    }

    /**
     * 向左下
     */
    public static boolean startLeftDownControl(int nChannelID, int lParam1, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_EXTPTZ_ControlType.NET_EXTPTZ_LEFTDOWN,
                    lParam1, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }
    }

    public static boolean stopLeftDownControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_EXTPTZ_ControlType.NET_EXTPTZ_LEFTDOWN,
                0, 0, 0, 1);
    }

    /**
     * 向右下
     */
    public static boolean startRightDownControl(int nChannelID, int lParam1, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_EXTPTZ_ControlType.NET_EXTPTZ_RIGHTDOWN,
                    lParam1, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }
    }

    public static boolean stopRightDownControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_EXTPTZ_ControlType.NET_EXTPTZ_RIGHTDOWN,
                0, 0, 0, 1);
    }

    /**
     * 变倍+
     */
    public static boolean startZoomAddControl(int nChannelID, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_PTZ_ControlType.NET_PTZ_ZOOM_ADD_CONTROL,
                    0, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }

    }

    public static boolean stopZoomAddControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_ZOOM_ADD_CONTROL,
                0, 0, 0, 1);
    }

    /**
     * 变倍-
     */
    public static boolean startZoomDecControl(int nChannelID, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_PTZ_ControlType.NET_PTZ_ZOOM_DEC_CONTROL,
                    0, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }

    }

    public static boolean stopZoomDecControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_ZOOM_DEC_CONTROL,
                0, 0, 0, 1);
    }

    /**
     * 变焦+
     */
    public static boolean startFocusAddControl(int nChannelID, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_PTZ_ControlType.NET_PTZ_FOCUS_ADD_CONTROL,
                    0, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }

    }

    public static boolean stopFocusAddControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_FOCUS_ADD_CONTROL,
                0, 0, 0, 1);
    }

    /**
     * 变焦-
     */
    public static boolean startFocusDecControl(int nChannelID, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_PTZ_ControlType.NET_PTZ_FOCUS_DEC_CONTROL,
                    0, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }

    }

    public static boolean stopFocusDecControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_FOCUS_DEC_CONTROL,
                0, 0, 0, 1);
    }

    /**
     * 光圈+
     */
    public static boolean startIrisAddControl(int nChannelID, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_PTZ_ControlType.NET_PTZ_APERTURE_ADD_CONTROL,
                    0, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }

    }

    public static boolean stopIrisAddControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_APERTURE_ADD_CONTROL,
                0, 0, 0, 1);
    }

    /**
     * 光圈-
     */
    public static boolean startIrisDecControl(int nChannelID, int lParam2)
    {
        if (m_hLoginHandle.longValue() != 0)
        {
            return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                    NetSDKLib.NET_PTZ_ControlType.NET_PTZ_APERTURE_DEC_CONTROL,
                    0, lParam2, 0, 0);
        } else
        {
            log.error("登录句柄不存在！");
            return false;
        }
    }

    public static boolean stopIrisDecControl(int nChannelID)
    {
        return netsdk.CLIENT_DHPTZControlEx(m_hLoginHandle, nChannelID,
                NetSDKLib.NET_PTZ_ControlType.NET_PTZ_APERTURE_DEC_CONTROL,
                0, 0, 0, 1);
    }


    /**
     * 登录
     * @param m_strIp       ip
     * @param m_nPort       端口号
     * @param m_strUser     账号
     * @param m_strPassword 密码
     * @return 成功则 true
     */
    public static boolean login(String m_strIp, int m_nPort, String m_strUser, String m_strPassword)
    {
        //IntByReference nError = new IntByReference(0);
        //入参
        NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY pstInParam = new NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY();
        pstInParam.nPort = m_nPort;
        pstInParam.szIP = m_strIp.getBytes();
        pstInParam.szPassword = m_strPassword.getBytes();
        pstInParam.szUserName = m_strUser.getBytes();
        //出参
        NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY pstOutParam = new NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY();
        pstOutParam.stuDeviceInfo = m_stDeviceInfo;
        m_hLoginHandle = netsdk.CLIENT_LoginWithHighLevelSecurity(pstInParam, pstOutParam);
        log.info(netsdk.getClass().toString());
        if (m_hLoginHandle.longValue() == 0)
        {
            log.error("登录失败！\n", m_strIp, m_nPort, ToolKits.getErrorCodePrint());
        } else
        {
            log.info("登录成功： [ " + m_strIp + " ]");
        }

        return m_hLoginHandle.longValue() == 0 ? false : true;
    }

    /**
     * 退出登录
     */
    private static void logout()
    {
        if (m_hLoginHandle.longValue() == 0)
        {
            return;
        }

        boolean bRet = netsdk.CLIENT_Logout(m_hLoginHandle);
        if (bRet)
        {
            m_hLoginHandle.setValue(0);
        }

    }

    // 设备断线回调: 通过 CLIENT_Init 设置该回调函数，当设备出现断线时，SDK会调用该函数
    private static class DisConnect implements NetSDKLib.fDisConnect
    {
        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser)
        {
            System.out.printf("Device[%s] Port[%d] DisConnect!\n", pchDVRIP, nDVRPort);
        }
    }

    // 网络连接恢复，设备重连成功回调
    // 通过 CLIENT_SetAutoReconnect 设置该回调函数，当已断线的设备重连成功时，SDK会调用该函数
    private static class HaveReConnect implements NetSDKLib.fHaveReConnect
    {
        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser)
        {
            System.out.printf("ReConnect Device[%s] Port[%d]\n", pchDVRIP, nDVRPort);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String m_strIp = "192.168.1.108";//    ip
        int m_nPort = 37777;//    端口
        String m_strUser = "admin";//   登录名
        String m_strPassword = "2001-05-16";// 密码
        int nChannelID = 0;//   通道id 默认为0
        int lParam1 = 0;//       默认 0，当有左上或左下等操作时才会传值 （1-8）
        int lParam2 = 2;//      垂直/水平 移动速度 （1-8）
        leftControlPtz(m_strIp, m_nPort,m_strUser,m_strPassword,nChannelID,lParam1,lParam2);

    }

    private static boolean bInit = false;
    private static boolean bLogopen = false;
    //初始化
    public static boolean init(NetSDKLib.fDisConnect disConnect, NetSDKLib.fHaveReConnect haveReConnect) {
        bInit = netsdk.CLIENT_Init(disConnect, null);
        if (!bInit) {
            System.out.println("Initialize SDK failed");
            return false;
        }

        // 设置断线重连回调接口，设置过断线重连成功回调函数后，当设备出现断线情况，SDK内部会自动进行重连操作
        // 此操作为可选操作，但建议用户进行设置
        netsdk.CLIENT_SetAutoReconnect(haveReConnect, null);

        //设置登录超时时间和尝试次数，可选
        int waitTime = 5000; //登录请求响应超时时间设置为 5S
        int tryTimes = 1; //登录时尝试建立链接 1 次
        netsdk.CLIENT_SetConnectTime(waitTime, tryTimes);
        // 设置更多网络参数， NET_PARAM 的 nWaittime，nConnectTryNum成员与CLIENT_SetConnectTime
        // 接口设置的登录设备超时时间和尝试次数意义相同,可选
        NetSDKLib.NET_PARAM netParam = new NetSDKLib.NET_PARAM();
        netParam.nConnectTime = 10000; // 登录时尝试建立链接的超时时间
        netParam.nGetConnInfoTime = 3000; // 设置子连接的超时时间
        netsdk.CLIENT_SetNetworkParam(netParam);
        return true;
    }

    public static void cleanup() {
        if (bLogopen) {
            netsdk.CLIENT_LogClose();
        }
        if (bInit) {
            netsdk.CLIENT_Cleanup();
        }
    }



}
