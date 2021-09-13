package nicecall.controller;

import nicecall.domain.CatchStatus;
import nicecall.domain.Catcher;
import nicecall.domain.CatcherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.Optional;

@RestController
public class CatcherController {
    @Autowired
    CatcherRepository catcherRepository;

    @RequestMapping(value = "/catches/catchCall/{callId}/{driverName}",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
//    public ResponseEntity<Catcher> catchCall(@PathVariable String catchId, @RequestBody HashMap<String, String> map) throws Exception {
    public ResponseEntity<Catcher> catchCall(@PathVariable String callId, @PathVariable String driverName) throws Exception {

        Optional<Catcher> optionalCatcher = catcherRepository.findByCallId(Long.valueOf(callId));

        if (!optionalCatcher.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (driverName.isEmpty()) {
            throw new InvalidParameterException("<<< driverName is missing >>>");
        } else {
            Catcher catcher = optionalCatcher.get();

            // RECEIVED 상태여야만 콜 catch가 가능하다.
            if (catcher.getCatchStatus() != CatchStatus.RECEIVED) {
                throw new InvalidCallException();
            }

            catcher.setDriverName(driverName);
            catcher.setCatchStatus(CatchStatus.CAUGHT);
            catcherRepository.save(catcher);

            return new ResponseEntity<>(catcher, HttpStatus.OK);
        }
    }

    // RECEIVED 아닌 콜을 캐치하는 경우 에러처리
    @ResponseStatus(value=HttpStatus.FORBIDDEN, reason="콜이 이미 다른 기사에게 넘어갔거나 취소된 상태입니다. ")
    public class InvalidCallException extends RuntimeException {}


}
