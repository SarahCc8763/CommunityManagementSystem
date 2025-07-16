package finalProj.service.bulletin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.bulletin.Bulletin;
import finalProj.domain.bulletin.BulletinAttachment;
import finalProj.domain.bulletin.BulletinCategory;
import finalProj.domain.poll.Poll;
import finalProj.domain.poll.PollOption;
import finalProj.repository.bulletin.BulletinAttachmentRepository;
import finalProj.repository.bulletin.BulletinCategoryRepository;
import finalProj.repository.bulletin.BulletinRepository;
import finalProj.repository.poll.PollOptionRepository;
import finalProj.repository.poll.PollRepository;
import finalProj.repository.poll.PollVoteRepository;
import finalProj.repository.users.UsersRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class BulletinService {

    private final UsersRepository usersRepository;

    @Autowired
    private BulletinRepository bulletinRepository;

    @Autowired
    private BulletinCategoryRepository bulletinCategoryRepository;

    @Autowired
    private BulletinAttachmentRepository bulletinAttachmentRepository;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollVoteRepository pollVoteRepository;

    @Autowired
    private PollOptionRepository pollOptionRepository;

    BulletinService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Bulletin> findAll(Integer communityId) {
        return bulletinRepository.findByCommunity_communityId(communityId);
    }

    public Long count() {
        return bulletinRepository.count();
    }

    //
    // ------ 透過id查詢 -------
    //

    public Bulletin findById(Integer id) {
        Optional<Bulletin> optional = bulletinRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }

        return null;
    }

    //
    // ------ 新增公告 -------
    //

    public Bulletin insert(Bulletin entity) {
        if (entity == null) {
            log.warn("新增公告失敗：entity 為 null");
            return null;
        }

        try {
            // 1. 確認分類是否存在
            Optional<BulletinCategory> optional = bulletinCategoryRepository
                    .findByCommunity_CommunityIdAndName(entity.getCommunity().getCommunityId(),
                            entity.getCategory().getName());

            if (!optional.isPresent()) {
                log.warn("新增公告失敗：分類 '{}' 不存在，請先建立分類", entity.getCategory().getName());
                return null;
            }

            entity.setCategory(optional.get());
            log.info("分類驗證成功：{}", entity.getCategory().getName());

            // 2. 儲存 Bulletin 主文
            if (entity.getRemoveTime() == null) {
                entity.setRemoveTime(LocalDateTime.now().plusDays(7));
            }

            Bulletin savedBulletin = bulletinRepository.save(entity);
            log.info("公告主文儲存成功，ID：{}", savedBulletin.getId());

            // 3. 儲存附件
            List<BulletinAttachment> attachments = entity.getAttachments();
            if (attachments != null && !attachments.isEmpty()) {
                for (BulletinAttachment attachment : attachments) {
                    attachment.setBulletin(savedBulletin);
                    bulletinAttachmentRepository.save(attachment);
                    log.info("附件儲存成功：{}", attachment.getFileName());
                }
            }

            // 4. 儲存投票與選項
            Poll poll = entity.getPoll();
            if (poll != null) {
                poll.setBulletin(savedBulletin);

                if (poll.getOptions() != null) {
                    for (PollOption option : poll.getOptions()) {
                        option.setPoll(poll);
                        log.debug("設定投票選項關聯：{}", option.getText());
                    }
                }
                if (poll.getStart() == null) {
                    poll.setStart(LocalDateTime.now());
                }
                pollRepository.save(poll);
                log.info("投票儲存成功，標題：{}", poll.getTitle());
            }

            log.info("公告新增流程完成，公告 ID：{}", savedBulletin.getId());
            return savedBulletin;

        } catch (Exception e) {
            log.error("新增公告時發生例外：{}", e.getMessage(), e);
            return null;
        }
    }

    //
    // ------ 修改公告 -------
    //

    public Bulletin modify(Bulletin entity) {
        if (entity == null || entity.getId() == null) {
            log.warn("修改公告失敗：entity 或 ID 為 null");
            return null;
        }

        try {
            Optional<Bulletin> optional = bulletinRepository.findById(entity.getId());
            if (!optional.isPresent()) {
                log.warn("修改公告失敗：找不到 ID = {} 的公告", entity.getId());
                return null;
            }

            Bulletin existing = optional.get();
            log.info("開始修改公告 ID = {}", existing.getId());

            // 更新主文內容
            existing.setTitle(entity.getTitle() != null ? entity.getTitle() : existing.getTitle());
            existing.setDescription(
                    entity.getDescription() != null ? entity.getDescription() : existing.getDescription());
            existing.setRemoveTime(entity.getRemoveTime() != null ? entity.getRemoveTime() : existing.getRemoveTime());
            existing.setCommunity(entity.getCommunity() != null ? entity.getCommunity() : existing.getCommunity());
            existing.setUser(entity.getUser() != null ? usersRepository.findById(entity.getUser().getUsersId()).get()
                    : existing.getUser());
            existing.setPostStatus(entity.getPostStatus() != null ? entity.getPostStatus() : existing.getPostStatus());

            existing.setModifyTime(LocalDateTime.now());
            log.debug("公告基本資訊已更新");

            // 更新分類（若有）
            if (entity.getCategory() != null) {
                Optional<BulletinCategory> categoryOpt = bulletinCategoryRepository
                        .findByCommunity_CommunityIdAndName(entity.getCommunity().getCommunityId(),
                                entity.getCategory().getName());
                if (categoryOpt.isPresent()) {
                    existing.setCategory(categoryOpt.get());
                    log.debug("公告分類已更新為：{}", categoryOpt.get().getName());
                } else {
                    log.warn("公告分類更新失敗：找不到分類 '{}'", entity.getCategory().getName());
                    return null;
                }
            }

            List<BulletinAttachment> incoming = entity.getAttachments();
            List<BulletinAttachment> toKeep = new ArrayList<>();
            List<BulletinAttachment> toAdd = new ArrayList<>();

            for (BulletinAttachment att : incoming) {
                if (att.getFileDataBase64() == null && !att.getIsNew()) {
                    // 是原有檔案，只保留 (用檔名比對，或補個唯一 ID 更穩)
                    Optional<BulletinAttachment> existingAtt = existing.getAttachments()
                            .stream()
                            .filter(e -> e.getFileName().equals(att.getFileName()))
                            .findFirst();
                    existingAtt.ifPresent(toKeep::add);
                    log.info("保留原有附件：{}");
                } else if (att.getFileDataBase64() != null && att.getIsNew()) {
                    // 是新檔案，加到待新增清單
                    att.setBulletin(existing);
                    toAdd.add(att);
                } else {
                    log.warn("附件處理失敗");
                    return null;
                }
            }

            // 刪除未被保留的舊附件
            existing.getAttachments().retainAll(toKeep);

            // 加入新附件
            existing.getAttachments().addAll(toAdd);

            Bulletin updated = bulletinRepository.save(existing);
            log.info("公告修改成功，ID = {}", updated.getId());
            return updated;

        } catch (Exception e) {
            log.error("修改公告發生例外：{}", e.getMessage(), e);
            return null;
        }
    }

    //
    // ------ 刪除公告 -------
    //

    public boolean deleteById(Integer id) {
        if (id == null) {
            log.warn("刪除公告失敗：ID 為 null");
            return false;
        }

        try {
            Optional<Bulletin> optional = bulletinRepository.findById(id);

            if (optional.isPresent()) {
                Bulletin bulletin = optional.get();
                Poll poll = bulletin.getPoll();

                if (poll != null) {
                    // 初始化 LAZY 關聯，避免 Proxy 錯誤
                    poll.getVotes().size();
                    poll.getOptions().size();

                    // ✅ Step 1：先刪投票紀錄
                    pollVoteRepository.deleteAll(poll.getVotes());
                    pollVoteRepository.flush();
                    poll.getVotes().clear();
                    // ✅ Step 2：再刪選項
                    pollOptionRepository.deleteAll(poll.getOptions());
                    pollOptionRepository.flush();
                    poll.getOptions().clear();

                    // ✅ Step 3：解除 bulletin 與 poll 的關聯，先保存解除狀態
                    pollRepository.saveAndFlush(poll);
                    bulletin.setPoll(null);
                    bulletinRepository.saveAndFlush(bulletin); // ⬅ 關鍵點

                    // ✅ Step 4：最後再刪 poll
                    pollRepository.delete(poll);
                    pollRepository.flush(); // optional，但安全
                }

                // ✅ Step 5：刪公告本體
                bulletinRepository.delete(bulletin);
                log.info("成功刪除公告，ID = {}", id);
                return true;
            } else {
                log.warn("刪除公告失敗：找不到 ID = {}", id);
            }

        } catch (Exception e) {
            log.error("刪除公告時發生例外：{}", e.getMessage(), e);
        }

        return false;
    }

    //
    // ------ 確認公告存在 -------
    //

    public Boolean existsById(Integer id) {
        boolean exists = bulletinRepository.existsById(id);
        log.debug("檢查公告是否存在，ID = {}，結果 = {}", id, exists);
        return exists;
    }

    //
    // ------ 透過分類或標題查詢 -------
    //

    public List<Bulletin> findByCategoryAndTitle(Bulletin body) {
        String category = (body.getCategory() == null || body.getCategory().getName() == null)
                ? null
                : body.getCategory().getName();
        String title = (body.getTitle() == null) ? null : body.getTitle();

        log.info("查詢公告，條件：分類 = '{}', 標題 = '{}'", category, title);

        List<Bulletin> result = bulletinRepository.findByCategoryAndTitle(category, title);
        log.debug("查詢結果筆數：{}", result.size());
        return result;
    }

    // 在現有公告新增投票
    public Poll addPollToBulletin(Integer bulletinId, Poll poll) {
        log.info("準備為公告 ID = {} 新增投票：{}", bulletinId, poll.getTitle());

        Optional<Bulletin> optional = bulletinRepository.findById(bulletinId);
        if (!optional.isPresent()) {
            log.warn("新增投票失敗：找不到 ID = {} 的公告", bulletinId);
            throw new IllegalArgumentException("找不到對應公告");
        }

        Bulletin bulletin = optional.get();

        if (bulletin.getPoll() != null) {
            log.warn("新增投票失敗：公告 ID = {} 已經有投票", bulletinId);
            throw new IllegalStateException("該公告已經有投票");
        }

        poll.setBulletin(bulletin);
        if (poll.getOptions() != null) {
            for (PollOption option : poll.getOptions()) {
                option.setPoll(poll);
                log.debug("設定投票選項：{}", option.getText());
            }
        }

        Poll saved = pollRepository.save(poll);
        bulletin.setPoll(saved); // 雙向關聯（可選）
        log.info("成功為公告 ID = {} 新增投票：{}", bulletinId, saved.getTitle());
        return saved;
    }

}
