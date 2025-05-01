package com.example.springboot.Hints;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/hints")
public class HintsController {

    private final HintsService hintsService;

//    Constructor
    public HintsController(HintsService hintsService){
        this.hintsService = hintsService;
    }

//    CRUD

    //    GET
    @GetMapping("/get/all")
    public List<HintsDTO> getHintsAll(){
        return hintsService.getHintsAll();
    }

    //    POST
    @PostMapping("/post/{challengeId}")
    public void postHints(@RequestBody HintsDTO hintsDTO, @PathVariable("challengeId") long challengeId){
        hintsService.postHints(hintsDTO, challengeId);
    }

    //    PUT
    @PatchMapping("/put/{id}")
    public void putHints(@RequestBody HintsDTO hintsDto,@PathVariable("id") long id){
        hintsService.putHints(hintsDto, id);
    }

    //    DELETE
    @DeleteMapping("delete/{id}")
    public void deleteHints(@PathVariable("id") long id){
        hintsService.deleteHints(id);
    }
}
