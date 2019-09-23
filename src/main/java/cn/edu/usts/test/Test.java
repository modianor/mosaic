package cn.edu.usts.test;

import org.bytedeco.javacpp.*;

import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        opencv_core.Mat src = opencv_imgcodecs.imread("G:\\group\\small\\IMG_482.jpg");
        opencv_core.Mat src1 = opencv_imgcodecs.imread("G:\\group\\small\\IMG_483.jpg");
        opencv_highgui.namedWindow("src", opencv_highgui.WINDOW_NORMAL);
        opencv_highgui.imshow("src", src);

        BytePointer ptr = src.ptr();

        BytePointer bp = src.data();

        byte[] data = new byte[src.cols() * src.rows() * src.channels()];
        byte[] data1 = new byte[src.cols() * src.rows() * src.channels()];
        bp.get(data1);

        ptr.get(data);

        String str_data = Arrays.toString(data);
        String str_data1 = Arrays.toString(data1);

        opencv_imgproc.cvtColor(src, src, opencv_imgproc.COLOR_BGR2Lab);

        System.out.println(str_data);
        System.out.println(str_data1);
        calcDeltaBgr(src, src1);
        int key = 0;
        while ((key & 0xFF) != 27)
            key = opencv_highgui.cvWaitKey(20);

        src.release();
        opencv_highgui.destroyAllWindows();
    }

    public static void calcDeltaLab(opencv_core.Mat m1, opencv_core.Mat m2) {

        if (m1.cols() != m2.cols() || m1.rows() != m2.rows() || m1.channels() != m2.channels() || m1.type() != m2.type())
            return;

        int height = m1.rows();
        int width = m1.cols();
        int channels = m1.channels();

        opencv_core.Mat lab_1 = new opencv_core.Mat();
        opencv_core.Mat lab_2 = new opencv_core.Mat();
        opencv_core.Mat dst = new opencv_core.Mat();
        opencv_imgproc.cvtColor(m1, lab_1, opencv_imgproc.COLOR_BGR2Lab);
        opencv_imgproc.cvtColor(m2, lab_2, opencv_imgproc.COLOR_BGR2Lab);

        opencv_core.absdiff(lab_1, lab_2, dst);

        byte[] detla_lan_data = new byte[width * height * channels];
        dst.data().get(detla_lan_data);

        double detle_lan = 0;

        for (int i = 0; i < detla_lan_data.length; i = i + 3) {
            int l = detla_lan_data[i + 0];
            int a = detla_lan_data[i + 1];
            int b = detla_lan_data[i + 2];
            detle_lan += Math.pow((Math.pow(l, 2) + Math.pow(a, 2) + Math.pow(b, 2)), 0.5);
        }

        System.out.println("detla lan is " + detle_lan);
    }

    public static void calcDeltaBgr(opencv_core.Mat m1, opencv_core.Mat m2) {

        if (m1.cols() != m2.cols() || m1.rows() != m2.rows() || m1.channels() != m2.channels() || m1.type() != m2.type())
            return;

        int height = m1.rows();
        int width = m1.cols();
        int channels = m1.channels();

        opencv_core.Mat dst = new opencv_core.Mat();

        opencv_core.absdiff(m1, m2, dst);

        byte[] detla_lan_data = new byte[width * height * channels];

        dst.data().get(detla_lan_data);

        double detle_lan = 0;

        for (int i = 0; i < detla_lan_data.length; i = i + 3) {
            int b = detla_lan_data[i + 0];
            int g = detla_lan_data[i + 1];
            int r = detla_lan_data[i + 2];
            detle_lan += Math.pow((Math.pow(b, 2) + Math.pow(g, 2) + Math.pow(r, 2)), 0.5);
        }

        System.out.println("detla lan is " + detle_lan);
    }
}
