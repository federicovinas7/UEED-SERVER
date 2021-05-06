package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Address;
import edu.utn.UEEDServer.model.Bill;
import edu.utn.UEEDServer.model.PostResponse;
import edu.utn.UEEDServer.model.Reading;
import edu.utn.UEEDServer.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public PostResponse add(@RequestBody Address address)
    {
        return addressService.add(address);
    }

    @GetMapping
    public List<Address>getAll()
    {
        return addressService.getAll();
    }

    @GetMapping("/{id}")
    public Address getById(@PathVariable Integer id)
    {
        return addressService.getById(id);
    }

    @DeleteMapping("{addressId}")
    public void delete(@PathVariable Integer addressId)
    {
        addressService.delete(addressId);
    }

    @PostMapping("/update/{addressId}")
    public PostResponse updateAddress(@RequestBody Address address,@PathVariable Integer addressId)
    {
       return  addressService.updateAddress(address,addressId);
    }

    @GetMapping("/readings") //Is this endpoint correct??
    public List<Reading> getReadingsBetweenDates(@RequestParam(required = false)Integer addressId,
                                                 @RequestParam @DateTimeFormat (pattern = "yyyy-MM")LocalDateTime from,
                                                 @RequestParam @DateTimeFormat(pattern ="yyyy_MM")LocalDateTime to){

        return addressService.getReadingsBetweenDates(addressId,from,to);
    }


}
