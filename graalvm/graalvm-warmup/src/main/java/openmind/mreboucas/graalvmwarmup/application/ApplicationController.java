package openmind.mreboucas.graalvmwarmup.application;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class ApplicationController {

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Hi everybody!!");
    }
}
