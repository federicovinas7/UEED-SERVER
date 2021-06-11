package edu.utn.UEEDServer.controller;

import edu.utn.UEEDServer.model.Address;
import edu.utn.UEEDServer.model.Rate;
import edu.utn.UEEDServer.model.dto.AddressDTO;
import edu.utn.UEEDServer.model.dto.UserDTO;
import edu.utn.UEEDServer.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static edu.utn.UEEDServer.utils.TestUtils.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BackOfficeControllerTest {


    @Mock
    private RateService rateService;
    @Mock
    private AddressService addressService;
    @Mock
    private MeterService meterService;
    @Mock
    private BillService billService;
    @Mock
    private ReadingService readingService;
    @Mock
    private ModelMapper modelMapper;

    private BackofficeController backofficeController;

    @Mock
    private Authentication auth;

    private ResponseStatusException expectedException;

    private UserDTO employee;



    @BeforeEach
    public void setUp(){

        initMocks(this);
        backofficeController = new BackofficeController(
                rateService,addressService,meterService,billService,readingService,modelMapper);
    employee = anEmployee();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
    @Test
    public void getAllRateTest_200(){

        Integer rateId = 1;
        List<Rate>rates = new ArrayList<>();
        rates.add(aRate());

        when(auth.getPrincipal()).thenReturn(employee);
        when(rateService.getAll()).thenReturn(List.of(aRate()));

        List<Rate>actualList=backofficeController.getAllRate(auth);

        Assert.assertEquals(actualList.size(),rates.size());


    }

    @Test
    public void getAllRate_403(){

        UserDTO userDTO = aUserDTO();
        when(auth.getPrincipal()).thenReturn(userDTO);

        try {
            backofficeController.getAllRate(auth);
        }    catch(ResponseStatusException e)
        {
            expectedException = e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void getByIdRateTest_200(){
        //given
        Integer rateId = 1;
        Rate rate = aRate();

        when(auth.getPrincipal()).thenReturn(employee);
        when(rateService.getById(rateId)).thenReturn(rate);

        ResponseEntity<Rate> response = backofficeController.getByIdRate(auth,rateId);

        Assert.assertEquals(rate,response.getBody());
        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
    }

    @Test
    public void getByIdRateTest_403(){

        Integer rateId = 1;
        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try{
            backofficeController.getByIdRate(auth,rateId);
        }catch (ResponseStatusException ex){

            expectedException = ex;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());

    }

    @Test
    public void addRateTest_201(){

        Rate rate = aRate();

        when(auth.getPrincipal()).thenReturn(employee);
        when(rateService.add(rate)).thenReturn(rate);

        ResponseEntity response = backofficeController.addRate(auth,rate);

        Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatusCodeValue());


    }

    @Test
    public void addRateTest_403(){
        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try{
            backofficeController.addRate(auth,aRate());
        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void updateRateTest_200()
    {
        Rate r = aRate();
        when(auth.getPrincipal()).thenReturn(employee);
        when(rateService.getById(r.getId())).thenReturn(r);

        ResponseEntity responseEntity = backofficeController.updateRate(auth,r);

        Assert.assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCodeValue());
        Assert.assertEquals(r,responseEntity.getBody());
    }

    @Test
    public void updateRateTest_201(){

        Rate rate = aRate();
        when(auth.getPrincipal()).thenReturn(employee);

        when(rateService.getById(2)).thenReturn(null);

        ResponseEntity response = backofficeController.updateRate(auth,rate);

        Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatusCodeValue());


    }

    @Test
    public void updateRateTest_403(){

        when(auth.getPrincipal()).thenReturn(aUserDTO());
        try{
            backofficeController.updateRate(auth,aRate());
        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void deleteRateTest_200(){

        when(auth.getPrincipal()).thenReturn(employee);

         Mockito.doNothing().when(rateService).delete(1);

         backofficeController.deleteRate(auth,1);

         Mockito.verify(rateService,Mockito.times(1)).delete(1);

    }

    @Test
    public void deleteRateTest_403(){

        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try{
            backofficeController.deleteRate(auth,1);
        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());

    }

    @Test
    public void getAllAddressTest_200(){

        List<Address>addresses = List.of(anAddress());

        when(auth.getPrincipal()).thenReturn(employee);
        when(addressService.getAll()).thenReturn(addresses);

        List<Address>actual=backofficeController.getAllAddress(auth);

        Assert.assertEquals(addresses.size(),actual.size());
    }

    @Test
    public void getAllAddressTest_403(){

        when(auth.getPrincipal()).thenReturn(aUserDTO());
        try{
            backofficeController.getAllAddress(auth);
        }catch (ResponseStatusException e){
            expectedException = e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void getByIdAddress_200(){

        Integer addressId = 1;
        Address address = anAddress();
        when(auth.getPrincipal()).thenReturn(employee);
        when(addressService.getById(addressId)).thenReturn(address);

        ResponseEntity<Address>response = backofficeController.getByIdAddress(auth,addressId);

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());
        Assert.assertEquals(address,response.getBody());

    }

    @Test
    public void getByIdAddress_403(){
        Integer addressId = 1;
        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try {
            backofficeController.getByIdAddress(auth,addressId);

        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void addAddressTest_201(){

        //given
        AddressDTO addressDTO = anAddressDTO();
        Address address = anAddress();
        Integer clientId=1;
        Integer rateId = 1;

        when(auth.getPrincipal()).thenReturn(employee);
        when(modelMapper.map(addressDTO,Address.class)).thenReturn(address);
        when(addressService.add(clientId,rateId,address)).thenReturn(address);

        ResponseEntity response =backofficeController.addAddress(auth,addressDTO);


        Assert.assertEquals(HttpStatus.CREATED.value(),response.getStatusCodeValue());

    }

    @Test
    public void addAddressTest_403(){


        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try {
            backofficeController.addAddress(auth,anAddressDTO());

        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }

    @Test
    public void updateAddressTest_200(){
        //given
        AddressDTO addressDTO = anAddressDTO();
        Integer clientId = 1;
        Integer rateId = 1;
        when(auth.getPrincipal()).thenReturn(employee);
        when(modelMapper.map(addressDTO,Address.class)).thenReturn(anAddress());
        when(addressService.update(clientId,rateId,anAddress())).thenReturn(anAddress());

        ResponseEntity<Address> response = backofficeController.updateAddress(auth,addressDTO);

        Assert.assertEquals(HttpStatus.OK.value(),response.getStatusCodeValue());

    }

    @Test
    public void updateAddressTest_403(){

        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try {
            backofficeController.updateAddress(auth,anAddressDTO());

        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());

    }
    @Test
    public void deleteAddressTest_200(){
        //given
        Integer addressId =1;
        when(auth.getPrincipal()).thenReturn(employee);
        doNothing().when(addressService).delete(addressId);

        backofficeController.deleteAddress(auth,addressId);

        verify(addressService,times(1)).delete(addressId);
    }

    @Test
    public void deleteAddressTest_403(){
        Integer addressId = 1;
        when(auth.getPrincipal()).thenReturn(aUserDTO());

        try {
            backofficeController.deleteAddress(auth,addressId);

        }catch (ResponseStatusException e){
            expectedException =e;
        }

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(),expectedException.getStatus().value());
    }
}

