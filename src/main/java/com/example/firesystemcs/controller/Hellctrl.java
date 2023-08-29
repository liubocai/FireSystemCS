package com.example.firesystemcs.controller;

import com.example.firesystemcs.dao.EquipmentDao;
import com.example.firesystemcs.dao.PosDao;
import com.example.firesystemcs.dao.RadioDao;
import com.example.firesystemcs.dao.VideoDao;
import com.example.firesystemcs.entity.*;
import com.example.firesystemcs.utils.Recorder;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600) //origins是指允许哪个网络来访问资源
@RestController
public class Hellctrl {


    @Resource
    private RadioDao radioDao;
    @Resource
    private EquipmentDao equipDao;
    @Resource
    private PosDao posDao;
    @Resource
    private VideoDao videoDao;

    @RequestMapping("/test")
    public String Hello(@RequestParam("name") String name){
//        Radio temp = new Radio("57", "http://192.168.1.57", "disconnect");
//        radioDao.addARadio(temp);
        return name+"1";
    }

    @RequestMapping(value = "/getallequip")
    public List<Equipment> get(){
        Map<String, Object> map = new HashMap<String, Object>();
        List<Equipment> result = equipDao.getAllEquip();
        return result;
    }

    @RequestMapping(value = "/addpos")
    public void addpos(@RequestParam("eid")String eid,
                       @RequestParam("rid")String rid,
                       @RequestParam("lat")String lat,
                       @RequestParam("lon")String lon,
                       @RequestParam("height")String height){
        Date date=new Date();
        String nowTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        Pos pos = new Pos(eid, rid, nowTime, lat, lon, height);
        posDao.addPos(pos);
    }

    @RequestMapping(value = "/getposbyeid")
    public Pos getposbyeid(@RequestParam("eid")String eid){
        Pos result = posDao.getByeid(eid);
        return result;
    }

    //根据设备的eid获取其当前位置，并将其作为一条Pos记录存入库内
    @RequestMapping(value = "/geteidnowpos")
    public Pos geteidnowpos(@RequestParam("eid")String eid){
        RestTemplate restTemplate = new RestTemplate();
        String ip = equipDao.getIpByeid(eid);
        String url = "http://"+ip+":8000";
        Resgps result = restTemplate.getForObject(url, Resgps.class);
        String rid = equipDao.getRidByeid(eid);
        Date date=new Date();
        String nowTime_db=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        Pos pos = new Pos(eid, rid, nowTime_db, result.getLatitude(), result.getLongitude(), result.getAltitude());
        posDao.addPos(pos);
        return pos;
    }

