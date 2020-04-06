package com.accounting.controller;

import com.accounting.service.AccountSymbolsService;
import com.accounting.service.ExceptionsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.accounting.config.Utils.USER_MESSAGE_LOG;

@Controller
@RequestMapping("resource")
public class ResourceController {

    private static final Logger logger = LogManager.getLogger(ResourceController.class);

    private AccountSymbolsService accountSymbolsService;
    private ExceptionsService exceptionsService;

    @Autowired
    ResourceController(AccountSymbolsService accountSymbolsService, ExceptionsService exceptionsService) {
        this.accountSymbolsService = accountSymbolsService;
        this.exceptionsService = exceptionsService;
    }

    @GetMapping(value = "/get/accountSymbols")
    public ResponseEntity<byte[]> getClassSymbolsRequest(HttpServletRequest request) throws IOException {
        logger.info(USER_MESSAGE_LOG,
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        byte[] data = accountSymbolsService.readFile().readAllBytes();
        return new ResponseEntity<>(data, prepareHeaderForDownload("account-symbols.json", data), HttpStatus.OK);
    }

    @PostMapping(value = "/add/accountSymbols")
    public String addClassSymbolsRequest(@RequestParam("symbolsFile") MultipartFile file, HttpServletRequest request) throws IOException {
        logger.info(USER_MESSAGE_LOG,
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        accountSymbolsService.write(file.getInputStream());
        return "redirect:/admin?symbolsUploaded";
    }

    @GetMapping(value = "/get/exceptions")
    public ResponseEntity<byte[]> getExceptionsRequest(HttpServletRequest request) throws IOException {
        logger.info(USER_MESSAGE_LOG,
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        byte[] data = exceptionsService.readFile().readAllBytes();
        return new ResponseEntity<>(data, prepareHeaderForDownload("exceptions.json",data), HttpStatus.OK);
    }

    @PostMapping(value = "/add/exceptions")
    public String addExceptionsRequest(@RequestParam("exceptionsFile") MultipartFile file, HttpServletRequest request) throws IOException {
        logger.info(USER_MESSAGE_LOG,
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        exceptionsService.write(file.getInputStream());
        return "redirect:/admin?exceptionsUploaded";
    }

    private HttpHeaders prepareHeaderForDownload(String fileName, byte[] data){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentLength(data.length);
        httpHeaders.setContentType(new MediaType("text", "json"));
        httpHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return httpHeaders;
    }
}
