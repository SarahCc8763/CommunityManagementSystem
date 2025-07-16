package finalProj.controller.bulletin;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.bulletin.BulletinAttachment;
import finalProj.domain.bulletin.BulletinCategory;
import finalProj.domain.bulletin.BulletinComment;
import finalProj.domain.poll.Poll;
import finalProj.domain.users.Users;
import finalProj.dto.bulletin.BulletinResponse;
import finalProj.repository.users.UsersRepository;
import finalProj.service.bulletin.BulletinAttachmentService;
import finalProj.service.bulletin.BulletinCategoryService;
import finalProj.service.bulletin.BulletinCommentLikeService;
import finalProj.service.bulletin.BulletinCommentService;
import finalProj.service.bulletin.BulletinService;
import finalProj.service.community.CommunityService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/bulletin")
@Slf4j
public class BulletinController {
    @Autowired

    //
    // 之後應該改成用service
    private UsersRepository usersRepository;
    //

    @Autowired
    private BulletinService bulletinService;

    @Autowired
    private BulletinCommentService bulletinCommentService;

    @Autowired
    private BulletinCategoryService bulletinCategoryService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private BulletinAttachmentService bulletinAttachmentService;

    @Autowired
    private BulletinCommentLikeService bulletinCommentLikeService;

    //
    //
    // ------ 公告 ------
    //
    //

    // -- 新增公告 --