    @RequestMapping(value = "/recordvideo")
    public void recordvideo(){
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        Date date=new Date();
        String nowTime=new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        //路径名加上文件名加上后缀名 = 整个文件下载路径
//        String fullPathName = downloadPath+fileName+"."+SuffixName;
        String fullPathName = "D://"+nowTime+"out.ts";

        try {
            // 1.获取连接对象
            URL url = new URL("http://192.168.10.220:8080/live/livestream.flv");
            // 获取链接对象，就是靠这个对象来获取流
            connection = (HttpURLConnection) url.openConnection();
            // Range代表读取的范围，bytes=0-代表从0字节到最大字节，意味着读取所有资源
            connection.setRequestProperty("Range", "bytes=0-");
            // 与网页建立链接，链接成功后就可以获得流；
            connection.connect();
            // 如果建立链接返回的相应代码是200到300间就为成功，否则链接失败,结束函数
            if (connection.getResponseCode() / 100 != 2) {
                System.out.println("连接失败...");
                return;
            }
            // 2.获取连接对象的流
            inputStream = connection.getInputStream();
            // 已下载的大小 下载进度
            int downloaded = 0;
            // 总文件的大小
            int fileSize = 10000000;
//            int fileSize = connection.getContentLength();
            // 3.把资源写入文件

            randomAccessFile = new RandomAccessFile(fullPathName, "rw");
            while (downloaded < fileSize) {
                // 3.1设置缓存流的大小
                //判断当前剩余的下载大小是否大于缓存之，如果不大于就把缓存的大小设为剩余的。
                byte[] buffer = null;
                if (fileSize - downloaded >= 1000000) {
                    buffer = new byte[1000000];
                } else {
                    buffer = new byte[fileSize - downloaded];
                }
                // 3.2把每一次缓存的数据写入文件
                int read = -1;
                int currentDownload = 0;
                long startTime = System.currentTimeMillis();
                // 这段代码是按照缓存的大小，读写该大小的字节。然后循环依次写入缓存的大小（用让硬件频繁的写入，可以提高效率和保护硬盘）
                while (currentDownload < buffer.length) {
                    read = inputStream.read();
                    buffer[currentDownload++] = (byte) read;
                }
                long endTime = System.currentTimeMillis();
                double speed = 0.0; //下载速度
                if (endTime - startTime > 0) {
                    speed = currentDownload / 1024.0 / ((double) (endTime - startTime) / 1000);
                }
                randomAccessFile.write(buffer);
                downloaded += currentDownload;
                randomAccessFile.seek(downloaded);
                System.out.printf(fullPathName+"下载了进度:%.2f%%,下载速度：%.1fkb/s(%.1fM/s)%n", downloaded * 1.0 / fileSize * 10000 / 100,
                        speed, speed / 1000);
            }

//        } catch (MalformedURLException e) {// 具体的异常放到前面
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                //关闭资源、连接
                connection.disconnect();
                inputStream.close();
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/recordbycv")
    public void recordbyjavacv(@RequestParam("eid")String eid){
        Long times_sec = 300L;// 停止录制时长 0为不限制时长，单位为秒
        Date date=new Date();
        String nowTime=new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        String nowTime_db=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        String downloadPath = "D://"; //保存路径
        String file_name = eid + "_" +nowTime; //文件命名规则，设备编号+开始录制时间
        String file_format = "mp4";//录制的文件格式
        String out_file_path = downloadPath + file_name + "." + file_format; //"D://rtmpjavacv.mp4";//输出路径
        Equipment equipment = equipDao.getEquipByeid(eid);
        String ip = equipment.getIp();
        String stream_url = "rtmp://"+ip+"/live/livestream";// 流地址 例如：rtmp://58.200.131.2:1935/livetv/hunantv 湖南卫视
        boolean is_audio = false;//是否录制声音
        Recorder recorder1 = new Recorder();
        recorder1.stream_url = stream_url;
        recorder1.out_file_path = out_file_path;
        recorder1.is_audio = is_audio;
        recorder1.times_sec = times_sec;
        new Thread(recorder1).start();
        videoDao.addAvid(new Video(eid, nowTime_db, out_file_path));
//        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(stream_url);
//        FFmpegFrameRecorder recorder = null;
//        try {
//            grabber.start();
//            Frame frame = grabber.grabFrame();
//            if (frame != null) {
//                //保存到本地的文件
//                File outFile = new File(out_file_path);
//                // 如果文件不存在或者不是一个文件 则根据文件的路径创建一个文件
//                if (out_file_path.isEmpty() || !outFile.exists() || outFile.isFile()) {
//                    outFile.createNewFile();
//                } else {
//                    System.out.println("输出文件无法创建");
//                    return;
//                }
//                // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制）
//                recorder = new FFmpegFrameRecorder(out_file_path, frame.imageWidth, frame.imageHeight, is_audio ? 1 : 0);
//                recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);//直播流格式
//                recorder.setFormat(file_format);//录制的视频格式
//                recorder.setFrameRate(25);//帧数
//                recorder.start();//开始录制
//                // 计算结束时间
//                long endTime = System.currentTimeMillis() + times_sec * 1000;
//                // 如果没有到录制结束时间并且获取到了下一帧则继续录制
//                while ((System.currentTimeMillis() < endTime) && (frame != null)) {
//                    recorder.record(frame);//录制
//                    frame = grabber.grabFrame();//获取下一帧
//                }
//                recorder.record(frame);
//            }
//        } catch (FrameGrabber.Exception e) {
//            e.printStackTrace();
//        } catch (FrameRecorder.Exception e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            //停止录制
//            if (null != grabber) {
//                try {
//                    grabber.stop();
//                } catch (FrameGrabber.Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (recorder != null) {
//                try {
//                    recorder.stop();
//                } catch (FrameRecorder.Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            System.out.println("录制完成，录制时长：" + times_sec + "秒(0为没有限制录制时长)");
//        }
    }
}
