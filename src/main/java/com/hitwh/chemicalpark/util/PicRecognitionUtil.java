package com.hitwh.chemicalpark.util;

import com.hitwh.chemicalpark.entity.Alert;
import com.hitwh.chemicalpark.entity.Pic;
import org.springframework.scheduling.annotation.Async;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;


public class PicRecognitionUtil {
    public Alert YOLO(Pic pic) throws IOException, InterruptedException {
        try {
            FileWriter write = new FileWriter("D:\\YOLO.bat");
            BufferedWriter bw = new BufferedWriter(write);
            //开启anaconda环境
            bw.write("C:\\ProgramData\\anaconda\\Scripts\\activate.bat && G:&&");
            bw.write("cd Yolov5-Fire-Detection\\yolov5 &&");
            bw.write("python detect.py --source " + pic.getPicurl() + " --weights runs/train/exp/weights/best.pt --save-txt --save-conf --conf 0.3 --view-img --name detect --exist-ok &&");
            bw.write("taskkill /f /im cmd.exe &&");
            bw.write("exit");
            bw.close();
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Runtime run = Runtime.getRuntime();
        String cmd = "cmd /c start D:\\YOLO.bat";

        try {
            //执行图像识别
            Process process = run.exec(cmd);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); // 获取标准输出流
            String line;
            while ((line = reader.readLine()) != null) { // 读取输出
                System.out.println(line);
            }
            System.out.println("开始图像识别等待");
            TimeUnit.SECONDS.sleep(10);
            //获得标签txt
            String labelpath = "G:\\ideaproject\\pjk-master\\Yolov5-Fire-Detection\\yolov5\\runs\\detect\\detect\\labels\\" + pic.getPicname() + ".txt";
            System.out.println(labelpath);
            File labels = new File(labelpath);
            //判断输出的结果txt是否存在，存在则读取置信度
            if (labels.exists()) {
                FileInputStream fis = new FileInputStream(labels);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                ArrayList<Float> arr = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    //置信度的位置
                    String[] parts = line.split(" "); // 将每一行数据拆分成数字数组
                    float con = Float.parseFloat(parts[parts.length - 1]); // 提取最后一个数字
                    arr.add(con);
                    System.out.println("置信度是"+con);
                }

                //阈值算法
                float max = Collections.max(arr);

                if (max >= 0.3) {
                    Alert firealert = new Alert();
                    firealert.setAlerttype("图像火警");
                    firealert.setCid(pic.getCid());
                    firealert.setPid(pic.getPid());
                    firealert.setAlerttime(LocalDateTime.now());
                    firealert.setAlertdata(Float.toString(max));
                    if (max >= 0.8) {
                        firealert.setAlertlevel("高报");
                    }
                    if (max < 0.8 || max >= 0.5) {
                        firealert.setAlertlevel("预警");
                    }
                    if (max < 0.5) {
                        firealert.setAlertlevel("预报");
                    }
                    return firealert;
                }
            }else{
                System.out.println("未读取到文件");
                process.destroy();
            }
        }catch (Exception e) {

        }
        return null;
    }
}
