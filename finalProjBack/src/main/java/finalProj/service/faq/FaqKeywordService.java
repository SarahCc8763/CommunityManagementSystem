package finalProj.service.faq;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.faq.FaqKeyword;
import finalProj.repository.faq.FaqKeywordRepository;

@Service
@Transactional
public class FaqKeywordService {

    @Autowired
    private FaqKeywordRepository faqKeywordRepository;

    public List<FaqKeyword> findAll() {
        return faqKeywordRepository.findAll();
    }

    public Optional<FaqKeyword> findById(Integer id) {
        return faqKeywordRepository.findById(id);
    }

    public FaqKeyword save(FaqKeyword entity) {
        return faqKeywordRepository.save(entity);
    }

    public void deleteById(Integer id) {
        faqKeywordRepository.deleteById(id);
    }
}
