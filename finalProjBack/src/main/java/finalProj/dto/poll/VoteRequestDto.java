package finalProj.dto.poll;

import java.util.List;

import lombok.Data;

@Data
public class VoteRequestDto {
    private Integer userId;
    private List<Integer> selectedOptionIds;
}
