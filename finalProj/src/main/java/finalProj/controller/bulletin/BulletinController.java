package finalProj.controller.bulletin;

import java.time.LocalDateTime;
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

import finalProj.domain.User;
import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.bulletin.BulletinCategory;
import finalProj.domain.bulletin.BulletinComment;
import finalProj.dto.BulletinResponse;
import finalProj.service.UserService;
import finalProj.service.bulletin.BulletinCategoryService;
import finalProj.service.bulletin.BulletinCommentService;
import finalProj.service.bulletin.BulletinService;
import finalProj.service.community.CommunityService;

@RestController
@RequestMapping("/api/bulletin")
public class BulletinController {
    @Autowired
    UserService userService;

    @Autowired
    private BulletinService bulletinService;

    @Autowired
    private BulletinCommentService bulletinCommentService;

    @Autowired
    BulletinCategoryService bulletinCategoryService;

    @Autowired
    CommunityService communityService;

    // 新增公告
    @PostMapping
    public BulletinResponse postBulletin(@RequestBody Bulletin bulletin) {
        BulletinResponse response = new BulletinResponse();
        if (bulletin == null) {
            response.setSuccess(false);
            response.setMessage("請輸入資料");
        } else {
            Bulletin bean = bulletinService.insert(bulletin);
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

    //
    //
    //
    // 修改
    @PutMapping("/{id}")
    public BulletinResponse putBulletin(@PathVariable Integer id, @RequestBody Bulletin body) {
        BulletinResponse response = new BulletinResponse();

        if (id == null) {
            response.setSuccess(false);
            response.setMessage("未提供要修改哪筆資料");

        } else if (!bulletinService.existsById(id)) {
            response.setSuccess(false);
            response.setMessage("資料不存在");

        } else {
            body.setId(id);
            Bulletin bulletin = bulletinService.modify(body);
            if (bulletin == null) {
                response.setSuccess(false);
                response.setMessage("修改失敗");
            } else {
                response.setSuccess(true);
                response.setMessage("修改成功");
            }
        }
        return response;
    }

    //
    //
    //
    // 刪除
    @DeleteMapping("/{id}")
    public BulletinResponse deleteBulletin(@PathVariable Integer id) {
        BulletinResponse response = new BulletinResponse();
        System.out.println(id);
        if (id == null) {
            response.setSuccess(false);
            response.setMessage("未提供要刪除哪筆資料");
        } else {
            if (!bulletinService.existsById(id)) {
                response.setSuccess(false);
                response.setMessage("資料不存在");
            } else {
                if (!bulletinService.deleteById(id)) {
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

    //
    //
    //
    // 查詢
    // 查詢所有公告
    @GetMapping
    public BulletinResponse findAllBulletin() {
        BulletinResponse response = new BulletinResponse();
        if (bulletinService.findAll().isEmpty()) {
            response.setMessage("查無資料");
            response.setSuccess(false);
        } else {
            List<Bulletin> list = bulletinService.findAll();
            response.setCount(Long.valueOf(list.size()));
            response.setSuccess(true);
            response.setMessage("查詢成功");
            response.setList(bulletinService.findAll());
        }
        return response;
    }

    // 查詢單筆公告
    @GetMapping("/{id}")
    public BulletinResponse findBulletinById(@PathVariable Integer id) {
        BulletinResponse response = new BulletinResponse();
        if (id == null) {
            response.setSuccess(false);
            response.setMessage("未提供要查詢哪筆資料");
        } else {
            if (!bulletinService.existsById(id)) {
                response.setSuccess(false);
                response.setMessage("資料不存在");
            } else {
                List<Bulletin> list = List.of(bulletinService.findById(id));
                response.setCount(Long.valueOf(list.size()));
                response.setSuccess(true);
                response.setMessage("查詢成功");
                response.setList(list);
            }
        }
        return response;
    }

    // 類別或標題查詢公告
    @PostMapping("/searchby")
    public BulletinResponse searchBulletinBy(@RequestBody Bulletin body) {
        BulletinResponse response = new BulletinResponse();
        String category = "";
        String title = "";
        if (body.getCategory() == null || body.getCategory().getName() == null) {
            category = null;
        } else {
            category = body.getCategory().getName();
        }
        if (body.getTitle() == null) {
            title = null;
        } else {
            title = body.getTitle();
        }

        List<Bulletin> list = bulletinService.findByCategoryAndTitle(category, title);

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

    //
    //
    //
    // 公告-留言

    // 新增留言
    @PostMapping("/{bulletinId}/comment")
    public BulletinComment postBulletinComment(@PathVariable Integer bulletinId, @RequestBody BulletinComment body) {
        User user = userService.findById(body.getUser().getUserId()).get();
        body.setUser(user);
        body.setBulletin(bulletinService.findById(bulletinId));

        return bulletinCommentService.save(body);
    }

    // 修改/刪除留言
    @PutMapping("/comment/{id}")
    public BulletinComment updateBulletinComment(@PathVariable Integer id,
            @RequestBody BulletinComment body) {

        if (body != null) {
            body.setId(id);
            body.setTime(LocalDateTime.now());
            body.setUser(userService.findById(body.getUser().getUserId()).get()); // 資料庫抓取完整用戶資料放進關聯屬性user
            body.setBulletin(bulletinService.findById(body.getBulletin().getId())); // 資料庫抓取完整公告資料放進關聯屬性bulletin
            return bulletinCommentService.save(body);

        }
        return null;
    }

    //
    //
    // 公告-分類

    // 新增分類
    @PostMapping("/category")
    public BulletinCategory postBulletinCategory(@RequestBody BulletinCategory body) {
        if (body != null) {
            body.setCommunity(communityService.findById(body.getCommunity().getCommunityId()));
            return bulletinCategoryService.save(body);

        }
        return null;
    }
}
