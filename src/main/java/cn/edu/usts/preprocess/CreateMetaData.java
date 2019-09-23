package cn.edu.usts.preprocess;

import cn.edu.usts.pojo.MetaData;
import cn.edu.usts.task.FetchInfoTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CreateMetaData {

    private static final int TOTAL_THREADS = 10;//线程数

    private String parent_path;
    private String meta_data_path;
    private String[] extension;

    public CreateMetaData(String parent_path, String meta_data_path, String[] extension) {
        this.parent_path = parent_path;
        this.meta_data_path = meta_data_path;
        this.extension = extension;
    }

    private void do_preprocess() throws IOException, ExecutionException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        File meta_data = new File(meta_data_path);
        File root = new File(parent_path);
        LinkedList<File> items = (LinkedList<File>) FileUtils.listFiles(root, extension, false);
        ExecutorService taskExecutor = Executors.newFixedThreadPool(TOTAL_THREADS);
        FileWriter fw = new FileWriter(meta_data);
        List<MetaData> datas = new LinkedList<MetaData>();
        for (int i = 0; i < items.size(); i++) {
            File item = items.get(i);
            FetchInfoTask task = new FetchInfoTask(item);
            Future<MetaData> result = taskExecutor.submit(task);
            datas.add(result.get());
            System.out.println("当前系统已完成预处理： " + (i + 1) + "张图片");
        }
        taskExecutor.shutdown();

        String json_data = mapper.writeValueAsString(datas);

        fw.write(json_data);

        fw.close();
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        String parent_path = "G:\\group\\big\\";
        String meta_data_path = "meta_data.txt";
        String[] extension = new String[]{"jpg", "jpeg", "png", "bmp"};

        CreateMetaData createMetaData = new CreateMetaData(parent_path, meta_data_path, extension);

        createMetaData.do_preprocess();
    }
}
