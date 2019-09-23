package cn.edu.usts.task;

import cn.edu.usts.pojo.MetaData;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

import java.util.concurrent.Callable;

public class ResizeTask implements Callable<opencv_core.Mat> {

    private MetaData item;
    private opencv_core.Size size;
    private final String resize_out_path;

    public ResizeTask(MetaData item, opencv_core.Size size, String resize_out_path) {
        this.item = item;
        this.size = size;
        this.resize_out_path = resize_out_path;
    }

    public String getName() {
        String path = item.getFile_name();
        int start = path.lastIndexOf("\\");
        return path.substring(start + 1);
    }

    public opencv_core.Mat call() throws Exception {
        String path = item.getFile_name();
        opencv_core.Mat mat_item = opencv_imgcodecs.imread(path);
        opencv_core.Mat dst = new opencv_core.Mat();
        opencv_imgproc.resize(mat_item, dst, size,0,0,opencv_imgproc.INTER_LINEAR);
        mat_item.release();
        return dst;
    }
}
