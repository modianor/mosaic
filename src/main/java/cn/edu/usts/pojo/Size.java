package cn.edu.usts.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

@Builder
@Getter
@Setter
@ToString
public class Size {
    private Integer width;
    private Integer height;

    @Tolerate
    public Size() {
    }
}
