package cn.edu.usts.task;

import cn.edu.usts.pojo.MetaData;
import cn.edu.usts.pojo.Size;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class FetchInfoTask implements Callable<MetaData> {

    private File item;

    public FetchInfoTask(File file) {
        this.item = file;
    }

    public MetaData call() {
        String path = item.getAbsolutePath();
        String format = item.getName().split("\\.")[1];
        opencv_core.Mat mat_item = opencv_imgcodecs.imread(path);
        opencv_core.Size cv_size = mat_item.size();
        Size size = Size.builder().width(cv_size.width()).height(cv_size.height()).build();
        MetaData data = MetaData.builder().file_name(path).size(size).format(format).build();
        return data;
    }
}
