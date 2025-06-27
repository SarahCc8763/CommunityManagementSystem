package finalProj.controller.feedback;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.feedback.Feedback;
import finalProj.dto.FeedbackResponse;
import finalProj.service.feedback.FeedbackService;

@RestController
@RequestMapping("/api/feedback")
public class feedbackController {
    @Autowired
    private FeedbackService feedbackService;

    //
    //
    // ---------- 意見主表 ----------
    //
    //

    //
    // -- 新增 --
    //

    @PostMapping
    public FeedbackResponse insert(@RequestBody Feedback feedback) {
        FeedbackResponse response = new FeedbackResponse();
        if (feedback == null) {
            response.setSuccess(false);
            response.setMessage("缺少必要欄位資料");
        } else {
            Feedback savedFeedback = feedbackService.insert(feedback);
            if (savedFeedback == null) {
                response.setSuccess(false);
                response.setMessage("新增失敗");
            } else {
                response.setSuccess(true);
                response.setMessage("新增成功");
            }

        }
        return response;
    }

    //
    // -- 修改 --
    //

    @PutMapping("/{id}")
    public FeedbackResponse update(@PathVariable Integer id, @RequestBody Feedback feedback) {
        FeedbackResponse response = new FeedbackResponse();
        if (feedback == null || id == null) {
            response.setSuccess(false);
            response.setMessage("缺少必要欄位資料");
            return response;
        }
        feedback.setId(id);
        if (!feedbackService.existById(id)) {
            response.setSuccess(false);
            response.setMessage("欲修改資料不存在");
            return response;
        } else {
            Feedback updatedFeedback = feedbackService.modify(feedback);
            if (updatedFeedback == null) {
                response.setSuccess(false);
                response.setMessage("修改失敗");
            } else {
                response.setSuccess(true);
                response.setMessage("修改成功");
                List<Feedback> data = new ArrayList<>();
                data.add(updatedFeedback);
                response.setList(data);
            }
        }
        return response;
    }

    //
    // --查詢 --
    //

    // 查詢所有
    @GetMapping
    public FeedbackResponse getAllFeedbacks() {
        FeedbackResponse response = new FeedbackResponse();
        List<Feedback> feedbacks = feedbackService.findAll();
        if (feedbacks.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("查無資料");
        } else {
            response.setSuccess(true);
            response.setMessage("查詢成功");
            response.setCount(feedbackService.count());
            response.setList(feedbacks);
        }
        return response;
    }

    // 查詢單筆
    @GetMapping("/{id}")
    public FeedbackResponse getFeedbackById(@PathVariable Integer id) {
        FeedbackResponse response = new FeedbackResponse();
        if (!feedbackService.existById(id)) {
            response.setSuccess(false);
            response.setMessage("查無該資料");
        } else {
            Feedback feedback = feedbackService.findById(id);
            response.setSuccess(true);
            response.setMessage("查詢成功");
            List<Feedback> data = new ArrayList<>();
            data.add(feedback);
            response.setList(data);
        }
        return response;
    }
    //
    //
    // ------ ------
    //
    //

}
