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
import finalProj.repository.poll.PollRepository;

@Service
@Transactional
public class BulletinService {

    @Autowired
    private BulletinRepository bulletinRepository;

    @Autowired
    private BulletinCategoryRepository bulletinCategoryRepository;

    @Autowired
    private BulletinAttachmentRepository bulletinAttachmentRepository;

    @Autowired
    private PollRepository pollRepository;

    public List<Bulletin> findAll() {
        return bulletinRepository.findAll();
    }

    public Bulletin findById(Integer id) {
        Optional<Bulletin> optional = bulletinRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }

        return null;
    }

    public Bulletin insert(Bulletin entity) {
        if (entity != null) {
            try {
                // 1. 確認分類是否存在
                Optional<BulletinCategory> optional = bulletinCategoryRepository
                        .findByName(entity.getCategory().getName());
                if (!optional.isPresent()) {
                    System.out.println("分類不存在，請先建立分類");
                    return null;
                }
                entity.setCategory(optional.get());

                // 2. 先儲存 Bulletin 主文（因為要產生 bulletinId）
                Bulletin savedBulletin = bulletinRepository.save(entity);

                // 3. 儲存附件（若有）
                List<BulletinAttachment> attachments = entity.getAttachments();
                if (attachments != null && !attachments.isEmpty()) {
                    for (BulletinAttachment attachment : attachments) {
                        attachment.setBulletin(savedBulletin); // 設定關聯
                        bulletinAttachmentRepository.save(attachment); // 儲存附件
                    }
                }
                // 4. 若有 poll，建立關聯與選項
                Poll poll = entity.getPoll();
                if (poll != null) {
                    poll.setBulletin(savedBulletin); // 雙向關聯

                    if (poll.getOptions() != null) {
                        for (PollOption option : poll.getOptions()) {
                            option.setPoll(poll); // 設定選項與投票的關聯
                        }
                    }
                    pollRepository.save(poll);
                }
                return savedBulletin;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Bulletin modify(Bulletin entity) {
        if (entity == null || entity.getId() == null)
            return null;

        try {
            Optional<Bulletin> optional = bulletinRepository.findById(entity.getId());
            if (!optional.isPresent())
                return null;

            Bulletin existing = optional.get();

            // 更新主文內容
            existing.setTitle(entity.getTitle() != null ? entity.getTitle() : existing.getTitle());
            existing.setDescription(
                    entity.getDescription() != null ? entity.getDescription() : existing.getDescription());
            existing.setRemoveTime(entity.getRemoveTime() != null ? entity.getRemoveTime() : existing.getRemoveTime());
            existing.setCommunity(entity.getCommunity() != null ? entity.getCommunity() : existing.getCommunity());
            existing.setUser(entity.getUser() != null ? entity.getUser() : existing.getUser());

            existing.setModifyTime(LocalDateTime.now()); // 一定要更新

            // 更新分類（若有）
            if (entity.getCategory() != null) {
                Optional<BulletinCategory> categoryOpt = bulletinCategoryRepository
                        .findByName(entity.getCategory().getName());
                categoryOpt.ifPresent(existing::setCategory);
            }

            // 移除舊附件
            if (existing.getAttachments() != null && !existing.getAttachments().isEmpty()) {
                bulletinAttachmentRepository.deleteAll(existing.getAttachments());
                existing.getAttachments().clear();
            }

            // 加入新附件（若有）
            if (entity.getAttachments() != null && !entity.getAttachments().isEmpty()) {
                List<BulletinAttachment> newAttachments = new ArrayList<>();
                for (BulletinAttachment newAttachment : entity.getAttachments()) {
                    // 不再解碼， fileData 已在 Controller 解好
                    newAttachment.setBulletin(existing);
                    newAttachments.add(newAttachment);
                }
                existing.setAttachments(newAttachments);
            }

            return bulletinRepository.save(existing);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteById(Integer id) {
        if (id != null) {
            try {
                if (bulletinRepository.existsById(id)) {
                    bulletinRepository.deleteById(id);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Boolean existsById(Integer id) {
        return bulletinRepository.existsById(id);
    }

    public List<Bulletin> findByCategoryAndTitle(Bulletin body) {
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
        return bulletinRepository.findByCategoryAndTitle(category, title);
    }

    // 在現有公告新增投票
    public Poll addPollToBulletin(Integer bulletinId, Poll poll) {
        Optional<Bulletin> optional = bulletinRepository.findById(bulletinId);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("找不到對應公告");
        }

        Bulletin bulletin = optional.get();

        // 檢查是否已有投票
        if (bulletin.getPoll() != null) {
            throw new IllegalStateException("該公告已經有投票");
        }

        // 設定關聯
        poll.setBulletin(bulletin);
        if (poll.getOptions() != null) {
            for (PollOption option : poll.getOptions()) {
                option.setPoll(poll);
            }
        }
        // 儲存並回傳
        Poll saved = pollRepository.save(poll);
        bulletin.setPoll(saved); // 可選，加強雙向關聯
        return saved;
    }
}
