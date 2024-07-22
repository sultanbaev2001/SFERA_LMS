package sfera.payload.req;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqHomeWork {
    private String fileName;
    private Integer taskId;

}