    @PostMapping
    public BulletinResponse postBulletin(@RequestBody Bulletin bulletin) {
        BulletinResponse response = new BulletinResponse();
        if (bulletin == null) {
            response.setSuccess(false);
            response.setMessage("請輸入資料");
        } else {
            try {
                // 轉換每個附件的 base64 → byte[]
                if (bulletin.getAttachments() != null) {
                    for (BulletinAttachment attachment : bulletin.getAttachments()) {
                        if (attachment.getFileDataBase64() != null) {
                            byte[] data = Base64.getDecoder().decode(attachment.getFileDataBase64());
                            attachment.setFileData(data);
                        }
                    }
                }

                Bulletin saved = bulletinService.insert(bulletin);
                if (saved == null) {
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
    // -- 下載公告附件 --
    //

    @GetMapping("/attachments/{id}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable Integer id) {
        Optional<BulletinAttachment> optional = bulletinAttachmentService.findById(id);
        if (!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        BulletinAttachment attachment = optional.get();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(attachment.getMimeType()));
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(attachment.getFileName())
                .build());

        return new ResponseEntity<>(attachment.getFileData(), headers,
                HttpStatus.OK);
    }
    // //對應前端(先放著)
    // function downloadAttachment(id, filename) {
    // fetch(`/attachments/${id}`)
    // .then(response => {
    // if (!response.ok) throw new Error("下載失敗");
    // return response.blob();
    // })
    // .then(blob => {
    // const url = window.URL.createObjectURL(blob);
    // const a = document.createElement('a');
    // a.href = url;
    // a.download = filename; // 從資料庫中取的原始檔名
    // a.click();
    // window.URL.revokeObjectURL(url);
    // })
    // .catch(error => {
    // alert("下載失敗：" + error.message);
    // });
    // }
    //

    //
    // -- 修改公告 --
    //

    @PutMapping("/{id}")
    public BulletinResponse putBulletin(@PathVariable Integer id, @RequestBody Bulletin body) {
        BulletinResponse response = new BulletinResponse();

        if (id == null) {
            response.setSuccess(false);
            response.setMessage("未提供要修改哪筆資料");
            return response;
        }

        if (!bulletinService.existsById(id)) {
            response.setSuccess(false);
            response.setMessage("資料不存在");
            return response;
        }

        body.setId(id); // 使用 setId，因為你的主鍵是 id

        // 如果 attachments 有 base64，先解碼
        if (body.getAttachments() != null) {
            for (BulletinAttachment attachment : body.getAttachments()) {
                if (attachment.getFileDataBase64() != null) {
                    attachment.setFileData(Base64.getDecoder().decode(attachment.getFileDataBase64()));
                }
            }
        }

        Bulletin updated = bulletinService.modify(body);

        if (updated != null) {
            response.setSuccess(true);
            response.setMessage("修改成功");
        } else {
            response.setSuccess(false);
            response.setMessage("修改失敗");
        }

        return response;
    }

    //
    // -- 在現有公告新增投票--
    //
    @PostMapping("/{id}/poll")
    public ResponseEntity<?> addPollToBulletin(@PathVariable Integer id, @RequestBody Poll poll) {
        try {
            Poll createdPoll = bulletinService.addPollToBulletin(id, poll);
            return ResponseEntity.ok("投票建立成功，ID: " + createdPoll.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("發生錯誤：" + e.getMessage());
        }
    }

    //
    // -- 刪除公告 --
    //

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
    // -- 查詢公告 --
    //

    // 查詢所有該社區公告
    @GetMapping("/community/{communityId}")
    public BulletinResponse findAllBulletin(@PathVariable Integer communityId) {
        BulletinResponse response = new BulletinResponse();
        if (bulletinService.findAll(communityId).isEmpty()) {
            response.setMessage("查無資料");
            response.setSuccess(false);
        } else {

            response.setCount(bulletinService.count());
            response.setSuccess(true);
            response.setMessage("查詢成功");
            response.setList(bulletinService.findAll(communityId));
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

        List<Bulletin> list = bulletinService.findByCategoryAndTitle(body)
                .stream().filter(a -> a.getCommunity().getCommunityId() == body.getCommunity().getCommunityId())
                .collect(Collectors.toList());
        // System.out.println(list.get(0).getCommunity().getCommunityId());

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
    // ------ 公告的留言 ------
    //
    //

    // -- 新增留言 --

    @PostMapping("/{bulletinId}/comment")
    public BulletinComment postBulletinComment(@PathVariable Integer bulletinId, @RequestBody BulletinComment body) {
        Users user = usersRepository.findById(body.getUser().getUsersId()).get();
        body.setUser(user);
        body.setBulletin(bulletinService.findById(bulletinId));

        return bulletinCommentService.save(body);
    }

    //
    // -- 修改/刪除留言--
    //

    @PutMapping("/comment/{id}")
    public ResponseEntity<?> updateBulletinComment(@PathVariable Integer id, @RequestBody BulletinComment body) {
        if (body != null) {
            body.setId(id);
            BulletinComment updated = bulletinCommentService.modify(body);
            if (updated != null) {
                return ResponseEntity.ok(updated); // 200 OK，回傳更新後的資料
            } else {
                return ResponseEntity.badRequest().body("修改失敗");
            }
        }
        return ResponseEntity.badRequest().body("請求資料無效");
    }

    //
    //
    // ------ 公告的分類 ------
    //
    //

    // -- 根據社區查詢公告分類 --

    @GetMapping("/category/community/{communityId}")
    public List<BulletinCategory> findAllBulletinCategoryByCommunity(@PathVariable Integer communityId) {
        return bulletinCategoryService.findByCommunityId(communityId);
    }

    // -- 新增公告分類 --

    @PostMapping("/category")
    public BulletinCategory postBulletinCategory(@RequestBody BulletinCategory body) {
        if (body != null) {
            body.setCommunity(communityService.findById(body.getCommunity().getCommunityId()));
            return bulletinCategoryService.save(body);

        }
        return null;
    }

    //
    // -- 刪除公告分類 --
    //

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Map<String, Object>> deleteBulletinCategory(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        if (id == null) {
            response.put("result", "未提供刪除分類所需資料");
            return ResponseEntity.badRequest().body(response);
        }

        log.info("收到刪除公告分類請求：{}", id);
        String result = bulletinCategoryService.delete(id);
        response.put("result", result);

        if (result.contains("成功")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(response); // 可視情況改成 404 or 409
        }
    }

    @PutMapping("/category/{id}")
    public BulletinCategory modifyBulletinCategory(@PathVariable Integer id, @RequestBody BulletinCategory body) {
        if (body != null) {
            body.setId(id);
            return bulletinCategoryService.modify(body);
        }
        return null;
    }

    //
    // -- 查詢全部公告分類 --
    //

    @GetMapping("/category")
    public List<BulletinCategory> findAllBulletinCategory() {
        return bulletinCategoryService.findAll();
    }

    //
    //
    // ------ 公告留言的案讚 ------
    //
    //

    @PostMapping("/comment/{commentId}/like/{userId}")
    public BulletinComment postBulletinCommentLike(@PathVariable Integer commentId, @PathVariable Integer userId) {
        bulletinCommentLikeService.likeComment(commentId, userId);
        return bulletinCommentService.findById(commentId).get(); // 返回最新留言資訊
    }

}
