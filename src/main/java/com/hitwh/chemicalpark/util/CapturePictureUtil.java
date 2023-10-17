package com.hitwh.chemicalpark.util;

import com.hitwh.chemicalpark.entity.Camera;
import com.hitwh.chemicalpark.entity.Pic;
import com.netsdk.common.SavePath;
import com.netsdk.demo.module.LoginModule;
import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.ToolKits;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class CapturePictureUtil {

    private static NetSDKLib netsdk = NetSDKLib.NETSDK_INSTANCE;
    private static NetSDKLib.NET_DEVICEINFO_Ex m_stDeviceInfo = new NetSDKLib.NET_DEVICEINFO_Ex();
    private static NetSDKLib.LLong m_hLoginHandle = new NetSDKLib.LLong(0);
    private static DisConnect disConnect = new DisConnect();
    private static HaveReConnect haveReConnect = new HaveReConnect();
    private static fCaptureReceiveCB m_CaptureReceiveCB = new fCaptureReceiveCB();
    private static Pic pic = new Pic();

    public Pic start(Camera camera){
        String m_strIp = camera.getIp();//    ip
        int m_nPort = camera.getPort();//    端口
        String m_strUser = camera.getCuser();//   登录名
        String m_strPassword = camera.getCpsd();// 密码
        int nChannelID = camera.getChannelid();//   通道id 默认为0
        int mode = camera.getMode();//       默认 0，当有左上或左下等操作时才会传值 （1-8）
        int quality = camera.getQuality();
        int imagesize = camera.getImagesize();
        int intersnap = camera.getIntersnap();
        int cid = camera.getCid();
       return AsynSnapPicture(cid,m_strIp,m_nPort,m_strUser,m_strPassword,nChannelID,quality,mode,imagesize,intersnap);
    }

    /**
     * 异步抓图方法
     * @param m_strIp 设备ip
     * @param m_nPort 设备端口
     * @param m_strUser 账号
     * @param m_strPassword 密码
     * @param chn 通道id
     * @param mode 请求一帧 默认 0
     */
    public static Pic AsynSnapPicture(int cid,String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int chn, int quality, int mode, int imagesize,int intersnap) {

        // 初始化sdk
        LoginModule.init(disConnect, haveReConnect);

        // 登录
        if (m_hLoginHandle.longValue() == 0) {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }

        int serialNum = 0;
        /// 设置抓图回调: 图片主要在 SnapCallback.getInstance() invoke. 中返回
        netsdk.CLIENT_SetSnapRevCallBack(m_CaptureReceiveCB, null);
        NetSDKLib.SNAP_PARAMS snapParam = new NetSDKLib.SNAP_PARAMS();
        snapParam.Channel = chn; //抓图通道
        snapParam.mode = mode; //表示请求模式
        snapParam.ImageSize = imagesize; // 0：QCIF,1：CIF,2：D1
        snapParam.Quality = quality;
        snapParam.CmdSerial = serialNum ++; // 请求序列号，有效值范围 0~65535，超过范围会被截断
        snapParam.InterSnap = intersnap;
        /// 触发抓图动作
        if (!netsdk.CLIENT_SnapPictureEx(m_hLoginHandle, snapParam , null)) {
            System.err.printf("CLIENT_SnapPictureEx Failed ! Last Error[%x]\n", netsdk.CLIENT_GetLastError());
            return new Pic();
        }

        // 以下保证图片数据的生成
        try {
            synchronized (fCaptureReceiveCB.class) {
        // 默认等待 3s, 防止设备断线时抓拍回调没有被触发，而导致死锁
                fCaptureReceiveCB.class.wait(3000L);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("--> " + Thread.currentThread().getName() + " CLIENT_SnapPictureEx Success." + System.currentTimeMillis());
        logout();
        LoginModule.cleanup();
        return pic;
    }

    /**
     * 同步抓图方法
     * @param m_strIp 设备ip
     * @param m_nPort 设备端口
     * @param m_strUser 账号
     * @param m_strPassword 密码
     * @param chn 通道id
     * @param mode 请求一帧 默认 0
     * @param quality 图片质量
     * @param intersnap 可能是抓拍间隔
     */
    public Pic SyncSnapPicture(String m_strIp, int m_nPort, String m_strUser, String m_strPassword, int chn, int quality, int imagesize,int mode, int intersnap) {

        // 初始化sdk
        LoginModule.init(disConnect, haveReConnect);

        // 登录
        if (m_hLoginHandle.longValue() == 0) {
            login(m_strIp, m_nPort, m_strUser, m_strPassword);
        }

        int serialNum = 0;

        NetSDKLib.NET_IN_SNAP_PIC_TO_FILE_PARAM snapParamIn = new NetSDKLib.NET_IN_SNAP_PIC_TO_FILE_PARAM();
        NetSDKLib.NET_OUT_SNAP_PIC_TO_FILE_PARAM snapParamOut = new NetSDKLib.NET_OUT_SNAP_PIC_TO_FILE_PARAM(1024 * 1024);
        snapParamIn.stuParam.Channel = chn;
        snapParamIn.stuParam.Quality = quality;
        snapParamIn.stuParam.ImageSize = imagesize; // 0：QCIF,1：CIF,2：D1
        snapParamIn.stuParam.mode = mode;// -1:表示停止抓图, 0：表示请求一帧, 1：表示定时发送请求, 2：表示连续请求
        snapParamIn.stuParam.InterSnap = intersnap;
        snapParamIn.stuParam.CmdSerial = serialNum;


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestring = dateFormat.format(new Date());
        final String fileName = "SyncSnapPicture_" + datestring +  ".jpg";
        System.arraycopy(fileName.getBytes(), 0, snapParamIn.szFilePath, 0, fileName.getBytes().length);
        final int timeOut = 3000; // 3 second
        if (!netsdk.CLIENT_SnapPictureToFile(m_hLoginHandle, snapParamIn, snapParamOut, timeOut)) {
            System.err.printf("CLIENT_SnapPictureEx Failed ! Last Error[%x]\n", netsdk.CLIENT_GetLastError());
            return new Pic();
        }
        File file = new File(fileName);

        System.out.println("CLIENT_SnapPictureToFile Success. " + file.getAbsolutePath());
        System.out.println("--> " + Thread.currentThread().getName() + " CLIENT_SnapPictureEx Success." + System.currentTimeMillis());

        //向数据库插入图片的信息
        Pic pic = new Pic();
        //图片地址
        pic.setPicurl(file.getAbsolutePath());
        System.out.println(file.getAbsolutePath());
        //时间格式转换
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime LocalTime = LocalDateTime.parse(datestring,df);
        pic.setDatatime(LocalTime);
        //摄像头ID
        pic.setCid(1);


        //注销和清空
        logout();
        LoginModule.cleanup();
        return pic;
    }


    /**
     * 登录
     */
    public static boolean login(String m_strIp, int m_nPort, String m_strUser, String m_strPassword) {

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
        System.out.println(netsdk.getClass());
        if (m_hLoginHandle.longValue() == 0) {
            System.err.printf("登录失败！\n", m_strIp, m_nPort, ToolKits.getErrorCodePrint());
        } else {
            System.out.println("登录成功： [ " + m_strIp + " ]");
        }
        return m_hLoginHandle.longValue() == 0 ? false : true;
    }

    /**
     * 退出登录
     */

    private static boolean logout() {
        if (m_hLoginHandle.longValue() == 0) {
            return false;
        }
        boolean bRet = netsdk.CLIENT_Logout(m_hLoginHandle);
        if (bRet) {
            m_hLoginHandle.setValue(0);
        }
        return bRet;
    }

    /**
     * 抓图方法
     *
     * @param chn 通道号
     * @param mode 默认 0
     * @param interval 默认0
     * @return 成功返回ture
     */
    private static boolean snapPicture(int chn, int mode, int interval) {
        setSnapRevCallBack(m_CaptureReceiveCB);
        // send caputre picture command to device
        NetSDKLib.SNAP_PARAMS stuSnapParams = new NetSDKLib.SNAP_PARAMS();
        stuSnapParams.Channel = chn; // channel
        stuSnapParams.mode = mode; // capture picture mode
        stuSnapParams.Quality = 3; // picture quality
        stuSnapParams.InterSnap = interval; // timer capture picture time interval
        stuSnapParams.CmdSerial = 0; // request serial
        IntByReference reserved = new IntByReference(0);
        if (!netsdk.CLIENT_SnapPictureEx(m_hLoginHandle, stuSnapParams, reserved)) {
            System.err.printf("CLIENT_SnapPictureEx Failed!" + ToolKits.getErrorCodePrint());
            return false;
        } else {
            System.out.println("CLIENT_SnapPictureEx success");
        }
        return true;
    }

    /**
     * 保存图片
     */
    private static class fCaptureReceiveCB implements NetSDKLib.fSnapRev {

        BufferedImage bufferedImage = null;


        public void invoke(NetSDKLib.LLong lLoginID, Pointer pBuf, int RevLen, int EncodeType, int CmdSerial, Pointer dwUser) {

            if (pBuf != null && RevLen > 0) {
                String strFileName = SavePath.getSavePath().getSaveCapturePath();
                String fileName = strFileName.replace("/","\\");
                fileName = fileName.replace(".\\","");
                pic.setPicurl(fileName);
                //截取出图片的名字
                String name1 = strFileName.substring(0,strFileName.indexOf("/"));
                String name2 = strFileName.substring(name1.length()+1,strFileName.length());
                String name3 = name2.substring(0,name2.indexOf("."));
                pic.setPicname(name3);
                System.out.println("strFileName = " + strFileName);
                byte[] buf = pBuf.getByteArray(0, RevLen);
                ByteArrayInputStream byteArrInput = new ByteArrayInputStream(buf);

                try {
                    bufferedImage = ImageIO.read(byteArrInput);
                    if (bufferedImage == null) {
                    }
                    ImageIO.write(bufferedImage, "jpg", new File(strFileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 抓图回调函数
     *
     * @param cbSnapReceive
     */
    private static void setSnapRevCallBack(NetSDKLib.fSnapRev cbSnapReceive) {
        netsdk.CLIENT_SetSnapRevCallBack(cbSnapReceive, null);

    }

    //设备断线回调: 通过 CLIENT_Init 设置该回调函数，当设备出现断线时，SDK会调用该函数
    private static class DisConnect implements NetSDKLib.fDisConnect {
        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("Device[%s] Port[%d] DisConnect!\n", pchDVRIP, nDVRPort);
        }

    }

    // 网络连接恢复，设备重连成功回调
    //通过 CLIENT_SetAutoReconnect 设置该回调函数，当已断线的设备重连成功时，SDK会调用该函数

    private static class HaveReConnect implements NetSDKLib.fHaveReConnect {
        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("ReConnect Device[%s] Port[%d]\n", pchDVRIP, nDVRPort);
        }
    }
}