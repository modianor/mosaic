package cn.edu.usts.preprocess;

import cn.edu.usts.pojo.MetaData;
import cn.edu.usts.task.ResizeTask;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ResizeMetaData {
    private static final int TOTAL_THREADS = 10;//线程数

    private String resize_out_path;
    private String meta_data_path;
    private opencv_core.Size size;

    public ResizeMetaData(String meta_data_path, String resize_out_path, opencv_core.Size size) {
        this.resize_out_path = resize_out_path;
        this.meta_data_path = meta_data_path;
        this.size = size;
    }

    private void do_preprocess() throws IOException, ExecutionException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        File meta_data = new File(meta_data_path);
        JavaType javaType = mapper.getTypeFactory().constructParametricType(LinkedList.class, MetaData.class);
        LinkedList<MetaData> data_list = (LinkedList<MetaData>) mapper.readValue(meta_data, javaType);

        ExecutorService taskExecutor = Executors.newFixedThreadPool(TOTAL_THREADS);
        for (int i = 0; i < data_list.size(); i++) {
            MetaData item = data_list.get(i);
            ResizeTask task = new ResizeTask(item, size, resize_out_path);
            Future<opencv_core.Mat> submit = taskExecutor.submit(task);
            opencv_core.Mat dst = submit.get();
            String path = resize_out_path + getName(item);
            opencv_imgcodecs.imwrite(path, dst);
            dst.release();
            System.out.println("当前系统已完成预处理： " + (i + 1) + "张图片");
        }

        size.deallocate();
        taskExecutor.shutdown();
    }

    public String getName(MetaData item) {
        String path = item.getFile_name();
        int start = path.lastIndexOf("\\");
        return path.substring(start + 1);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        String meta_data_path = "meta_data.txt";
        String resize_out_path = "G:\\group\\small\\";
        opencv_core.Size size = new opencv_core.Size(80, 90);

        ResizeMetaData size_data = new ResizeMetaData(meta_data_path, resize_out_path, size);
        size_data.do_preprocess();
    }
}
