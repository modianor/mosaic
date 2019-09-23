package cn.edu.usts.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;

@Builder
@Setter
@Getter
@ToString
public class MetaData {
    private String file_name;
    private String format;
    private Size size;

    @Tolerate
    public MetaData() {
    }
}
