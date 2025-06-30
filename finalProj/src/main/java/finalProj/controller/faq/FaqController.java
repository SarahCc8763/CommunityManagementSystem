package finalProj.controller.faq;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.faq.Faq;
import finalProj.dto.FaqDto;
import finalProj.dto.FaqResponse;
import finalProj.service.faq.FaqService;

@RestController
@RequestMapping("/api/faq")
public class FaqController {

    @Autowired
    private FaqService faqService;

    @PostMapping
    public FaqResponse postFaq(@RequestBody Faq faq) {
        FaqResponse response = new FaqResponse();
        if (faq == null) {
            response.setSuccess(false);
            response.setMessage("請輸入資料");
        } else {
            Faq bean = faqService.insert(faq);
            if (bean == null) {
                response.setSuccess(false);
                response.setMessage("新增失敗");
            } else {
                response.setSuccess(true);
                response.setMessage("新增成功");
            }

        }
        return response;

    }

    @PutMapping("/{id}")
    public FaqResponse putFaq(@PathVariable Integer id, @RequestBody Faq body) {
        FaqResponse response = new FaqResponse();

        if (id == null) {
            response.setSuccess(false);
            response.setMessage("未提供要修改哪筆資料");

        } else if (!faqService.existsById(id)) {
            response.setSuccess(false);
            response.setMessage("資料不存在");

        } else {
            body.setId(id);
            Faq faq = faqService.modify(body);
            System.out.println(faq);
            if (faq == null) {
                response.setSuccess(false);
                response.setMessage("修改失敗");
            } else {
                response.setSuccess(true);
                response.setMessage("修改成功");
            }
        }
        return response;
    }

    @DeleteMapping("/{id}")
    public FaqResponse deleteFaq(@PathVariable Integer id) {
        FaqResponse response = new FaqResponse();
        System.out.println(id);
        if (id == null) {
            response.setSuccess(false);
            response.setMessage("未提供要刪除哪筆資料");
        } else {
            if (!faqService.existsById(id)) {
                response.setSuccess(false);
                response.setMessage("資料不存在");
            } else {
                if (!faqService.deleteById(id)) {
                    response.setSuccess(false);
                    response.setMessage("刪除失敗");
                } else {
                    response.setSuccess(true);
                    response.setMessage("刪除成功");
                }
                ;
            }
        }
        return response;
    }

    @GetMapping
    public FaqResponse findAllFaq() {
        FaqResponse response = new FaqResponse();
        if (faqService.findAll().isEmpty()) {
            response.setMessage("查無資料");
            response.setSuccess(false);
        } else {
            response.setSuccess(true);
            response.setMessage("查詢成功");
            response.setList(faqService.findAll());
        }
        return response;
    }

    @GetMapping("/{id}")
    public FaqResponse findFaqById(@PathVariable Integer id) {
        FaqResponse response = new FaqResponse();
        if (id == null) {
            response.setSuccess(false);
            response.setMessage("未提供要查詢哪筆資料");
        } else {
            if (!faqService.existsById(id)) {
                response.setSuccess(false);
                response.setMessage("資料不存在");
            } else {
                response.setSuccess(true);
                response.setMessage("查詢成功");
                response.setList(faqService.findById(id));
            }
        }
        return response;
    }

    @PostMapping("/searchby")
    public FaqResponse searchFaqBy(@RequestBody Faq body) {
        FaqResponse response = new FaqResponse();
        String category = "";
        if (body.getCategory() == null || body.getCategory().getName() == null) {
            category = null;
        } else {
            category = body.getCategory().getName();
        }

        List<String> keywordList = body.getKeywords() == null ? null : Arrays.asList(body.getKeywords().split(","));
        List<FaqDto> list = faqService.findByCategoryAndKeyword(category, keywordList);

        if (list.isEmpty()) {
            response.setMessage("查無資料");
            response.setSuccess(false);
        } else {
            response.setSuccess(true);
            response.setMessage("查詢成功");
            response.setList(list);
        }
        return response;
    }
}
