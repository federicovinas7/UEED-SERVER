package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.*;
import edu.utn.UEEDServer.model.dto.AddressDTO;
import edu.utn.UEEDServer.model.dto.ConsumersDTO;

import edu.utn.UEEDServer.model.dto.UserDTO;
import edu.utn.UEEDServer.service.*;
import edu.utn.UEEDServer.utils.EntityURLBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.Date;
import java.util.List;

import static edu.utn.UEEDServer.utils.Constants.*;

@RestController
@RequestMapping("/backoffice")
public class BackofficeController {

    private static final String RATE_PATH = "/rate";
    private static final String ADDRESS_PATH = "/address";
    private static final String METER_PATH = "/meter";
    private static final String CLIENT_PATH = "/client";

    private RateService rateService;
    private AddressService addressService;
    private MeterService meterService;
    private BillService billService;
    private ReadingService readingService;
    private ModelMapper modelMapper;

    @Autowired
    public BackofficeController(RateService rateService, AddressService addressService,
                                MeterService meterService, BillService billService,
                                ReadingService readingService, ModelMapper modelMapper) {
        this.rateService = rateService;
        this.addressService = addressService;
        this.meterService = meterService;
        this.billService = billService;
        this.readingService = readingService;
        this.modelMapper = modelMapper;
    }

/* RATES ENDPOINTS */

    @GetMapping(RATE_PATH)
    public List<Rate> getAllRate(Authentication auth) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return rateService.getAll();
    }

    @GetMapping(RATE_PATH + "/{id}")
    public Rate getByIdRate(Authentication auth, @PathVariable Integer id) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return rateService.getById(id);
    }

    @PostMapping(RATE_PATH)
    public PostResponse addRate(Authentication auth, @RequestBody Rate rate) {
        Rate added = rateService.add(rate);
        return PostResponse.builder().
                status(HttpStatus.CREATED).
                url(EntityURLBuilder.buildURL(BACKOFFICE_PATH + RATE_PATH, added.getId())).
                build();
    }

    @PutMapping(RATE_PATH)
    public PostResponse updateRate(Authentication auth, @RequestBody Rate rate) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");

        Rate saved = rateService.update(rate);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(BACKOFFICE_PATH + RATE_PATH, saved.getId().toString()))
                .build();
    }

    // Shall we implement DELETE method??
    @DeleteMapping(RATE_PATH + "/{id}")
    public void deleteRate(Authentication auth, @PathVariable Integer id) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        rateService.delete(id);
    }


/* ADDRESSES ENDPOINTS */

    @GetMapping(ADDRESS_PATH)
    public List<Address> getAllAddress(Authentication auth) {
        return addressService.getAll();
    }

    @GetMapping(ADDRESS_PATH + "/{id}")
    public Address getByIdAddress(Authentication auth, @PathVariable Integer id) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return addressService.getById(id);
    }

    @PostMapping(ADDRESS_PATH)
    public PostResponse addAddress(Authentication auth, @RequestBody AddressDTO addressDTO) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        Address newAddress = modelMapper.map(addressDTO,Address.class);
        Address savedAddress = addressService.add(addressDTO.getClientId(), addressDTO.getRateId(), newAddress);

        return PostResponse.builder().
                status(HttpStatus.CREATED).
                url(EntityURLBuilder.buildURL(BACKOFFICE_PATH + ADDRESS_PATH, savedAddress.getAddressId())).
                build();
    }

    @PutMapping(ADDRESS_PATH)
    public PostResponse updateAddress(Authentication auth, @RequestBody AddressDTO addressDTO) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");

        Address newAddress = modelMapper.map(addressDTO,Address.class);
        Address updated = addressService.update(addressDTO.getClientId(), addressDTO.getRateId(), newAddress);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(BACKOFFICE_PATH + ADDRESS_PATH, updated.getAddressId()))
                .build();
    }

    @DeleteMapping(ADDRESS_PATH + "/{addressId}")
    public void deleteAddress(Authentication auth, @PathVariable Integer addressId) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        addressService.delete(addressId);
    }


/* METER ENDPOINTS */

    @GetMapping
    public List<Meter> getAllMeter(Authentication auth) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return this.meterService.getAll();
    }

    @GetMapping(METER_PATH + "/{serialNumber}")
    public Meter getByIdMeter(Authentication auth, @PathVariable String serialNumber) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return this.meterService.getById(serialNumber);
    }

    @PostMapping(METER_PATH)
    public PostResponse addMeter(Authentication auth, @RequestBody Meter newMeter) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        Meter saved = meterService.add(newMeter);

        return PostResponse.builder().
                status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(BACKOFFICE_PATH + METER_PATH, saved.getSerialNumber().toString()))
                .build();
    }

    @PutMapping(METER_PATH)
    public PostResponse updateMeter(Authentication auth, @RequestBody Meter meter) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        Meter m = meterService.update(meter);

        return PostResponse.builder().
                status(HttpStatus.OK)
                .url(EntityURLBuilder.buildURL(METER_PATH, m.getSerialNumber().toString()))
                .build();
    }

    @DeleteMapping(METER_PATH + "/{serialNumber}")
    public void deleteMeter(Authentication auth, @PathVariable String serialNumber) {
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        this.meterService.delete(serialNumber);
    }


/* BILL ENDPOINTS */

    @GetMapping(CLIENT_PATH + "/{clientId}/bills") // VER QUERY DSL PARA LOS DIFERENTES FILTERS
    public List<Bill> filterByDate(Authentication auth,
                                   @PathVariable Integer clientId,
                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date from,
                                   @RequestParam @DateTimeFormat(pattern="yyyy-MM") Date to){
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return this.billService.filterByClientAndDate(clientId,from,to);
    }

    @GetMapping(CLIENT_PATH + "/{clientId}/bills/unpaid")
    public List<Bill>getUnpaidBillClient(Authentication auth, @PathVariable Integer clientId){
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return billService.getClientUnpaid(clientId);
    }

    @GetMapping(ADDRESS_PATH + "/{addressId}/bills/unpaid")
    public List<Bill>getUnpaidBillAddress(Authentication auth, @PathVariable Integer addressId){
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
            return billService.getAddressUnpaid(addressId);
    }

/* READING ENDPOINTS */

    @GetMapping(ADDRESS_PATH + "/{addressId}/readings")
    public List<Reading> getAddressReadings(Authentication auth,
                                            @PathVariable Integer addressId,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date to){
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return this.readingService.getAddressReadingsByDate(addressId,from,to);
    }


/* CLIENT ENDPOINTS */

    @GetMapping(CLIENT_PATH + "/topconsumers")
    public List<ConsumersDTO> getTopConsumers( Authentication auth,
                                               @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                               @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to){
        if(!((UserDTO) auth.getPrincipal()).getEmployee())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access forbidden for your profile.");
        return this.readingService.getTopConsumers(from,to);
    }
}

