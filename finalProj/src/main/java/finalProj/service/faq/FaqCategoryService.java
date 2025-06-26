package finalProj.service.faq;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.faq.FaqCategory;
import finalProj.repository.faq.FaqCategoryRepository;

@Service
@Transactional
public class FaqCategoryService {

    @Autowired
    private FaqCategoryRepository faqCategoryRepository;

    public List<FaqCategory> findAll() {
        return faqCategoryRepository.findAll();
    }

    public Optional<FaqCategory> findByName(String name) {
        return faqCategoryRepository.findByName(name);
    }

    public FaqCategory save(FaqCategory entity) {
        return faqCategoryRepository.save(entity);
    }

    public void deleteByName(String name) {
        faqCategoryRepository.deleteByName(name);
    }

}
