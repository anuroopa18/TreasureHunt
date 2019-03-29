package com.example.demo.Controller;

import com.example.demo.Entity.HintEntity;
import com.example.demo.Repository.HintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
public class HintController {
    @Autowired
    private HintRepository hintRepository;

    @GetMapping("/api/hints")
    public Iterable<HintEntity> findAllHints() {
        return hintRepository.findAll();
    }

    @GetMapping("/api/hints/{id}")
    public HintEntity findHintById(@PathVariable("id") int id) {
        return hintRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException("Hint is not found" + id));
    }

    @PostMapping("/api/hints")
    public HintEntity createHint(@RequestBody (required = false) HintEntity hint) {
        return hintRepository.save(hint);
    }

    @DeleteMapping("/api/hints/{id}")
    public void deleteHint(@PathVariable("id") Integer id) {
        hintRepository.deleteById(id);
    }

    @PutMapping("/api/teams/{id}")
    public HintEntity updateHint(@RequestBody (required = false) HintEntity newHint, @PathVariable("id") Integer id) {
        return hintRepository.findById(id)
            .map(hint -> {
                if (newHint.getText() != hint.getText())
                    hint.setText(newHint.getText());
                if (newHint.getPoints() != hint.getPoints())
                    hint.setPoints(newHint.getPoints());
                return hintRepository.save(hint);
            })
            .orElseGet(() -> {
                newHint.setId(id);
                return hintRepository.save(newHint);
            });
    }
}
