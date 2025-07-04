package finalProj.service.faq;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.embed.FaqFaqKeywordId;
import finalProj.domain.faq.FaqFaqKeyword;
import finalProj.repository.faq.FaqFaqKeywordRepository;

@Service
@Transactional
public class FaqFaqKeywordService {

    @Autowired
    private FaqFaqKeywordRepository faqFaqKeywordRepository;

    public List<FaqFaqKeyword> findAll() {
        return faqFaqKeywordRepository.findAll();
    }

    public Optional<FaqFaqKeyword> findById(FaqFaqKeywordId id) {
        return faqFaqKeywordRepository.findById(id);
    }

    public FaqFaqKeyword save(FaqFaqKeyword entity) {
        return faqFaqKeywordRepository.save(entity);
    }

}
