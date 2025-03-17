package ee.avastaeesti.gameback.controller.gethint;

import ee.avastaeesti.gameback.service.gethint.GetHintService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetHintController {

    private final GetHintService getHintService;

    @GetMapping("/gethint/{questionId}")
    public String getHint(@PathVariable int questionId) {
        return getHintService.getLocationHint(questionId);
    }
}