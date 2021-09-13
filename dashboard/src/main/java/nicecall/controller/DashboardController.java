package nicecall.controller;

import nicecall.domain.Dashboard;
import nicecall.domain.DashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// GET 방식만 허용되도록 함
@RestController
@RequestMapping(value="/dashboards")
public class DashboardController {

    @Autowired
    DashboardRepository dashboardRepository;

    // this declaration allows on GET method
    @GetMapping("/{id}")
    public ResponseEntity<Dashboard> getDashboard(@PathVariable String id) {

        Optional<Dashboard> dashboardOptional = dashboardRepository.findById(Long.valueOf(id));

        if (dashboardOptional.isPresent()) {
            Dashboard payment = dashboardOptional.get();
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/myPage/{callId}", method = RequestMethod.GET)
    public ResponseEntity<List<Dashboard>> myPage(@PathVariable Long callId) {

        List<Dashboard> dashboards = dashboardRepository.findByCallId(callId);

        return new ResponseEntity<>(dashboards, HttpStatus.OK);
    }

/*    @GetMapping("/callList")
    public ResponseEntity<List<Dashboard>> callList(@RequestParam(required = false) String driverName,
                                                    @RequestParam(required = false) String location,
                                                    ) {

        List<Dashboard> dashboards = dashboardRepository.findByDriverName(driverName));

        return new ResponseEntity<>(dashboards, HttpStatus.OK);
    }*/
}
