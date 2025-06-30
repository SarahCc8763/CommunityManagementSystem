package finalProj.service.faq;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.embed.FaqFaqKeywordId;
import finalProj.domain.faq.Faq;
import finalProj.domain.faq.FaqCategory;
import finalProj.domain.faq.FaqFaqKeyword;
import finalProj.domain.faq.FaqKeyword;
import finalProj.dto.FaqDto;
import finalProj.repository.community.CommunityRepository;
import finalProj.repository.faq.FaqCategoryRepository;
import finalProj.repository.faq.FaqFaqKeywordRepository;
import finalProj.repository.faq.FaqKeywordRepository;
import finalProj.repository.faq.FaqRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class FaqService {

    @Autowired
    private FaqRepository faqRepository;

    @Autowired
    private FaqKeywordRepository faqKeywordRepository;

    @Autowired
    private FaqFaqKeywordRepository faqFaqKeywordRepository;

    @Autowired
    private FaqCategoryRepository faqCategoryRepository;

    @Autowired
    private CommunityRepository communityRepository;

    //
    //
    //
    // 查詢

    // 查詢全部
    public List<FaqDto> findAll() {
        return faqRepository.findAll().stream().map(faq -> {
            List<String> keywordNames = faq.getKeywordLinks().stream()
                    .map(link -> link.getKeyword().getWord())
                    .collect(Collectors.toList());

            return new FaqDto(faq, keywordNames);
        }).collect(Collectors.toList());

    }

    // 查詢單筆
    public List<FaqDto> findById(Integer id) {
        List<String> keywords = faqRepository.findById(id).get().getKeywordLinks().stream()
                .map(link -> link.getKeyword().getWord())
                .collect(Collectors.toList());
        FaqDto dto = new FaqDto(faqRepository.findById(id).get(), keywords);
        List<FaqDto> list = new ArrayList<FaqDto>();
        list.add(dto);
        return list;
    }

    // 分類或關鍵字查詢
    public List<FaqDto> findByCategoryAndKeyword(String category, List<String> keywords) {

        return faqRepository.findByCategoryAndKeywords(category, keywords).stream().map(faq -> {
            List<String> keywordNames = faq.getKeywordLinks().stream()
                    .map(link -> link.getKeyword().getWord())
                    .collect(Collectors.toList());

            return new FaqDto(faq, keywordNames);
        }).collect(Collectors.toList());
    }

    //
    //
    //

    // 新增
    public Faq insert(Faq entity) {
        if (entity != null) {
            // 找關鍵字
            String[] keywordArray = entity.getKeywords().split(",");
            Optional<FaqCategory> optionalCat = faqCategoryRepository.findByCommunity_CommunityIdAndName(
                    entity.getCommunity().getCommunityId(), entity.getCategory().getName());
            if (!optionalCat.isPresent()) {
                log.warn("找不到分類 ");
                return null;
            }
            log.info("找到分類物件: " + optionalCat.get().getName());
            entity.setCategory(optionalCat.get());
            Faq faq = faqRepository.save(entity); // 新增FAQ物件(不含關鍵字)
            for (String word : keywordArray) {
                Optional<FaqKeyword> optional = faqKeywordRepository.findByWord(word);
                log.info("尋找關鍵字: " + word);
                if (optional.isPresent()) { // 關鍵字存在
                    FaqKeyword faqKeyword = optional.get(); // 找到關鍵字物件

                    FaqFaqKeywordId faqFaqKeywordId = new FaqFaqKeywordId(faq.getId(), faqKeyword.getId()); // 建立複合主鍵ID物件
                    log.info("找到關鍵字，建立複合主鍵ID物件: " + faqFaqKeywordId.toString());

                    FaqFaqKeyword faqFaqKeyword = faqFaqKeywordRepository
                            .save(new FaqFaqKeyword(faqFaqKeywordId, faq, faqKeyword)); // 建立關聯物件

                    List<FaqFaqKeyword> list2 = faqKeyword.getFaqLinks(); // 取得faqkeyword物件現有關聯
                    list2.add(faqFaqKeyword); // 加入新關聯物件
                    faqKeyword.setFaqLinks(list2); // 設定faqkeyword物件關聯的中介屬性

                    List<FaqFaqKeyword> list = faq.getKeywordLinks(); // 取得faq物件現有關鍵字連結
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(faqFaqKeyword); // 加入新關聯物件
                    faq.setKeywordLinks(list); // 設定faq物件關鍵字連結

                } else { // 關鍵字不存在
                    log.info("關鍵字不存在，建立新關鍵字物件: " + word);
                    FaqKeyword faqKeyword = new FaqKeyword(); // 建立關鍵字物件
                    faqKeyword.setWord(word); // 設定關鍵字
                    faqKeywordRepository.save(faqKeyword); // 新增關鍵字物件(未連結)

                    FaqFaqKeywordId faqFaqKeywordId = new FaqFaqKeywordId(faq.getId(), faqKeyword.getId()); // 建立複合主鍵ID物件
                    log.info("建立複合主鍵ID物件: " + faqFaqKeywordId.toString());

                    FaqFaqKeyword faqFaqKeyword = faqFaqKeywordRepository
                            .save(new FaqFaqKeyword(faqFaqKeywordId, faq, faqKeyword)); // 建立關聯物件

                    // 設定faqkeyword物件關聯的中介屬性
                    List<FaqFaqKeyword> list = new ArrayList<>(); // 建立關聯物件List
                    list.add(faqFaqKeyword); // 加入新關聯物件
                    faqKeyword.setFaqLinks(list); // 將新關鍵字物件加入物件List

                    // 設定faq物件關聯的中介屬性
                    List<FaqFaqKeyword> list2 = new ArrayList<>();
                    if (faq.getKeywordLinks() != null) { // 如果已有現有關鍵字
                        list2 = faq.getKeywordLinks(); // 取得faq物件現有關鍵字連結
                    }

                    list2.add(faqFaqKeyword); // 加入新關聯物件
                    faq.setKeywordLinks(list2); // 設定faq物件關鍵字連結

                }
            }

            faq.setLastModified(LocalDateTime.now());
            faq.setPostStatus(false);
            return faq;

        }
        return null;
    }
    //
    //
    //

    // 修改
    public Faq modify(Faq entity) {
        if (entity != null && entity.getId() != null) {
            log.info("修改FAQ物件: " + entity.getId());
            Faq faq = faqRepository.findById(entity.getId()).get();
            if (faqRepository.existsById(entity.getId())) {
                // 找關鍵字是否存在
                String[] keywordArray = entity.getKeywords().split(",");
                for (String word : keywordArray) {
                    Optional<FaqKeyword> optional = faqKeywordRepository.findByWord(word);

                    if (optional.isPresent()) { // 關鍵字存在
                        FaqKeyword faqKeyword = optional.get(); // 找到關鍵字物件
                        FaqFaqKeywordId faqFaqKeywordId = new FaqFaqKeywordId(faq.getId(), faqKeyword.getId()); // 建立複合主鍵ID物件

                        if (faqFaqKeywordRepository.existsById(faqFaqKeywordId)) { // 關聯物件存在
                            log.info("關聯物件存在，不建立新關聯物件: {}", faqKeyword.getId());
                            log.info(faqKeyword.getWord());
                            continue; // 跳過
                        }

                        // FaqFaqKeyword faqFaqKeyword =
                        faqFaqKeywordRepository.save(new FaqFaqKeyword(faqFaqKeywordId, faq, faqKeyword)); // 建立關聯物件

                        // List<FaqFaqKeyword> list2 = faqKeyword.getFaqLinks(); // 取得faqkeyword物件現有關聯
                        // list2.add(faqFaqKeyword); // 加入新關聯物件
                        // faqKeyword.setFaqLinks(list2); // 設定faqkeyword物件關聯的中介屬性

                        // List<FaqFaqKeyword> list = faq.getKeywordLinks(); // 取得faq物件現有關鍵字連結
                        // if (list == null) {
                        // list = new ArrayList<>();
                        // }
                        // list.add(faqFaqKeyword); // 加入新關聯物件
                        // faq.setKeywordLinks(list); // 設定faq物件關鍵字連結

                    } else { // 關鍵字不存在
                        FaqKeyword faqKeyword = new FaqKeyword(); // 建立關鍵字物件
                        faqKeyword.setWord(word); // 設定關鍵字
                        faqKeyword = faqKeywordRepository.save(faqKeyword); // 新增關鍵字物件(未連結)
                        log.info("關鍵字不存在，建立新關鍵字物件，id為 : {} " + faqKeyword.getId());
                        log.info(word);

                        FaqFaqKeywordId faqFaqKeywordId = new FaqFaqKeywordId(faq.getId(), faqKeyword.getId()); // 建立複合主鍵ID物件

                        // FaqFaqKeyword faqFaqKeyword =
                        faqFaqKeywordRepository.save(new FaqFaqKeyword(faqFaqKeywordId, faq, faqKeyword)); // 建立關聯物件

                        // // 設定faqkeyword物件關聯的中介屬性
                        // List<FaqFaqKeyword> list = new ArrayList<>(); // 建立關聯物件List
                        // list.add(faqFaqKeyword); // 加入新關聯物件
                        // faqKeyword.setFaqLinks(list); // 將新關鍵字物件加入物件List

                        // // 設定faq物件關聯的中介屬性
                        // List<FaqFaqKeyword> list2 = new ArrayList<>();
                        // if (faq.getKeywordLinks() != null) { // 如果已有現有關鍵字
                        // list2 = faq.getKeywordLinks(); // 取得faq物件現有關鍵字連結
                        // }

                        // list2.add(faqFaqKeyword); // 加入新關聯物件
                        // faq.setKeywordLinks(list2); // 設定faq物件關鍵字連結

                    }
                }
                if (entity.getQuestion() != null && !entity.getQuestion().equals(faq.getQuestion())) {
                    faq.setQuestion(entity.getQuestion());
                }
                if (entity.getAnswer() != null && !entity.getAnswer().equals(faq.getAnswer())) {
                    faq.setAnswer(entity.getAnswer());
                }
                if (entity.getPostStatus() != null && !entity.getPostStatus().equals(faq.getPostStatus())) {
                    faq.setPostStatus(entity.getPostStatus());
                }
                if (entity.getCommunity() != null && entity.getCommunity().getCommunityId() != null) {

                    if (!communityRepository.findById(entity.getCommunity().getCommunityId()).get()
                            .equals(faq.getCommunity())) {
                        log.warn("不得變更FAQ所屬的社區");
                    }
                }

                faq.setLastModified(LocalDateTime.now());
                log.info("通過修改驗證");
                return faqRepository.save(faq);
            }
        }
        return null;

    }
    //
    //
    //

    // 刪除
    public boolean deleteById(Integer id) {
        if (id != null) {
            try {
                if (faqRepository.existsById(id)) {
                    faqRepository.deleteById(id);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    //
    //
    //

    // 查詢是否存在
    public boolean existsById(Integer id) {
        return faqRepository.existsById(id);
    }
}
