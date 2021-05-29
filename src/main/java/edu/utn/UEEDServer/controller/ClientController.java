package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.Client;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.model.User;
import edu.utn.UEEDServer.model.dto.UserDTO;
import edu.utn.UEEDServer.service.BillService;
import edu.utn.UEEDServer.service.ClientService;
import edu.utn.UEEDServer.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/client")
public class ClientController {

    ReadingService readingService;
    BillService billService;
    ClientService clientService;

    @Autowired
    public ClientController(ReadingService readingService, BillService billService, ClientService clientService) {
        this.readingService = readingService;
        this.billService = billService;
        this.clientService = clientService;
    }

    @GetMapping("/{clientId}/bill") // VER QUERY DSL PARA LOS DIFERENTES FILTERS
    public List<Bill> filterByClientAndDate(Authentication auth,
                                            @PathVariable Integer clientId,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date from,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date to){
        Client loggedClient = this.clientService.getByUsername( ((UserDTO) auth.getPrincipal()).getUsername() );
        if(loggedClient.getId() != clientId)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access forbidden. Verify clientId.");
        return this.billService.filterByClientAndDate(clientId,from,to);
    }

    @GetMapping("/{clientId}/unpaid")
    public List<Bill>getUnpaidBillClient(Authentication auth, @PathVariable Integer clientId){
        Client loggedClient = this.clientService.getByUsername( ((UserDTO) auth.getPrincipal()).getUsername() );
        if(loggedClient.getId() != clientId)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access forbidden. Verify clientId.");
        return billService.getClientUnpaid(clientId);
    }

    @GetMapping("/{clientId}/consumption")
    public Map<String, Float> getClientConsumption(Authentication auth,
                                                   @PathVariable Integer clientId,
                                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to){
        Client loggedClient = this.clientService.getByUsername( ((UserDTO) auth.getPrincipal()).getUsername() );
        if(loggedClient.getId() != clientId)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access forbidden. Verify clientId.");
        return this.readingService.getClientConsumption(clientId,from,to);
    }

    @GetMapping("/{clientId}/readings")
    public List<Reading> getClientReadingsByDate(Authentication auth,
                                                 @PathVariable Integer clientId,
                                                 @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                                 @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to){
        Client loggedClient = this.clientService.getByUsername( ((UserDTO) auth.getPrincipal()).getUsername() );
        if(loggedClient.getId() != clientId)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access forbidden. Verify clientId.");
        return this.readingService.getClientReadingsByDate(clientId,from,to);
    }
}
