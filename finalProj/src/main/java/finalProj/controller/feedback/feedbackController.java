package finalProj.controller.feedback;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.feedback.Feedback;
import finalProj.domain.feedback.FeedbackAttachment;
import finalProj.domain.feedback.FeedbackCategory;
import finalProj.dto.FeedbackResponse;
import finalProj.service.feedback.FeedbackCategoryService;
import finalProj.service.feedback.FeedbackService;

@RestController
@RequestMapping("/api/feedback")
public class feedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackCategoryService feedbackCategoryService;

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
            try {
                // 轉換每個附件的 base64 → byte[]
                if (feedback.getAttachments() != null) {
                    for (FeedbackAttachment attachment : feedback.getAttachments()) {
                        if (attachment.getFileDataBase64() != null) {
                            byte[] data = Base64.getDecoder().decode(attachment.getFileDataBase64());
                            attachment.setAttachment(data);
                            attachment.setFileSize(data.length);
                        }
                    }
                }
                Feedback savedFeedback = feedbackService.insert(feedback);
                if (savedFeedback == null) {
                    response.setSuccess(false);
                    response.setMessage("新增失敗");
                } else {
                    response.setSuccess(true);
                    response.setMessage("新增成功");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.setSuccess(false);
                response.setMessage("處理失敗：" + e.getMessage());
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
            try {
                // 轉換每個附件的 base64 → byte[]
                if (feedback.getAttachments() != null) {
                    for (FeedbackAttachment attachment : feedback.getAttachments()) {
                        if (attachment.getFileDataBase64() != null) {
                            byte[] data = Base64.getDecoder().decode(attachment.getFileDataBase64());
                            attachment.setAttachment(data);
                        }
                    }
                }
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
            } catch (Exception e) {
                e.printStackTrace();
                response.setSuccess(false);
                response.setMessage("處理失敗：" + e.getMessage());
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
    // -- 新增意見分類 --

    @PostMapping("/category")
    public ResponseEntity<Map<String, Object>> postBulletinCategory(@RequestBody FeedbackCategory body) {
        Map<String, Object> response = new HashMap<>();
        if (body == null) {
            response.put("result", "新增失敗");
            return ResponseEntity.status(400).body(response);
        }
        FeedbackCategory feedbackCategory = feedbackCategoryService.save(body);
        if (feedbackCategory == null) {
            response.put("result", "新增失敗");
            return ResponseEntity.status(400).body(response);
        }
        response.put("result", "新增成功");
        response.put("data", feedbackCategory);
        return ResponseEntity.ok(response);

    }

    //
    // -- 刪除意見分類 --
    //

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Map<String, Object>> deleteFeedbackCategory(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        if (id == null) {
            response.put("result", "未提供刪除分類所需資料");
            return ResponseEntity.badRequest().body(response);
        }

        String result = feedbackCategoryService.deleteById(id);
        response.put("result", result);

        if (result.contains("成功")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response);
        }
    }
}
