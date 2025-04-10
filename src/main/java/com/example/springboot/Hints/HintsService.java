package com.example.springboot.Hints;

import com.example.springboot.Challenge.Challenge;
import com.example.springboot.Challenge.ChallengeRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HintsService {

//    Variable
    private final HintsRepo hintsRepo;
    private final ChallengeRepo challengeRepo;

//    Constructor
    public HintsService(HintsRepo hintsRepo,ChallengeRepo challengeRepo){
        this.hintsRepo = hintsRepo;
        this.challengeRepo = challengeRepo;
    }

//    DTO
    private HintsDTO convertToDto(Hints hints){
        return new HintsDTO(hints.getHint_1(),hints.getHint_2());
    }
    private Hints convertToEntity(HintsDTO hintsDto){
        return new Hints(hintsDto.getHint_1(),hintsDto.getHint_2());
    }


//    CRUD

    //    GET
    public List<HintsDTO> getHintsAll() {
        List<HintsDTO> hints = hintsRepo.findAll().stream().map(this::convertToDto).toList();
        return hints;
    }

    //    POST
    public void postHints(HintsDTO hintsDto,long challengeId) {

        Challenge challenge = challengeRepo.findById(challengeId).orElseThrow(()-> new EntityNotFoundException("Challenge Not found"));

        Hints hints = convertToEntity(hintsDto);

        challenge.setHints(hints);

        hintsRepo.save(hints);
    }

    //    PUT
    public void putHints(HintsDTO hintsDto, long id) {

        Hints hints = hintsRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Hints not found"));

        hints.setHint_1(hintsDto.getHint_1());
        hints.setHint_2(hintsDto.getHint_2());

        hintsRepo.save(hints);
    }

    //    DELETE
    public void deleteHints(long id) {
        Hints hints = hintsRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Hints not found"));

        hintsRepo.delete(hints);
    }
}
